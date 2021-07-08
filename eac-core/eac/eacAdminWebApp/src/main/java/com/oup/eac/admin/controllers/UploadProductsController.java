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
import com.oup.eac.service.AsyncUploadService;



@Controller
@RequestMapping("/mvc/uploadProducts")
public class UploadProductsController {
	private static final String UPLOAD_PRODUCTS_VIEW = "uploadProducts";
	private static final String SUCCESS_REDIRECT_URL = "redirect:/mvc/uploadProducts/formUpload.htm?statusMessageKey=";
	private static final String ERROR_REDIRECT_URL = "redirect:/mvc/uploadProducts/formUpload.htm?errorMessageKey=";
	private static final String PRODUCTS_FILE_UPLOAD_SUCCESS = "status.upload.success";
	private static final String PRODUCT_FILE_HEADER_UNMATCHED_ERROR = "error.unmatched.header";
	private static final String PRODUCT_FILE_SHEET_NUMBER_ERROR = "error.noOf.sheets";
	private static final String PRODUCT_FILE_NOT_UPLOADED = "error.file.not.uploaded";
	private static final String PRODUCT_FILE_FORMAT_ERROR = "error.file.format";
	final String productHeader = "[Name, State, OrganisationalUnit, RegistrationType, HomePage, LandingPage, Email, SLA, Page_Account, EmailValidation, ActivationCode, Page_Product, Activation, Validator, LicenceType, StartDate, EndDate, TotalConcurrency, UserConcurrency, AllowedUsages, TimePeriod, UnitType, BeginOn, SendUserConfirmationEmail, externalId, externalSystem, externalSystemType, Protocol, Host, Path, Query, Fragment, Expression]";
	final String tempFileLocation = System.getProperty("java.io.tmpdir")+File.separator;
	final String linkedProductHeader="[Parent_Product_Name, Child_Product_Name]"; 
	final String productPlatformHeader="[Product_Name, Platform_Codes]"; 
	
	private final AsyncUploadService asyncUploadService;

	   
	    @Autowired
	    public UploadProductsController(final AsyncUploadService asyncUploadService) {
	        this.asyncUploadService = asyncUploadService;
	    }
	
	 @RequestMapping(value = { "/formUpload.htm" }, method = RequestMethod.GET)
	 public ModelAndView showForm() {
		 return showFormInternal();
	 }	
	
	 private ModelAndView showFormInternal() {
	        ModelAndView modelAndView = new ModelAndView(UPLOAD_PRODUCTS_VIEW);
	        return modelAndView;
	    }
	 
	 @RequestMapping(value = { "upload.htm" }, method = RequestMethod.POST)
	 public ModelAndView upload( @ModelAttribute("uploadForm") FileUploadForm uploadForm) throws ErightsException, IllegalStateException, IOException{
		 AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		 MultipartFile file = uploadForm.getMyfile();
		 if(file == null){
			 return new ModelAndView(ERROR_REDIRECT_URL + PRODUCT_FILE_NOT_UPLOADED);
		 }
		 
		 String fileName= file.getOriginalFilename();
		 if(StringUtils.isBlank(fileName)){
			 return new ModelAndView(ERROR_REDIRECT_URL + PRODUCT_FILE_NOT_UPLOADED);
		 }
		 
		 String name = fileName.substring(0,fileName.lastIndexOf("."));
		 String extension=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		 if(!extension.equals("xls")){
			 return new ModelAndView(ERROR_REDIRECT_URL + PRODUCT_FILE_FORMAT_ERROR);
		 }
			 File uploadedFile = new File(tempFileLocation + name +"_"+ System.currentTimeMillis() +"."+ extension);
			 file.transferTo(uploadedFile);
			 //**code for reading xls
			 String validatorMsg = validateUploadedFile(uploadedFile);
				 if(validatorMsg.equalsIgnoreCase("matched")){
					 asyncUploadService.createProductBatch(uploadedFile,adminUser);
					 return new ModelAndView(SUCCESS_REDIRECT_URL + PRODUCTS_FILE_UPLOAD_SUCCESS);
				 }else{
					 uploadedFile.delete();
					 return new ModelAndView(ERROR_REDIRECT_URL + PRODUCT_FILE_HEADER_UNMATCHED_ERROR);
				 }
	 }
	 
