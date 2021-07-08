package com.oup.eac.admin.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ImportService;



@Controller
@RequestMapping("/mvc/BulkCreateActivationCodes")
public class BulkCreateActivationController {
	private static final String PRODUCT_UPDATE_SUCCESS = "status.product.update.success";
	private static final String BULK_CREATE_ACTIVATION_CODES_VIEW = "bulkCreateActivationCodes";
	private static final String SUCCESS_REDIRECT_URL = "redirect:/mvc/BulkCreateActivationCodes/bulkActivationcodeupload.htm?statusMessageKey=";
	private static final String ERROR_REDIRECT_URL = "redirect:/mvc/BulkCreateActivationCodes/bulkActivationcodeupload.htm?errorMessageKey=";
	private static final String ACTIVATIONCODE_FILE_UPLOAD_SUCCESS = "status.bulkupload.success";
	private static final String ACTIVATIONCODE_FILE_HEADER_UNMATCHED_ERROR = "error.unmatched.header";
	private static final String ACTIVATIONCODE_FILE_SHEET_NUMBER_ERROR = "error.noOf.sheets";
	private static final String ACTIVATIONCODE_FILE_NOT_UPLOADED = "error.file.not.uploaded";
	private static final String ACTIVATIONCODE_FILE_FORMAT_ERROR = "error.file.format";
	private static final String ACTIVATIONCODE_FILE_MAXLIMIT_UNMATCHED_ERROR = "error.file.maxlimit";
	private static final String ACTIVATIONCODE_FILE_SHEET_UNMATCHED_ERROR = "error.file.sheetname";
	public static String ACTIVATIONCODE_FILE_ERROR = "error.file.upload";
	final String batchHeader = "[Batch Name, Format, Number Of Tokens, Allowed Usage, Token Validity Start Date, Token Validity End Date]";
	final String tempFileLocation = System.getProperty("java.io.tmpdir")+File.separator;
	final String licenseHeader="[Batch Name, Product, Product Group, Licence Type, Licence Start Date, Licence End Date, Total Concurrency, User Concurrency, Time Period, Unit Type, Begin On, Allowed Usage]"; 	
	
	private final ImportService importService;

		@Autowired
	    public BulkCreateActivationController(final ImportService importService) {
	        this.importService = importService;
	    }
	
	 @RequestMapping(value = { "/bulkActivationcodeupload.htm" }, method = RequestMethod.GET)
	 public ModelAndView showForm() {
		 return showFormInternal();
	 }	
	
	 private ModelAndView showFormInternal() {
	        ModelAndView modelAndView = new ModelAndView(BULK_CREATE_ACTIVATION_CODES_VIEW);
	        return modelAndView;
	    }
	 