	 private String validateUploadedFile(File uploadedFile) throws IOException{
		 String msg="";
		 FileInputStream file = new FileInputStream(uploadedFile);
		 HSSFWorkbook workbook = new HSSFWorkbook(file);
		 int noOfSheets= workbook.getNumberOfSheets();

		 if(noOfSheets >= 1){
			boolean ProductHeaderMatched = validateProductSheet(workbook.getSheetAt(0));
			boolean linkedProductHeaderMatched=true;
			boolean productPlatformHeaderMatched=true;
			if(noOfSheets >= 2){
			 linkedProductHeaderMatched=validateLinkedProduct(workbook.getSheetAt(1));
			}
			if (noOfSheets >= 3) {
				productPlatformHeaderMatched=validateProductPlatform(workbook.getSheetAt(2)) ;
			}
			if(ProductHeaderMatched && linkedProductHeaderMatched && productPlatformHeaderMatched){
				msg = "matched";
			}else{
				msg=PRODUCT_FILE_HEADER_UNMATCHED_ERROR;
			}
		 }else{
			 msg= PRODUCT_FILE_SHEET_NUMBER_ERROR;
		 }
		 return msg;
	 }	 
		 
	 private boolean validateProductSheet(HSSFSheet productSheet){
		 Row row = productSheet.getRow(0);
		 for (int i = 0; i <= 32; i++) {
			 if(row.getCell(i)==null){
				 row.createCell(i).setCellValue("");
			 }
			 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
				 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			 }
		 }
		 String sheetHeader ="["+row.getCell(0).getStringCellValue()+", "
					 			+row.getCell(1).getStringCellValue()+", "
					 			+row.getCell(2).getStringCellValue()+", "
					 			+row.getCell(3).getStringCellValue()+", "
					 			+row.getCell(4).getStringCellValue()+", "
					 			+row.getCell(5).getStringCellValue()+", "
					 			+row.getCell(6).getStringCellValue()+", "
					 			+row.getCell(7).getStringCellValue()+", "
					 			+row.getCell(8).getStringCellValue()+", "
					 			+row.getCell(9).getStringCellValue()+", "
					 			+row.getCell(10).getStringCellValue()+", "
					 			+row.getCell(11).getStringCellValue()+", "
					 			+row.getCell(12).getStringCellValue()+", "
					 			+row.getCell(13).getStringCellValue()+", "
					 			+row.getCell(14).getStringCellValue()+", "
					 			+row.getCell(15).getStringCellValue()+", "
					 			+row.getCell(16).getStringCellValue()+", "
					 			+row.getCell(17).getStringCellValue()+", "
					 			+row.getCell(18).getStringCellValue()+", "
					 			+row.getCell(19).getStringCellValue()+", "
					 			+row.getCell(20).getStringCellValue()+", "
					 			+row.getCell(21).getStringCellValue()+", "
					 			+row.getCell(22).getStringCellValue()+", "
					 			+row.getCell(23).getStringCellValue()+", "
					 			+row.getCell(24).getStringCellValue()+", "
					 			+row.getCell(25).getStringCellValue()+", "
					 			+row.getCell(26).getStringCellValue()+", "
					 			+row.getCell(27).getStringCellValue()+", "
					 			+row.getCell(28).getStringCellValue()+", "
					 			+row.getCell(29).getStringCellValue()+", "
					 			+row.getCell(30).getStringCellValue()+", "
					 			+row.getCell(31).getStringCellValue()+", "
					 			+row.getCell(32).getStringCellValue()+"]";
		 
		 return sheetHeader.equalsIgnoreCase(productHeader);
	 }
	 private boolean validateLinkedProduct(HSSFSheet productSheet){
		 Row row = productSheet.getRow(0);

		 for (int i = 0; i <= 1; i++) {
			 if(row.getCell(i)==null){
				 row.createCell(i).setCellValue("");
			 }
			 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
				 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			 }
		 }
		 String sheetHeader ="["+row.getCell(0).getStringCellValue()+", "
					 			+row.getCell(1).getStringCellValue()+
					 			"]";


		 return sheetHeader.equalsIgnoreCase(linkedProductHeader);
	 }
	 private boolean validateProductPlatform(HSSFSheet productSheet){
		 Row row = productSheet.getRow(0);

		 for (int i = 0; i <= 1; i++) {
			 if(row.getCell(i)==null){
				 row.createCell(i).setCellValue("");
			 }
			 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
				 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			 }
		 }
		 String sheetHeader ="["+row.getCell(0).getStringCellValue()+", "
					 			+row.getCell(1).getStringCellValue()+
					 			"]";


		 return sheetHeader.equalsIgnoreCase(productPlatformHeader);
	 }

} 