	 @RequestMapping(value = { "Activationcodeupload.htm" }, method = RequestMethod.POST)
	public ModelAndView upload(
			@ModelAttribute("activationCodeBatchReport") FileUploadForm activationCodeBatchReport)
			throws ErightsException, IllegalStateException, IOException {
		AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String status = null;
		MultipartFile file = activationCodeBatchReport.getMyfile();
		if (file == null) {
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_NOT_UPLOADED);
		}
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_NOT_UPLOADED);
		}

		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		if (!extension.equals("xls")) {
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_FORMAT_ERROR);
		}
		File uploadedFile = new File(tempFileLocation + name + "_"
				+ System.currentTimeMillis() + "." + extension);
		file.transferTo(uploadedFile);
		// **code for reading xls
		String messageKey = null;
		String validatormsg = validateUploadedFile(uploadedFile);
		if (validatormsg.equalsIgnoreCase("matched")) {
			try {
				status = importService.bulkCreateActivationCodeBatch(
						uploadedFile, adminUser);
				if (uploadedFile.exists()) {
					uploadedFile.delete();
				}
				if (status == "success") {
					return new ModelAndView(SUCCESS_REDIRECT_URL
							+ ACTIVATIONCODE_FILE_UPLOAD_SUCCESS);
				} else {
					messageKey = ACTIVATIONCODE_FILE_ERROR;
					return new ModelAndView(
							"redirect:/mvc/BulkCreateActivationCodes/bulkActivationcodeupload.htm?errorMessageKey="
									+ messageKey + "&errorStatus=" + status);
				}

			} catch (Exception e) {
				if (uploadedFile.exists()) {
					uploadedFile.delete();
				}
				messageKey = ACTIVATIONCODE_FILE_ERROR;
				return new ModelAndView(
						"redirect:/mvc/BulkCreateActivationCodes/bulkActivationcodeupload.htm?errorMessageKey="
								+ messageKey + "&errorStatus=" + status);
			}
		} else if (validatormsg.equalsIgnoreCase("sheeterror")) {
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_SHEET_NUMBER_ERROR);
		}
		else if (validatormsg.equalsIgnoreCase("maxlimitcrossed")) {
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_MAXLIMIT_UNMATCHED_ERROR);
		}
		else if (validatormsg.equalsIgnoreCase("unmatchedbatchname")) {
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_SHEET_UNMATCHED_ERROR);
		}else {
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}
			return new ModelAndView(ERROR_REDIRECT_URL
					+ ACTIVATIONCODE_FILE_HEADER_UNMATCHED_ERROR);

		}
	}
	 
	private String validateUploadedFile(File uploadedFile) throws IOException {
		String msg = "";
		FileInputStream file = new FileInputStream(uploadedFile);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		int noOfSheets = workbook.getNumberOfSheets();
		if (noOfSheets == 2) {
			if (!workbook.getSheetName(0).equalsIgnoreCase("Batch")
					|| !workbook.getSheetName(1).equalsIgnoreCase("License")) {
				msg = "unmatchedbatchname";
				return msg;
			}
			boolean batchHeaderMatched = validateBatchSheet(workbook
					.getSheetAt(0));
			boolean licenseHeaderMatched = true;
			if (batchHeaderMatched) {
				licenseHeaderMatched = validateLicense(workbook.getSheetAt(1));
				if (batchHeaderMatched && licenseHeaderMatched) {
					msg = "matched";
				} else {
					msg = ACTIVATIONCODE_FILE_HEADER_UNMATCHED_ERROR;
				}
			} else {
				msg = ACTIVATIONCODE_FILE_HEADER_UNMATCHED_ERROR;
			}
		} else {
			msg = ACTIVATIONCODE_FILE_SHEET_NUMBER_ERROR;
		}
		if (workbook.getSheetAt(0).getPhysicalNumberOfRows() > 201
				|| workbook.getSheetAt(1).getPhysicalNumberOfRows() > 201) {
			msg = "maxlimitcrossed";
		}
		return msg;
	}	 
		 
	 private boolean validateBatchSheet(HSSFSheet productSheet){
		 Row row = productSheet.getRow(0);
		 for (int i = 0; i < 6; i++) {
			 if(row.getCell(i)==null){
				 row.createCell(i).setCellValue("");
			 }
			 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
				 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			 }
		 }
		 String sheetHeader ="["+row.getCell(0).getStringCellValue().trim()+", "
					 			+row.getCell(1).getStringCellValue().trim()+", "
					 			+row.getCell(2).getStringCellValue().trim()+", "
					 			+row.getCell(3).getStringCellValue().trim()+", "
					 			+row.getCell(4).getStringCellValue().trim()+", "
					 			+row.getCell(5).getStringCellValue().trim()+"]";
		 
		 return sheetHeader.equalsIgnoreCase(batchHeader);
	 }
	 private boolean validateLicense(HSSFSheet productSheet){
		 Row row = productSheet.getRow(0);

		 for (int i = 0; i < 12; i++) {
			 if(row.getCell(i)==null){
				 row.createCell(i).setCellValue("");
			 }
			 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
				 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			 }
		 }
		 String sheetHeader ="["+row.getCell(0).getStringCellValue().trim()+", "
					 			+row.getCell(1).getStringCellValue().trim()+", "
					 			+row.getCell(2).getStringCellValue().trim()+", "
					 			+row.getCell(3).getStringCellValue().trim()+", "
					 			+row.getCell(4).getStringCellValue().trim()+", "
					 			+row.getCell(5).getStringCellValue().trim()+", "
					 			+row.getCell(6).getStringCellValue().trim()+", "
					 			+row.getCell(7).getStringCellValue().trim()+", "
					 			+row.getCell(8).getStringCellValue().trim()+", "
					 			+row.getCell(9).getStringCellValue().trim()+", "
					 			+row.getCell(10).getStringCellValue().trim()+", "
					 			+row.getCell(11).getStringCellValue().trim()+"]";
		 return sheetHeader.equalsIgnoreCase(licenseHeader);
	 }

} 

