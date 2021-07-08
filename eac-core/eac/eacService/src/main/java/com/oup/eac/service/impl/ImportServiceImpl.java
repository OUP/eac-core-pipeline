package com.oup.eac.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.CircularCountryMatchException;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.integration.erights.CodeFormatEnum;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.impl.ErightsObjectFactory;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ImportService;
import com.oup.eac.service.PageDefinitionService;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationActivationService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.process.ProductUpdateProcess;

@Service("importService")
public class ImportServiceImpl implements ImportService{

	private static final Logger LOG = Logger.getLogger(ImportService.class);
	String Msg;
    private static final String EMAIL_TEMPLATE = "com/oup/eac/service/velocity/upload.vm";
    private static final String EXPORT_EMAIL_SUBJECT = "Product ID file";
	
    private final DivisionService divisionService;
	private final ErightsFacade erightsFacade;
	private final ProductService productService;
	private final PlatformService platformService;
	private final RegistrationDefinitionService registrationDefinitionService;
	private final PageDefinitionService pageDefinitionService;
	private final RegistrationActivationService registrationActivationService;
	private final ExternalIdService externalIdService;
	private final VelocityEngine velocityEngine;
	private final EmailService emailService;
	private final MessageSource messageSource;
	
	String tempFileLocation;
	
	String outputFileSuffix = ".txt";
	List<ExternalSystem> allExternalSystems;
	String pipeSep = " || ";
	String productOutputFileName;
	String linkedProductOutputFileName;
	String productPlatformOutputFileName;
	String zipFileName;
	StringBuffer productCreateSuccessBuffer;
	StringBuffer productUpdateSuccessBuffer;
	StringBuffer productFailureBuffer;
	StringBuffer linkedProductCreateSuccessBuffer;
	StringBuffer linkedProductUpdateSuccessBuffer;
	StringBuffer linkedProductFailureBuffer;
	StringBuffer productPlatformFailureBuffer;
	StringBuffer productPlatformSuccessBuffer;
	int productCreateCount;
	int productUpdateCount;
	int productFailureCount;
	int linkedProductCreateCount;
	int linkedProductUpdateCount;
	int linkedProductFailureCount;
	int productPlatformSuccessCount ;
	int productPlatformFailureCount ;
	/***********bulkcreateactivationcode********************/
	String batchOutputFileName;
	String licenseOutputFileName;
	String zipBulkFileName;
	StringBuffer batchCreateSuccessBuffer;
	StringBuffer batchUpdateSuccessBuffer;
	StringBuffer batchFailureBuffer;
	StringBuffer licenseCreateSuccessBuffer;
	StringBuffer licenseUpdateSuccessBuffer;
	StringBuffer licenseFailureBuffer;
	int batchCreateCount;
	int batchUpdateCount;
	int batchFailureCount;
	int licenseCreateCount;
	int licenseUpdateCount;
	int licenseFailureCount;
	
	
	@Autowired
	public ImportServiceImpl(
			final DivisionService divisionService,
			final ErightsFacade erightsFacade, 
			final ProductService productService, 
			final RegistrationDefinitionService registrationDefinitionService,
			final PageDefinitionService pageDefinitionService,
			final RegistrationActivationService registrationActivationService,
			final ExternalIdService externalIdService,
			final VelocityEngine velocityEngine,
			final EmailService emailService,
			final MessageSource messageSource,
			final PlatformService platformService) {
		super();
		this.divisionService = divisionService;
		this.erightsFacade = erightsFacade;
		this.productService = productService;
		this.registrationDefinitionService = registrationDefinitionService;
		this.pageDefinitionService = pageDefinitionService;
		this.registrationActivationService = registrationActivationService;
		this.externalIdService = externalIdService;
		this.velocityEngine = velocityEngine;
		this.emailService = emailService;
		this.messageSource = messageSource;
		this.platformService = platformService ;
	}
	
	private void writeToFile(String successCreateBufferString, String successUpdateBufferString, String failureBufferString, String outputFileName) {
		try {
			System.out.println("File location" + tempFileLocation+outputFileName+outputFileSuffix);
			File file=new File(tempFileLocation+outputFileName+outputFileSuffix);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			BufferedWriter bw =  new BufferedWriter(new FileWriter(file,true));
			bw.write(successCreateBufferString);
			bw.newLine();
			bw.newLine();
			bw.write(successUpdateBufferString);
			bw.newLine();
			bw.newLine();
			bw.write(failureBufferString);
			bw.flush();
			bw.close();
		} 
		catch (IOException e) 
		{
			LOG.info("Problem in file writing.");
			e.printStackTrace();
		}
	}

	@Override
	public void createProducts(File uploadedFile, AdminUser adminUser){
		tempFileLocation = System.getProperty("java.io.tmpdir")+File.separator;
		productOutputFileName = "Product_ids_"+ System.currentTimeMillis();
		linkedProductOutputFileName= "Linked_Product_"+System.currentTimeMillis();
		productPlatformOutputFileName= "Product_Platform"+System.currentTimeMillis();
		zipFileName = "Uploaded_Products_"+ System.currentTimeMillis();
		productCreateSuccessBuffer = new StringBuffer("Success : Created"+'\n'+"-----------------");
		productUpdateSuccessBuffer = new StringBuffer("Success : Updated"+'\n'+"-----------------");
		productFailureBuffer = new StringBuffer("Failure :"+'\n'+"----------");
		linkedProductCreateSuccessBuffer = new StringBuffer("Success : Created"+'\n'+"-----------------");
		linkedProductUpdateSuccessBuffer = new StringBuffer("Success : Updated"+'\n'+"-----------------");
		
		productPlatformSuccessBuffer = new StringBuffer("Success : Updated"+'\n'+"-----------------") ;
		productPlatformFailureBuffer = new StringBuffer("Failure :"+'\n'+"----------") ;
		linkedProductFailureBuffer = new StringBuffer("Failure :"+'\n'+"----------");
		productCreateCount = 0;
		productUpdateCount = 0;
		productFailureCount = 0;
		linkedProductCreateCount = 0;
		linkedProductUpdateCount = 0;
		linkedProductFailureCount = 0;
		productPlatformFailureCount = 0;
		productPlatformSuccessCount = 0 ;
		
		
		FileInputStream file;
		try {
			file = new FileInputStream(uploadedFile);
			 HSSFWorkbook workbook = new HSSFWorkbook(file);
			 HSSFSheet productSheet = workbook.getSheetAt(0);
			 createProductsInBatch(productSheet,adminUser);
			 if(workbook.getNumberOfSheets()>=2)
			 {	 
				 HSSFSheet linkedProduct = workbook.getSheetAt(1);
				 createLinkedProductsInBatch(linkedProduct,adminUser);
			 }
			 if(workbook.getNumberOfSheets()>=3)
			 {	 
				 HSSFSheet productPlatform = workbook.getSheetAt(2);
				 addPlatfromProductsInBatch(productPlatform,adminUser);
			 }
			 
			 logOutputData();
			 sendMail(adminUser);
			 deleteFiles(uploadedFile, new File(tempFileLocation+productOutputFileName+outputFileSuffix), new File(tempFileLocation+linkedProductOutputFileName+outputFileSuffix), new File(tempFileLocation+productPlatformOutputFileName+outputFileSuffix) );
		} catch (IOException | AccessDeniedException | ErightsException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String bulkCreateActivationCodeBatch(File uploadedFile, AdminUser adminUser){
		String status = null;
		FileInputStream file;
		try {
			file = new FileInputStream(uploadedFile);
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			if (workbook.getNumberOfSheets() == 2) {
				status = createBulkActivationCodeBatchBatch(workbook, adminUser);
			}
		} catch (IOException | AccessDeniedException | ErightsException e) {
			e.printStackTrace();
		}
		 return status;
	}
	
	private void deleteFiles(File uploadedFile, File productFile, File linkedProductFile, File platformProductFile){
		if(uploadedFile.exists()){
			boolean delFile = uploadedFile.delete();
            LOG.info(uploadedFile.getName() + " is deleted... Flag : " + delFile); 
		}
		if(productFile.exists()){
			boolean delFile = productFile.delete();
            LOG.info(productFile.getName() + " is deleted... Flag : " + delFile); 
		}
		if(linkedProductFile.exists()){
			boolean delFile = linkedProductFile.delete();
            LOG.info(linkedProductFile.getName() + " is deleted... Flag : " + delFile); 
		}
		if(platformProductFile.exists()){
			boolean delFile = platformProductFile.delete();
            LOG.info(platformProductFile.getName() + " is deleted... Flag : " + delFile); 
		}
	}
	
	private void logOutputData(){
		LOG.info(productCreateSuccessBuffer.toString());
		LOG.info(productUpdateSuccessBuffer.toString());
		LOG.info(productFailureBuffer.toString());
		LOG.info(linkedProductCreateSuccessBuffer.toString());
		LOG.info(linkedProductUpdateSuccessBuffer.toString());
		LOG.info(linkedProductFailureBuffer.toString());
		LOG.info(productPlatformSuccessBuffer.toString());
		LOG.info(productPlatformFailureBuffer.toString());
	}
	
	

	 private void createProductsInBatch(HSSFSheet productSheet, AdminUser adminUser) throws AccessDeniedException, ErightsException, UnsupportedEncodingException{
		 int count = 0;
		 ProductBean dpb= new ProductBean();
		 setModelPrerequisites(dpb, adminUser);
		 List<Division> divisions = new ArrayList<Division>();
		divisions = divisionService.getAllDivisions();
		 Iterator<Row> rowIterator = productSheet.iterator();
		 while(rowIterator.hasNext()){
			 Row row=rowIterator.next();
			 DataFormatter df = new DataFormatter();
			
			 for (int i = 0; i <= 32; i++) {
				 if(row.getCell(i)==null){
					 row.createCell(i).setCellValue("");
				 }
				 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
					 if(row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC){
						 if (DateUtil.isCellDateFormatted(row.getCell(i))) {
							 String tmpDte= "";
							 tmpDte = df.formatCellValue(row.getCell(i));
							 System.out.println("date tmp : "+ tmpDte);
							 row.getCell(i).setCellValue(tmpDte);
						 }
					 }
					 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
				 }
			 }
			 if(count ==0){
				 LOG.info("this is header.");
				 count++;
			 }else{
				 LOG.info("Reading line no. : "+count);
				 Boolean urlFlag=true;
				 
				 //Protocol,Host,Path,Query,Fragment,Expression,Name,OrganisationalUnit,State
				 if(StringUtils.isBlank(row.getCell(27).getStringCellValue()) 
						 && StringUtils.isBlank(row.getCell(28).getStringCellValue()) 
						 && StringUtils.isBlank(row.getCell(29).getStringCellValue()) 
						 && StringUtils.isBlank(row.getCell(30).getStringCellValue()) 
						 && StringUtils.isBlank(row.getCell(31).getStringCellValue()) 
						 && StringUtils.isBlank(row.getCell(32).getStringCellValue())){
					 LOG.info("url list is empty.");
					 urlFlag = false;
				 }
				 
				 EnforceableProductUrlDto epudto = null;
				 if(urlFlag){
					 
					 epudto = new EnforceableProductUrlDto();
					
					 if(!StringUtils.isBlank(row.getCell(27).getStringCellValue())){
						 epudto.setProtocol(row.getCell(27).getStringCellValue());
					 }
				 
					 if(!StringUtils.isBlank(row.getCell(28).getStringCellValue())){
						 epudto.setHost(row.getCell(28).getStringCellValue());
					 }
				 
					 if(!StringUtils.isBlank(row.getCell(29).getStringCellValue())){
						 epudto.setPath(row.getCell(29).getStringCellValue());
					 }
					
					 if(!StringUtils.isBlank(row.getCell(30).getStringCellValue())){
						 epudto.setQuery(row.getCell(30).getStringCellValue());
					 }
				 
					 if(!StringUtils.isBlank(row.getCell(31).getStringCellValue())){
						 epudto.setFragment(row.getCell(31).getStringCellValue());
					 }
				 
					 if(!StringUtils.isBlank(row.getCell(32).getStringCellValue())){
						 epudto.setExpression(row.getCell(32).getStringCellValue());
					 }
				 }
				 
				 String trimmedRow0 = row.getCell(0).getStringCellValue().trim();;
				 EnforceableProductDto product = null ;
				 if(!StringUtils.isBlank(trimmedRow0)){
					 try {
						 product=productService.getProductByName(trimmedRow0);
					 } catch (ErightsException e) {
						 LOG.info(e.getMessage());
					 }
				 }else{
					 LOG.info("Product name doesn't specified. Skipped product creation.");
					 appendIntoProductFailureBuffer("At line no. "+(count+1) , "Product name doesn't specified.");
					 count++;
					 continue;
				 }
				 
				 
				 
				if (product != null) {

					boolean isUpdate = false;
					try {
						if (epudto != null) {
							
							
							/*RegisterableProduct registerableProduct = (RegisterableProduct) product;
							ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
							EnforceableProductDto enforceableProduct;
							
							enforceableProduct = erightsFacade
									.getProduct(product.getErightsId());
							enforceableProduct.setAdminEmail(product.getEmail());
							enforceableProduct.setState(product.getState().toString());
							//product.get
							enforceableProduct.addEnforceableProductUrl(epudto);
							
							productService.updateRegisterableProduct(registerableProduct,productRegistrationDefinition);
							productService.updateProduct(product);*/
							//product de-duplication
							/*RegisterableProduct registerableProduct = (RegisterableProduct) product;
							ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
							LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(productRegistrationDefinition.getLicenceTemplate());
							EnforceableProductDto enforceableProduct;
							List<Integer> erightsProductIds = Arrays.asList(productRegistrationDefinition.getProduct().getErightsId());
							enforceableProduct = erightsFacade
									.getProduct(product.getErightsId());
							enforceableProduct.setAdminEmail(product.getEmail());
							enforceableProduct.setState(product.getState().toString());
							enforceableProduct.addEnforceableProductUrl(epudto);
							enforceableProduct.setHomePage(product.getHomePage());
							enforceableProduct.setLandingPage(product.getLandingPage());
							enforceableProduct.setSla(product.getServiceLevelAgreement());*/
							
							product.addEnforceableProductUrl(epudto);
							erightsFacade.updateProduct(product);
							isUpdate = true;
							
						}  
						if (!StringUtils.isBlank(row.getCell(24)
								.getStringCellValue())
								&& !StringUtils.isBlank(row.getCell(25)
										.getStringCellValue())
								&& !StringUtils.isBlank(row.getCell(26)
										.getStringCellValue())) {
							updateproduct(product, row.getCell(24)
									.getStringCellValue().replaceAll("[\n\r]", ""), row.getCell(25)
									.getStringCellValue(), row.getCell(26)
									.getStringCellValue());
							isUpdate = true;
						}
						if (isUpdate) {
							LOG.info("Product updated successfully with eRights ID : "
									+ product.getProductId());
							appendIntoProductUpdateSuccessBuffer(
									product.getName(),
									product.getProductId());
							count++;
							continue;
						}
					    else {
							appendIntoProductFailureBuffer(
									product.getName(),
									"Product Exist but URLs or External id is empty or some value missed.");
						}
					} catch (ErightsException e) {
						LOG.info("Internal Server Error while updating product");
						 appendIntoProductFailureBuffer(product.getName(), "Erights error while updating.");
						 count++;
						 continue;
					} catch (ServiceLayerException e) {
						LOG.info("Internal Server Error while updating product");
						appendIntoProductFailureBuffer(product.getName() , e.getMessage());
						count++;
						continue;
					}
				}
				else {
					
					ProductBean productBean = new ProductBean();
					if (!StringUtils.isBlank(trimmedRow0)) {
						productBean.setProductName(trimmedRow0);
					}
					// productBean.setId(product.getId());
					// ProductBean product = new
					//product de-duplication
					//EnforceableProductDto epd = null;

					if (urlFlag) {
						List<EnforceableProductUrlDto> epudList = new ArrayList<EnforceableProductUrlDto>();
						epudList.add(epudto);
						productBean.setUrls(epudList);
						//product de-duplication
						//epd = new EnforceableProductDto(trimmedRow0, epudList);
					} else {
						//product de-duplication
						//epd = new EnforceableProductDto(trimmedRow0);
					}

					// State
					if (row.getCell(1).getStringCellValue()
							.equalsIgnoreCase("ACTIVE")) {
						productBean.setState(ProductState.ACTIVE);
					} else if (row.getCell(1).getStringCellValue()
							.equalsIgnoreCase("SUSPENDED")) {
						productBean.setState(ProductState.SUSPENDED);
					} else if (row.getCell(1).getStringCellValue()
							.equalsIgnoreCase("RETIRED")) {
						productBean.setState(ProductState.RETIRED);
					} else if (row.getCell(1).getStringCellValue()
							.equalsIgnoreCase("REMOVED")) {
						productBean.setState(ProductState.REMOVED);
					} else {
						LOG.info("Product state is not ACTIVE. Skipped product creation for :"
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(
								productBean.getProductName(),
								"Product state should be ACTIVE.");
						count++;
						continue;
					}
					
					 //Division
					 if(!StringUtils.isBlank(row.getCell(2).getStringCellValue())){
						 for (Division division : divisions) {
							if(division.getDivisionType().equalsIgnoreCase(row.getCell(2).getStringCellValue())){
								productBean.setDivisionId(division.getErightsId().toString());
								break;
							}
						}
						 if(productBean.getDivisionId()==null){
							 LOG.info("Org Unit specified doesn't match with existing values. Skipped product creation for :"+trimmedRow0);
							 appendIntoProductFailureBuffer(trimmedRow0, "Org Unit doesn't match with existing values");
							 count++;
							 continue;
						 }
					 }else{
						 LOG.info("Org Unit doesn't specified. Skipped product creation for :"+trimmedRow0);
						 appendIntoProductFailureBuffer(trimmedRow0, "Org Unit doesn't specified.");
						 count++;
						 continue;
					 }
					
					// Email
					if (!StringUtils.isBlank(row.getCell(6)
							.getStringCellValue())) {
						productBean.setEmail(row.getCell(6)
								.getStringCellValue());
						//product de-duplication
						//epd.setAdminEmail(productBean.getEmail());
					} else {
						LOG.info("email does't specified. It's mandatory.Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Email does't specified");
						count++;
						continue;
					}
					
					DateTimeFormatter FORMATTER = DateTimeFormat
							.forPattern("dd/MM/yyyy");

					
					
					
					// StartDate
					if (!StringUtils.isBlank(row.getCell(15)
							.getStringCellValue())) {
						try {
							productBean.setLicenceStartDate(FORMATTER
									.parseDateTime(
											row.getCell(15).getStringCellValue())
											.toLocalDate());
						} catch (Exception e) {
							appendIntoProductFailureBuffer(trimmedRow0,
									"Date format must be DD/MM/YYYY");
							count++;
							continue;
						}
					}

					// EndDate
					if (!StringUtils.isBlank(row.getCell(16)
							.getStringCellValue())) {
						try {
							productBean.setLicenceEndDate(FORMATTER.parseDateTime(
									row.getCell(16).getStringCellValue())
									.toLocalDate());
						} catch (Exception e) {
							appendIntoProductFailureBuffer(trimmedRow0,
									"Date format must be DD/MM/YYYY");
							count++;
							continue;
						}
					}

					// TotalConcurrency
					if (!StringUtils.isBlank(row.getCell(17)
							.getStringCellValue())) {
						productBean.setTotalConcurrency(row.getCell(17)
								.getStringCellValue());
					}

					// UserConcurrency
					if (!StringUtils.isBlank(row.getCell(18)
							.getStringCellValue())) {
						productBean.setUserConcurrency(row.getCell(18)
								.getStringCellValue());
					}

					// AllowedUsages
					if (!StringUtils.isBlank(row.getCell(19)
							.getStringCellValue())) {
						productBean.setLicenceAllowedUsages(row.getCell(19)
								.getStringCellValue());
					}

					// TimePeriod
					if (!StringUtils.isBlank(row.getCell(20)
							.getStringCellValue())) {
						productBean.setTimePeriod(row.getCell(20)
								.getStringCellValue());
					}

					// UnitType
					if (!StringUtils.isBlank(row.getCell(21)
							.getStringCellValue())) {
						if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("day"))
							productBean.setRollingUnitType(RollingUnitType.DAY);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("hour"))
							productBean
									.setRollingUnitType(RollingUnitType.HOUR);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("millisecond"))
							productBean
									.setRollingUnitType(RollingUnitType.MILLISECOND);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("minute"))
							productBean
									.setRollingUnitType(RollingUnitType.MINUTE);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("month"))
							productBean
									.setRollingUnitType(RollingUnitType.MONTH);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("second"))
							productBean
									.setRollingUnitType(RollingUnitType.SECOND);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("week"))
							productBean
									.setRollingUnitType(RollingUnitType.WEEK);
						else if (row.getCell(21).getStringCellValue()
								.equalsIgnoreCase("year"))
							productBean
									.setRollingUnitType(RollingUnitType.YEAR);
					}

					// Rolling Begin On
					if (!StringUtils.isBlank(row.getCell(22)
							.getStringCellValue())) {
						if (row.getCell(22).getStringCellValue()
								.equalsIgnoreCase("creation"))
							productBean
									.setRollingBeginOn(RollingBeginOn.CREATION);
						else if (row.getCell(22).getStringCellValue()
								.toLowerCase().contains("first"))
							productBean
									.setRollingBeginOn(RollingBeginOn.FIRST_USE);
					}
					
					// License Type
					if (!StringUtils.isBlank(row.getCell(14)
							.getStringCellValue())) {
						if (row.getCell(14).getStringCellValue()
								.equalsIgnoreCase("standard")) {
							if (productBean.getTotalConcurrency() != null && productBean.getUserConcurrency() != null ) {
								productBean.setLicenceType(LicenceType.STANDARD);
							} else {
								LOG.info("License parameters invalid. Skipping product creation for "
										+ productBean.getProductName());
								appendIntoProductFailureBuffer(trimmedRow0,
										"Unknown value for licence parameters.");
								count++;
								continue;
							}
						}
						else if (row.getCell(14).getStringCellValue()
								.equalsIgnoreCase("concurrent")) {
							if (productBean.getTotalConcurrency() != null && productBean.getUserConcurrency() != null ) {
								productBean.setLicenceType(LicenceType.CONCURRENT);
							} else {
								LOG.info("License parameters invalid. Skipping product creation for "
										+ productBean.getProductName());
								appendIntoProductFailureBuffer(trimmedRow0,
										"Unknown value for licence parameters.");
								count++;
								continue;
							}
						}
						else if (row.getCell(14).getStringCellValue()
								.equalsIgnoreCase("usage")) {
							if (productBean.getLicenceAllowedUsages() != null ) {
								productBean.setLicenceType(LicenceType.USAGE);
							} else {
								LOG.info("License parameters invalid. Skipping product creation for "
										+ productBean.getProductName());
								appendIntoProductFailureBuffer(trimmedRow0,
										"Unknown value for licence parameters.");
								count++;
								continue;
							}
						}
						else if (row.getCell(14).getStringCellValue()
								.equalsIgnoreCase("rolling")) {
							if (productBean.getTimePeriod() != null && productBean.getRollingBeginOn() != null && productBean.getRollingUnitType() != null ) {
								productBean.setLicenceType(LicenceType.ROLLING);
							} else {
								LOG.info("License parameters invalid. Skipping product creation for "
										+ productBean.getProductName());
								appendIntoProductFailureBuffer(trimmedRow0,
										"Unknown value for licence parameters.");
								count++;
								continue;
							}
						}
						else {
							LOG.info("License Type specified doesn't match with the existing values. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"Unknown value for licence type.");
							count++;
							continue;
						}
					} else {
						LOG.info("License Type does't specified. Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Licence type does't specified");
						count++;
						continue;
					}
					// RegistrationType
					if (!StringUtils.isBlank(row.getCell(3)
							.getStringCellValue())) {
						if (row.getCell(3).getStringCellValue().toLowerCase()
								.contains("self")) {
							productBean
									.setRegistrationType(RegisterableType.SELF_REGISTERABLE
											.name());
						} else if (row.getCell(3).getStringCellValue()
								.toLowerCase().contains("admin")) {
							productBean
									.setRegistrationType(RegisterableType.ADMIN_REGISTERABLE
											.name());
						} else {
							LOG.info("registration type specified doesn't match with the existing values. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"Unknown value for registration type.");
							count++;
							continue;
						}
					} else {
						LOG.info("registration type does't specified. Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Registration type does't specified");
						count++;
						continue;
					}
					
					// ActivationCode
					if (!StringUtils.isBlank(row.getCell(10)
							.getStringCellValue())) {
						if (row.getCell(10).getStringCellValue()
								.equalsIgnoreCase("yes")){
							productBean.setActivationCode(true);
							//product de-duplication
							//epd.setRegistrationDefinitionType(RegistrationDefinition.RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString());
						}
						else if (row.getCell(10).getStringCellValue()
								.equalsIgnoreCase("no")){
							productBean.setActivationCode(false);
							//product de-duplication
							//epd.setRegistrationDefinitionType(RegistrationDefinition.RegistrationDefinitionType.PRODUCT_REGISTRATION.toString());
						}
						else {
							LOG.info("Activation Code specified doesn't match with the existing values. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"Unkown value for activation code field.");
							count++;
							continue;
						}
						
					} else {
						LOG.info("Activation Code does't specified. Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Activation Code field does't specified");
						count++;
						continue;
					}
					
					// HomePage
					if (!StringUtils.isBlank(row.getCell(4)
							.getStringCellValue())) {
						productBean.setHomePage(row.getCell(4)
								.getStringCellValue());
						//product de-duplication
						/*epd.setHomePage(row.getCell(4)
								.getStringCellValue());*/
					}

					// Landing Page
					if (!StringUtils.isBlank(row.getCell(5)
							.getStringCellValue())) {
						productBean.setLandingPage(row.getCell(5)
								.getStringCellValue());
						//product de-duplication
						/*epd.setLandingPage(row.getCell(5)
								.getStringCellValue());*/
					}
					
					// SLA
					if (!StringUtils.isBlank(row.getCell(7)
							.getStringCellValue())) {
						productBean.setSla(row.getCell(7).getStringCellValue());
						//product de-duplication
						/*epd.setSla(row.getCell(7).getStringCellValue());*/
					}
					
					

					

					

					

					// PageAccount
					if (!StringUtils.isBlank(row.getCell(8)
							.getStringCellValue())) {
						List<AccountPageDefinition> apdList = dpb
								.getAccountPageDefinitions();
						
						for (AccountPageDefinition accountPageDefinition : apdList) {
							if (accountPageDefinition
									.getName()
									.equalsIgnoreCase(
											row.getCell(8).getStringCellValue())) {
								productBean
										.setAccountPageDefinitionId(accountPageDefinition
												.getId());
								break;
							}
						}
						if (productBean.getAccountPageDefinitionId() == null) {
							LOG.info("Page account specified doesn't match with the existing pages. Setting it 'None Selected'.");
						}
					} else {
						LOG.info("Page account does't specified. Setting it 'None Selected'.");
					}

					// EmailValidation
					if (!StringUtils.isBlank(row.getCell(9)
							.getStringCellValue())) {
						if (row.getCell(9).getStringCellValue()
								.equalsIgnoreCase("yes"))
							productBean.setEmailValidation(true);
						else if (row.getCell(9).getStringCellValue()
								.equalsIgnoreCase("no"))
							productBean.setEmailValidation(false);
						else {
							LOG.info("Email validation specified doesn't match with the existing values. Setting it 'no'.");
							productBean.setEmailValidation(false);
						}
					} else {
						LOG.info("Email validation does't specified. Setting it 'no'.");
						productBean.setEmailValidation(false);
					}

					

					// Page_Product
					if (!StringUtils.isBlank(row.getCell(11)
							.getStringCellValue())) {
						List<ProductPageDefinition> ppdList = dpb
								.getProductPageDefinitions();
						
						for (ProductPageDefinition productPageDefinition : ppdList) {
							if (productPageDefinition.getName()
									.equalsIgnoreCase(
											row.getCell(11)
													.getStringCellValue())) {
								productBean
										.setProductPageDefinitionId(productPageDefinition
												.getId());
								break;
							}
						}
						if (productBean.getProductPageDefinitionId() == null) {
							LOG.info("Page page specified doesn't match with the existing pages. Setting it 'None Selected'.");
						}
					} else {
						LOG.info("Page page does't specified. Setting it 'None Selected'.");
					}

					// Activation
					if (!StringUtils.isBlank(row.getCell(12)
							.getStringCellValue())) {
						List<RegistrationActivation> ras = dpb
								.getRegistrationActivations();
						// registrationActivationService.getAllRegistrationActivationsOrderedByType();
						productBean.setRegistrationActivations(ras);
						for (RegistrationActivation registrationActivation : ras) {
							if (registrationActivation instanceof ValidatedRegistrationActivation) {
								String email = ((ValidatedRegistrationActivation) registrationActivation)
										.getValidatorEmail();
								if (row.getCell(12).getStringCellValue()
										.toLowerCase().contains("validated")
										&& row.getCell(13).getStringCellValue()
												.equalsIgnoreCase(email)) {
									productBean
											.setRegistrationActivationId(registrationActivation
													.getId());
									break;
								} else if (row.getCell(12).getStringCellValue()
										.toLowerCase().contains("validated")
										&& !row.getCell(13)
												.getStringCellValue()
												.equalsIgnoreCase(email)) {
									productBean
											.setRegistrationActivationId(ProductBean.NEW_VALIDATOR_ACTIVATION);
									break;
								}
							} else if (registrationActivation instanceof SelfRegistrationActivation) {
								if (row.getCell(12).getStringCellValue()
										.toLowerCase().contains("self")) {
									productBean
											.setRegistrationActivationId(registrationActivation
													.getId());
									break;
								}
							} else if (registrationActivation instanceof InstantRegistrationActivation) {
								if (row.getCell(12).getStringCellValue()
										.toLowerCase().contains("instant")) {
									productBean
											.setRegistrationActivationId(registrationActivation
													.getId());
									break;
								}
							}

						}

						if (productBean.getRegistrationActivationId() == null) {
							LOG.info("Registration activation does't match with existing values. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"Unknown value for registration activation type.");
							count++;
							continue;
						}
					} else {
						LOG.info("Registration activation does't specified. Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Registration activation type does't specified");
						count++;
						continue;
					}

					// Validator
					if (!StringUtils.isBlank(row.getCell(13)
							.getStringCellValue())) {
						productBean.setValidator(row.getCell(13)
								.getStringCellValue());
					}

					
				

					// Send User Confirmation Email-Email confirmation enabled
					if (!StringUtils.isBlank(row.getCell(23)
							.getStringCellValue())) {
						if (row.getCell(23).getStringCellValue()
								.equalsIgnoreCase("yes"))
							productBean.setEmailConfirmationEnabled(true);
						else if (row.getCell(23).getStringCellValue()
								.equalsIgnoreCase("no"))
							productBean.setEmailConfirmationEnabled(false);
						else {
							LOG.info("Send User Confirmation Email specified doesn't match with the existing values. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"Unknown value for Send User Confirmation mail. ");
							count++;
							continue;
						}
					} else {
						LOG.info("Send User Confirmation Email does't specified. Skipping product creation for "
								+ productBean.getProductName());
						appendIntoProductFailureBuffer(trimmedRow0,
								"Send User Confirmation Email does't specified");
						count++;
						continue;
					}
					
					//external -product
					if (!StringUtils.isBlank(row.getCell(24)
							.getStringCellValue())
							&& !StringUtils.isBlank(row.getCell(25)
									.getStringCellValue())
							&& !StringUtils.isBlank(row.getCell(26)
									.getStringCellValue())) {
						boolean extInUse = checkExtIdinUse(
								productBean.getProductId(), row.getCell(24)
										.getStringCellValue(), row.getCell(25)
										.getStringCellValue(), row.getCell(26)
										.getStringCellValue());
						if (extInUse) {
							LOG.info("External Id in use. Skipping product creation for "
									+ productBean.getProductName());
							appendIntoProductFailureBuffer(trimmedRow0,
									"External ID in use.");
							count++;
							continue;
						} else {
							String extIdToAdd = externalIdmap(row.getCell(24)
									.getStringCellValue(), row.getCell(25)
									.getStringCellValue(), row.getCell(26)
									.getStringCellValue(), trimmedRow0);
							if (!extIdToAdd.equalsIgnoreCase("exception")) {
								productBean.setExternalIdsToAdd(extIdToAdd);
								
							} else {
								LOG.info("Error in external id set.");
								count++;
								continue;
							}
						}
					} else if(!(StringUtils.isBlank(row.getCell(24)
							.getStringCellValue())
							&& StringUtils.isBlank(row.getCell(25)
									.getStringCellValue())
							&& StringUtils.isBlank(row.getCell(26)
									.getStringCellValue()))){
						appendIntoProductFailureBuffer(trimmedRow0,
								"Unknown value for external system type.");
						count++;
						continue;
					}
					
					try {
						LicenceTemplate licenceTemplate = createLicenceTemplate(productBean);
						updateLicenceTemplate(productBean, licenceTemplate);
						LicenceDetailDto licenceDetailDto = ErightsObjectFactory.getLicenceTemplateDto(licenceTemplate);
						// to set the state of product
						if (productBean.getState() != null){}
							//epd.setState(productBean.getState().toString());
						EnforceableProductDto epd = productBean.getEnforceableProduct() ;
						
						EnforceableProductDto createdProduct = erightsFacade
								.createProduct(epd,licenceDetailDto);
						productBean.setProductId(createdProduct.getProductId());
					} catch (ErightsException e) {
						LOG.info("Internal Server Error while creating product");
						appendIntoProductFailureBuffer(
								productBean.getProductName(),
								e.getMessage());
						count++;
						continue;
					} 
					try {
						productBean.setAccountPageDefinitions(dpb
								.getAccountPageDefinitions());
						productBean.setProductPageDefinitions(dpb
								.getProductPageDefinitions());
						productBean.setRegistrationActivations(dpb
								.getRegistrationActivations());
						productBean.setExternalSystemMap(dpb
								.getExternalSystemMap());
						processProductBean(productBean);
						appendIntoProductCreateSuccessBuffer(trimmedRow0,
								productBean.getProductId());
						count++;
					} catch (ServiceLayerException e) {
						LOG.info("service layer exception for ." + trimmedRow0);
						appendIntoProductFailureBuffer(trimmedRow0,
								"Internal server Error");
						count++;
						continue;
					} catch (UnsupportedEncodingException e) {
						LOG.info("Unsupported Encoding Exception for "
								+ trimmedRow0);
						appendIntoProductFailureBuffer(trimmedRow0,
								"Internal server Error");
						count++;
						continue;
					} catch (CircularCountryMatchException e) {
						LOG.info("Country match exception for " + trimmedRow0);
						appendIntoProductFailureBuffer(trimmedRow0,
								"Internal server Error");
						count++;
						continue;
					}
				}
				
			 }
		 }
		 LOG.info("Reading product file done.");
		 productCreateSuccessBuffer.append('\n'+"---------------"+'\n'+"Total : "+productCreateCount+'\n');
		 productUpdateSuccessBuffer.append('\n'+"---------------"+'\n'+"Total : "+productUpdateCount+'\n');
		 productFailureBuffer.append('\n'+"---------------"+'\n'+"Total : "+productFailureCount+'\n');
		 writeToFile(productCreateSuccessBuffer.toString(), productUpdateSuccessBuffer.toString(), productFailureBuffer.toString(), productOutputFileName);
	 }
	 
	 private void createLinkedProductsInBatch(HSSFSheet productSheet, AdminUser adminUser) throws ErightsException{
		 int count = 0;
		 final int col=2;
		 
		 Iterator<Row> rowIterator = productSheet.iterator();
		 while(rowIterator.hasNext()){
			 Row row=rowIterator.next();
			 DataFormatter df = new DataFormatter();
			
			 for (int i = 0; i <= col; i++) {
				 if(row.getCell(i)==null){
					 row.createCell(i).setCellValue("");
				 }
				 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
					 if(row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC){
						 if (DateUtil.isCellDateFormatted(row.getCell(i))) {
							 String tmpDte= "";
							 tmpDte = df.formatCellValue(row.getCell(i));
							 System.out.println("date tmp : "+ tmpDte);
							 row.getCell(i).setCellValue(tmpDte);
						 }
					 }
					 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
				 }
			 }
			 if(count ==0){
				 LOG.info("this is header.");
				 count++;
			 }else{
				 LOG.info("Reading line no. : "+count);
				 
				 String trimmedRow0 = row.getCell(0).getStringCellValue().trim();
				 String trimmedRow1 = row.getCell(1).getStringCellValue().trim();
				 String trimmedRow2 = row.getCell(2).getStringCellValue().trim();
				
				 EnforceableProductDto chkProduct=null;
				 
				 if(!StringUtils.isBlank(trimmedRow0)){
					 try {
						 chkProduct = productService.getProductByName(trimmedRow0);
					 } catch (ErightsException e){
						 LOG.info(e.getMessage() + " for Linked product");
					 }
				 }else{
					 LOG.info("product name doesn't specified. Skipped linked product creation.");
					 appendIntoLinkedProductFailureBuffer("At line no. "+(count+1) , "product name doesn't specified.");
					 count++;
					 continue;
				 }
				 
				 EnforceableProductDto chkLinkedProduct=null;
				 if(!StringUtils.isBlank(trimmedRow1)){
					 try {
						 chkLinkedProduct = productService.getProductByName(trimmedRow1);
					 } catch (ErightsException e){
						 LOG.info(e.getMessage() + " for Linked product");
					 }
				 }else{
					 LOG.info("Linked product name doesn't specified. Skipped linked product creation.");
					 appendIntoLinkedProductFailureBuffer("At line no. "+(count+1) , "linked Product name doesn't specified.");
					 count++;
					 continue;
				 }
				 
				 if (chkProduct != null && chkLinkedProduct!=null) {
					 
				  try {
					//product de-duplication
						 /*RegisterableProduct registerableProduct = (RegisterableProduct) productService.getProductById(chkProduct.getEacId());
						 EnforceableProductDto enforceableProductDto = chkProduct;
						 AccountRegistrationDefinition accountRegistrationDefinition = registrationDefinitionService.getAccountRegistrationDefinitionByProduct(registerableProduct);
						 ProductRegistrationDefinition productRegistrationDefinition = registrationDefinitionService.getProductRegistrationDefinitionByProduct(registerableProduct);
						 List<RegistrationDefinition> regDefs = getRegistrationDefinitionsForProduct(registerableProduct);
						 List<LinkedProductNew> linked = enforceableProductDto.getLinkedProducts();
						 
						 
						 ProductBean productBean = new ProductBean(registerableProduct, accountRegistrationDefinition, productRegistrationDefinition,enforceableProductDto,regDefs,linked);*/
						 String linkedProductId= getLinkedProductFromProduct(chkLinkedProduct.getProductId(),chkProduct);
						 
						 String linkedProductProductId=chkLinkedProduct.getProductId();
						 String buildLinkedProducts=linkedProductProductId+","+trimmedRow2;
						 if(!StringUtils.isBlank(linkedProductId))
						 {
							 //update
							 //productBean.setLinkedProductsToUpdate(linkedProductId+","+buildLinkedProducts);
							 //add call if linked product update W/S create
						 }else{
							 //insert
							
							 productService.addLinkedProductToRightsuit(chkLinkedProduct.getProductId(), chkProduct.getProductId());
							 
						 }
						//product de-duplication
						 /*ProductUpdateProcess updateProcess = new ProductUpdateProcess(productService);
	
						 updateProcess.process(productBean);
						 
						 productService.updateLinkedProducts(productBean,registerableProduct );
						 Product product = updateProcess.getUpdatedProduct();
						 //product de-duplication
						 //registerableProduct=updateProcess.getRegistrableProduct();
						 productRegistrationDefinition = updateProcess.getUpdatedProductRegistrationDefinition();
						 //product de-duplication
						 //productService.updateRegisterableProduct(registerableProduct,productRegistrationDefinition, null);
						 productService.updateProduct(product); // Saves child objects
*/						
						 
						 if(!StringUtils.isBlank(linkedProductId))
						 {
							 appendIntoLinkedProductUpdateSuccessBuffer(trimmedRow0, trimmedRow1);
						 }else{ 
							 	appendIntoLinkedProductCreateSuccessBuffer(trimmedRow0, trimmedRow1);
							 }
							count++;
							continue;
						}  
				//product de-duplication
				  /*catch (ServiceLayerException e) {
							LOG.info("service layer exception for ."+trimmedRow0);
							appendIntoProductFailureBuffer(trimmedRow0, "Internal server Error");
							count++;
							continue;
						} catch (UnsupportedEncodingException e) {
							LOG.info("Unsupported Encoding Exception for "+trimmedRow0);
							appendIntoProductFailureBuffer(trimmedRow0, "Internal server Error");
							count++;
							continue;
						} */catch (ErightsException e) {
							LOG.info("Erihghts exception for updating products "+trimmedRow0);
							appendIntoProductFailureBuffer(trimmedRow0, "Internal server Error");
							count++;
							continue;
						} 
					 
				 }else{
					 LOG.info("product or linked product specified does not exist. Skipped linked product creation.");
					 appendIntoLinkedProductFailureBuffer("At line no. "+(count+1) , "product or linked product specified does not exist");
					 count++;
					 continue;
				 }
				 
			 }
		 }
		 LOG.info("Reading linked product sheet end.");
		 linkedProductCreateSuccessBuffer.append('\n'+"---------------"+'\n'+"Total : "+linkedProductCreateCount+'\n');
		 linkedProductUpdateSuccessBuffer.append('\n'+"---------------"+'\n'+"Total : "+linkedProductUpdateCount+'\n');
		 linkedProductFailureBuffer.append('\n'+"---------------"+'\n'+"Total : "+linkedProductFailureCount+'\n');
		 writeToFile(linkedProductCreateSuccessBuffer.toString(), linkedProductUpdateSuccessBuffer.toString(), linkedProductFailureBuffer.toString(), linkedProductOutputFileName);
	 }
	 
	 private void addPlatfromProductsInBatch(HSSFSheet productSheet, AdminUser adminUser) throws ErightsException{
		 int count = 0;
		 final int col=2;
		 List<Platform> platformList = new ArrayList<Platform>() ;
		 platformList = platformService.getAllPlatforms() ;
		 Iterator<Row> rowIterator = productSheet.iterator();
		 while(rowIterator.hasNext()){
			 Row row=rowIterator.next();
			 DataFormatter df = new DataFormatter();
			
			 for (int i = 0; i <= col; i++) {
				 if(row.getCell(i)==null){
					 row.createCell(i).setCellValue("");
				 }
				 if(row.getCell(i).getCellType() != Cell.CELL_TYPE_STRING){
					 if(row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC){
						 if (DateUtil.isCellDateFormatted(row.getCell(i))) {
							 String tmpDte= "";
							 tmpDte = df.formatCellValue(row.getCell(i));
							 System.out.println("date tmp : "+ tmpDte);
							 row.getCell(i).setCellValue(tmpDte);
						 }
					 }
					 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
				 }
			 }
			 if(count ==0){
				 LOG.info("this is header.");
				 count++;
			 }else{
				 LOG.info("Reading line no. : "+count);
				 
				 String trimmedRow0 = row.getCell(0).getStringCellValue().trim();
				 String trimmedRow1 = row.getCell(1).getStringCellValue().trim();
				 
				 EnforceableProductDto chkProduct=null;
				 
				 if(!StringUtils.isBlank(trimmedRow0)){
					 try {
						 chkProduct = productService.getProductByName(trimmedRow0);
					 } catch (ErightsException e){
						 LOG.info(e.getMessage() + " for product platfrom");
					 }
				 }else{
					 LOG.info("product name doesn't specified. Skipped add product to platfrom.");
					 appendIntoProductPlatfromFailureBuffer("At line no. "+(count+1) , "product name doesn't specified.");
					 count++;
					 continue;
				 }
				 
				 String[] platformCodeToAdd = null ;
				 List<Platform> platformListToAdd = new ArrayList<Platform>() ;
				 if(!StringUtils.isBlank(trimmedRow1)){
					 platformCodeToAdd = trimmedRow1.split(",") ;
				 }else{
					 LOG.info("Platfroms name doesn't specified. Skipped add product to platfrom.");
					 appendIntoProductPlatfromFailureBuffer("At line no. "+(count+1) , "Platfrom name doesn't specified.");
					 count++;
					 continue;
				 }
				 boolean platformCodeFound = false ;
				 if ( platformCodeToAdd != null && platformCodeToAdd.length > 0) {
					 for (String platformCode : platformCodeToAdd ) {
						 platformCodeFound = false ;
						 for (Platform platform : platformList ) {
							 if (platform.getCode().equalsIgnoreCase(platformCode)) {
								 platformListToAdd.add(platform);
								 platformCodeFound = true ;
								 break ;
							 }
						 }
						 if (!platformCodeFound) {
							 LOG.info("Platform specified does not exist with platform code "+ platformCode);
								appendIntoProductPlatfromFailureBuffer("At line no. "
										+ (count + 1),
										"Platfroms specified does not exist with platform code "+ platformCode);
							 break ;
						 }
					 }
				 }
				 //if platform code not found then skipped platform product mapping creation
				 if (!platformCodeFound) { 
					count++;
					continue; 
				 }
				if (chkProduct != null && platformListToAdd != null
						&& platformListToAdd.size() > 0 && platformCodeFound == true) {

					try {
						chkProduct.getPlatformList().addAll(platformListToAdd);
						erightsFacade.updateProduct(chkProduct);

					} catch (ErightsException e) {
						LOG.info("Erihghts exception for updating products "
								+ trimmedRow0);
						appendIntoProductPlatfromFailureBuffer(trimmedRow0,
								e.getMessage());
					}
					
					appendIntoProductPlatfromSuccessBuffer(
								trimmedRow0, trimmedRow1);
					count++;
					continue;

				} else {
					LOG.info("Product or Platform specified does not exist. Skipped add product to platfrom.");
					appendIntoProductPlatfromFailureBuffer("At line no. "
							+ (count + 1),
							"Product or Platfroms specified does not exist");
					count++;
					continue;
				}
				 
			 }
		 }
		 LOG.info("Reading Platfrom sheet end.");
		 productPlatformSuccessBuffer.append('\n'+"---------------"+'\n'+"Total : "+productPlatformSuccessCount+'\n') ;
		 productPlatformFailureBuffer.append('\n'+"---------------"+'\n'+"Total : "+productPlatformFailureCount+'\n');
		 writeToFile(productPlatformSuccessBuffer.toString(), new String(), productPlatformFailureBuffer.toString(), productPlatformOutputFileName);
	 }
	 
	private String createBulkActivationCodeBatchBatch(HSSFWorkbook workbook, AdminUser adminUser)
			throws AccessDeniedException, ErightsException,
 UnsupportedEncodingException {
		int count = 0;
		String msg = null;
		String format = "EAC Numeric";
		List<ActivationCodeBatchDto> acd = new ArrayList<ActivationCodeBatchDto>();
		HSSFSheet batchSheet = workbook.getSheetAt(0);
		HSSFSheet licenseSheet = workbook.getSheetAt(1);
		if (batchSheet.getPhysicalNumberOfRows() != licenseSheet
				.getPhysicalNumberOfRows()) {
			LOG.info("Batch sheet and license sheet should have same number of rows.");
			msg = EACSettings.getProperty(EACSettings.ERROR_SHEET_ROWNUMBER);
			return msg;
		}
		
		Iterator<Row> batchIterator = batchSheet.iterator();
		Iterator<Row> licenseIterator = licenseSheet.iterator();
		while (batchIterator.hasNext() && licenseIterator.hasNext()) {
			ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
			Row batchrow = batchIterator.next();
			Row licenserow = licenseIterator.next();
			int batchColm = 6;
			DataFormatter df = new DataFormatter();
			if (count == 0) {
				LOG.info("this is header.");
				count++;
			} else {
				LOG.info("Reading line no. : " + count);
				count++;
				// check for type of numeric data
				if (batchrow.getCell(2) != null
						&& batchrow.getCell(2).getCellType() != Cell.CELL_TYPE_NUMERIC) {
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_TOKEN)+" "+count;
					return msg;
				}
				if (batchrow.getCell(3) != null
						&& batchrow.getCell(3).getCellType() != Cell.CELL_TYPE_NUMERIC) {
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_ALLOWEDUSAGE)+" "+count;
					return msg;
				}
				//Converting all cells to String type
				for (int i = 0; i < batchColm; i++) {
					if (batchrow.getCell(i) == null) {
						batchrow.createCell(i).setCellValue("");
					}
					if (batchrow.getCell(i).getCellType() != Cell.CELL_TYPE_STRING) {
						if (batchrow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
							if (DateUtil.isCellDateFormatted(batchrow
									.getCell(i))) {
								String tmpDte = "";
								tmpDte = df
										.formatCellValue(batchrow.getCell(i));
								batchrow.getCell(i).setCellValue(tmpDte);
							}
						}
					}
					batchrow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
				}
				//batch name
				if (StringUtils.isBlank(batchrow.getCell(0)
						.getStringCellValue().trim())) {
					LOG.info("batch name is empty.");
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_BATCHNAME)+" "+count;
					
					return msg;
				} else if (batchrow.getCell(0).getStringCellValue().trim()
						.length() > 255) {
					LOG.info("Batch name should be less than 255 characters.");
					msg = EACSettings.getProperty(EACSettings.ERROR_BATCHNAME_LENGTH)+" "+count;
					return msg;
				} else {

					if (!StringUtils.isBlank(batchrow.getCell(0)
							.getStringCellValue().trim())) {
						activationCodeBatchDto.setBatchId(batchrow.getCell(0)
								.getStringCellValue());
					}
				}
				//code format
				if (batchrow.getCell(1).getStringCellValue().trim()
						.equalsIgnoreCase(format)) {
					activationCodeBatchDto
							.setCodeFormat(CodeFormatEnum.EAC_NUMERIC
									.toString());

				} else {
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_CODEFORMAT)+" "+count;
					return msg;
				}
				// No Of token
				if (!StringUtils.isBlank(batchrow.getCell(2)
						.getStringCellValue().trim())) {
					activationCodeBatchDto.setNumberOfTokens(Integer
							.valueOf(batchrow.getCell(2).getStringCellValue()
									.trim()));
				} else {
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_TOKEN)+" "+count;
					return msg;
				
				}

				// Allowed Usage
				if (!StringUtils.isBlank(batchrow.getCell(3)
						.getStringCellValue().trim())) {
					activationCodeBatchDto.setAllowedUsages(Integer
							.valueOf(batchrow.getCell(3).getStringCellValue()
									.trim()));
				} else {
					msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_ALLOWEDUSAGE)+" "+count;
					return msg;
				}

				DateTimeFormatter FORMATTER = DateTimeFormat
						.forPattern("dd/MM/yyyy");

				// Token Validity Start Date
				if (!StringUtils.isBlank(batchrow.getCell(4)
						.getStringCellValue().trim())) {
					try {
						activationCodeBatchDto.setValidFrom(FORMATTER
								.parseDateTime(
										batchrow.getCell(4)
												.getStringCellValue().trim())
								.toLocalDate());
					} catch (Exception e) {
						msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_DATE)+" "+count;
						return msg;
					}
				}
				
				// EndDate
				if (!StringUtils.isBlank(batchrow.getCell(5)
						.getStringCellValue().trim())) {
					try {
						activationCodeBatchDto.setValidTo(FORMATTER
								.parseDateTime(
										batchrow.getCell(5)
												.getStringCellValue().trim())
								.toLocalDate());
					} catch (Exception e) {
						msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_DATE)+" "+count;
						return msg;
					}
				}
				

				try {
					LicenceDetailDto licenceDetailDto = createBulkActivationCodeLicense(
							licenserow, activationCodeBatchDto, adminUser);
					if (licenceDetailDto != null) {
						activationCodeBatchDto
								.setLicenceDetailDto(licenceDetailDto);
						acd.add(activationCodeBatchDto);
						msg = "success";
					}

					else {
						msg = EACSettings.getProperty(EACSettings.ERROR_INVALID_LICENSE)+" "+count;
						return msg;
					}
				} catch (Exception msg1){
					msg = msg1.getMessage()+" " +count;
					return msg;
				}
			}
		}
		if (msg == "success") {
			this.erightsFacade.bulkCreateActivationCodeBatchRequest(acd,
					adminUser);
			msg = "success";
			return msg;
		}

		return msg;
	}
	 
	private LicenceDetailDto createBulkActivationCodeLicense(Row licenserow,
			ActivationCodeBatchDto activationCodeBatchDto, AdminUser adminUser) throws Exception,
 AccessDeniedException, ErightsException,
			UnsupportedEncodingException {
		LicenceDetailDto licenceDetailDto = null;
		DataFormatter df = new DataFormatter();
		int licenseColm = 12;
		// Converting all cells to String type
		for (int i = 0; i < licenseColm; i++) {
			if (licenserow.getCell(i) == null) {
				licenserow.createCell(i).setCellValue("");
			}
			if (licenserow.getCell(i).getCellType() != Cell.CELL_TYPE_STRING) {
				if (licenserow.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
					if (DateUtil.isCellDateFormatted(licenserow.getCell(i))) {
						String tmpDte = "";
						tmpDte = df.formatCellValue(licenserow.getCell(i));
						System.out.println("date tmp : " + tmpDte);
						licenserow.getCell(i).setCellValue(tmpDte);
					}
				}
				licenserow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			}
		}
		// product or product group name
		if (licenserow.getCell(0).getStringCellValue().trim()
				.equalsIgnoreCase(activationCodeBatchDto.getBatchId())) {
			if (StringUtils.isBlank(licenserow.getCell(1).getStringCellValue()
					.trim())
					&& StringUtils.isBlank(licenserow.getCell(2)
							.getStringCellValue().trim())) {
				throw new Exception(
						EACSettings
								.getProperty(EACSettings.ERROR_PRODUCTORGROUPNAME));
			} else if (!StringUtils.isBlank(licenserow.getCell(1)
					.getStringCellValue().trim())
					&& !StringUtils.isBlank(licenserow.getCell(2)
							.getStringCellValue().trim())) {
				throw new Exception(
						EACSettings
								.getProperty(EACSettings.ERROR_PRODUCTORGROUPNAME));
			} else if (!StringUtils.isBlank(licenserow.getCell(1)
					.getStringCellValue().trim())) {
				activationCodeBatchDto.setProductId(licenserow.getCell(1)
						.getStringCellValue().trim());
			} else if (!StringUtils.isBlank(licenserow.getCell(2)
					.getStringCellValue().trim())) {
				activationCodeBatchDto.setProductGroupId(licenserow.getCell(2)
						.getStringCellValue().trim());
			}
			// License
			if (licenserow.getCell(3).getStringCellValue().trim()
					.equalsIgnoreCase("rolling")) {
				RollingLicenceTemplate obj = new RollingLicenceTemplate();
				// begin on
				if (!StringUtils.isBlank(licenserow.getCell(10)
						.getStringCellValue().trim())) {
					if (licenserow.getCell(10).getStringCellValue().trim()
							.equalsIgnoreCase("creation"))
						obj.setBeginOn(RollingBeginOn.CREATION);
					else if (licenserow.getCell(10).getStringCellValue().trim()
							.toLowerCase().contains("first"))
						obj.setBeginOn(RollingBeginOn.FIRST_USE);
					else {
						throw new Exception(
								EACSettings
										.getProperty(EACSettings.ERROR_INVALID_ROLLINGLICENSE));
					}
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_ROLLINGLICENSE));
				}
				// time period
				if (!StringUtils.isBlank(licenserow.getCell(8)
						.getStringCellValue().trim())) {
					obj.setTimePeriod(Integer.valueOf(licenserow.getCell(8)
							.getStringCellValue().trim()));
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_ROLLINGLICENSE));
				}
				// Unit Type
				if (!StringUtils.isBlank(licenserow.getCell(9)
						.getStringCellValue().trim())) {
					if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("day"))
						obj.setUnitType(RollingUnitType.DAY);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("hour"))
						obj.setUnitType(RollingUnitType.HOUR);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("millisecond"))
						obj.setUnitType(RollingUnitType.MILLISECOND);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("minute"))
						obj.setUnitType(RollingUnitType.MINUTE);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("month"))
						obj.setUnitType(RollingUnitType.MONTH);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("second"))
						obj.setUnitType(RollingUnitType.SECOND);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("week"))
						obj.setUnitType(RollingUnitType.WEEK);
					else if (licenserow.getCell(9).getStringCellValue().trim()
							.equalsIgnoreCase("year"))
						obj.setUnitType(RollingUnitType.YEAR);
					else {
						throw new Exception(
								EACSettings
										.getProperty(EACSettings.ERROR_INVALID_ROLLINGLICENSE));
					}
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_ROLLINGLICENSE));
				}
				RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(
						obj.getBeginOn(), obj.getUnitType(),
						obj.getTimePeriod(), null);
				licenceDetailDto = dtoObj;
			} else if (licenserow.getCell(3).getStringCellValue().trim()
					.equalsIgnoreCase("concurrent")) {
				ConcurrentLicenceTemplate obj = new ConcurrentLicenceTemplate();
				if (!StringUtils.isBlank(licenserow.getCell(6)
						.getStringCellValue().trim())) {
					try {
						obj.setTotalConcurrency(Integer.valueOf(licenserow
								.getCell(6).getStringCellValue()));
					} catch (Exception e) {
						throw new Exception(
								EACSettings
										.getProperty(EACSettings.ERROR_INVALID_CONCURRENTLICENSE));
					}
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_CONCURRENTLICENSE));
				}

				if (!StringUtils.isBlank(licenserow.getCell(7)
						.getStringCellValue().trim())) {
					try {
						obj.setUserConcurrency(Integer.valueOf(licenserow
								.getCell(7).getStringCellValue()));
					} catch (Exception e) {
						throw new Exception(
								EACSettings
										.getProperty(EACSettings.ERROR_INVALID_CONCURRENTLICENSE));
					}
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_CONCURRENTLICENSE));
				}
				StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(
						obj.getTotalConcurrency(), obj.getUserConcurrency());
				licenceDetailDto = dtoObj;
			} else if (licenserow.getCell(3).getStringCellValue().trim()
					.equalsIgnoreCase("usage")) {
				UsageLicenceTemplate obj = new UsageLicenceTemplate();
				if (!StringUtils.isBlank(licenserow.getCell(11)
						.getStringCellValue().trim())) {
					try {
						obj.setAllowedUsages(Integer.valueOf(licenserow
								.getCell(11).getStringCellValue().trim()));
					} catch (Exception e) {
						throw new Exception(
								EACSettings
										.getProperty(EACSettings.ERROR_INVALID_USAGELICENSE));
					}
				} else {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_USAGELICENSE));
				}
				UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
				dtoObj.setAllowedUsages(obj.getAllowedUsages());
				licenceDetailDto = dtoObj;
			} else {
				throw new Exception(
						EACSettings
								.getProperty(EACSettings.ERROR_INVALID_LICENSE));
			}
			DateTimeFormatter FORMATTER = DateTimeFormat
					.forPattern("dd/MM/yyyy");
			// StartDate
			if (!StringUtils.isBlank(licenserow.getCell(4).getStringCellValue()
					.trim())) {
				try {
					licenceDetailDto.setStartDate(FORMATTER.parseDateTime(
							licenserow.getCell(4).getStringCellValue().trim())
							.toLocalDate());
				} catch (Exception e) {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_DATE));
				}
			} 
			// EndDate
			if (!StringUtils.isBlank(licenserow.getCell(5).getStringCellValue()
					.trim())) {
				try {
					licenceDetailDto.setEndDate(FORMATTER.parseDateTime(
							licenserow.getCell(5).getStringCellValue().trim())
							.toLocalDate());
				} catch (Exception e) {
					throw new Exception(
							EACSettings
									.getProperty(EACSettings.ERROR_INVALID_DATE));
				}
			}

		} else {
			throw new Exception(
					EACSettings.getProperty(EACSettings.ERROR_SAME_BATCHNAME));
		}
		return licenceDetailDto;
	}
			
	 
	 private void setModelPrerequisites(final ProductBean productBean, AdminUser adminUser) throws AccessDeniedException, ErightsException {
			productBean.setRegistrationActivations(registrationActivationService.getAllRegistrationActivationsOrderedByType());
			productBean.setAccountPageDefinitions(pageDefinitionService.getAvailableAccountPageDefinitions(adminUser));
			productBean.setProductPageDefinitions(pageDefinitionService.getAvailableProductPageDefinitions(adminUser));
			
			List<ExternalSystem> externalSystems = null;
			try {
				externalSystems = externalIdService.getAllExternalSystemsOrderedByName();
			} catch (ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (ExternalSystem externalSystem : externalSystems) {
				List<ExternalSystemIdType> externalSystemIdTypes = externalIdService.getExternalSystemIdTypesOrderedByName(externalSystem);
				externalSystem.setExternalSystemIdTypes(new HashSet<ExternalSystemIdType>(externalSystemIdTypes));
				productBean.addToExternalSystemMap(externalSystem, externalSystemIdTypes);
			}
			allExternalSystems = externalSystems;
	 }
	
	 private void processProductBean(final ProductBean productBean) throws CircularCountryMatchException, UnsupportedEncodingException, ServiceLayerException  {
		 //add to remove compilation error as part of product de-duplication
		 ProductUpdateProcess updateProcess = new ProductUpdateProcess(productService); ; //new ProductUpdateProcess(externalIdService,productService, divisionService);
			try{
				updateProcess.process(productBean);
				ProductRegistrationDefinition productRegistrationDefinition = updateProcess.getUpdatedProductRegistrationDefinition();
				AccountRegistrationDefinition accountRegistrationDefinition = updateProcess.getUpdatedAccountRegistrationDefinition();
				//Product product = updateProcess.getUpdatedProduct();
				//productService.saveProductWithExistingGuid(product); // Saves the product definition
				//productService.updateProduct(product); // Saves child objects
				//product de-duplication
				//registrationActivationService.saveOrUpdateRegistrationActivation(productRegistrationDefinition.getRegistrationActivation());
				//update data in Atypon
				//Product registerableProductNew =  updateProcess.getProduct();
				//productService.updateRegisterableProduct(registerableProductNew);	
				//product de-duplication
				//productService.updateProduct(registerableProductNew, productRegistrationDefinition, null);
				accountRegistrationDefinition.setProductId(productBean.getProductId());
				productRegistrationDefinition.setProductId(productBean.getProductId());
				registrationDefinitionService.saveRegistrationDefinition(accountRegistrationDefinition);
				registrationDefinitionService.saveRegistrationDefinition(productRegistrationDefinition);
				//product de-duplication
				//licenceTemplateService.saveOrUpdateLicenceTemplate(productRegistrationDefinition.getLicenceTemplate());
				LOG.info("Product created successfully. Product name : "+productBean.getProductName()+" Product ID : "+productBean.getProductId());
			} catch(Exception e){
				appendIntoProductFailureBuffer(productBean.getProductName(), "Internal server Error");
			}	
	 }
	 
	 private void updateproduct(EnforceableProductDto product, String row24, String  row25,String row26) throws ServiceLayerException, ErightsException, UnsupportedEncodingException{
		 Set<ExternalProductId> externalIdsToAdd = new HashSet<ExternalProductId>();
		 ExternalProductId externalProductId = new ExternalProductId();
		 externalProductId.setExternalId(row24.trim());
		//product de-duplication
//		 externalProductId.setProduct(product);
		boolean processFlag = true;
		boolean extFlag = false;
		for (ExternalSystem externalSystem : allExternalSystems) {
			if(externalSystem.getName().equalsIgnoreCase(row25)){
				List<ExternalSystemIdType> esitList = externalIdService.getExternalSystemIdTypesOrderedByName(externalSystem);
					for (ExternalSystemIdType externalSystemIdType : esitList) {
						if(externalSystemIdType.getName().equalsIgnoreCase(row26)){
							externalSystemIdType.setExternalSystem(externalSystem);
							externalProductId.setExternalSystemIdType(externalSystemIdType);
							extFlag = true;
						}
					}
					if(extFlag == true){
						break;
					}
			}
		}
		
		if(StringUtils.isBlank(externalProductId.getExternalId())){
			processFlag = false;
			appendIntoProductFailureBuffer(product.getName(), "External Id doesn't specified.");
		}
		if(processFlag && externalProductId.getExternalSystemIdType()== null){
			processFlag = false;
			appendIntoProductFailureBuffer(product.getName(), "Unknown value for external system type.");
		}
		if(processFlag && externalProductId.getExternalSystemIdType().getExternalSystem()== null){
			processFlag = false;
			appendIntoProductFailureBuffer(product.getName(), "Unknown value for external system.");
		}
		 if(processFlag){
			 String externalId=externalProductId.getExternalId();
			 String externalSystemType=externalProductId.getExternalSystemIdType().getName();
			 String externalSystem=externalProductId.getExternalSystemIdType().getExternalSystem().getName();
			boolean	extInUse = checkExtIdinUse(product.getProductId(),externalId,externalSystemType, externalSystem );
			boolean extIdTypeinUse = checkExtIdTypeinUse(product, externalProductId);
			
			if(extInUse == false && extIdTypeinUse == false){
				 externalIdsToAdd.add(externalProductId);
				 product.getExternalIds().addAll(externalIdsToAdd);
				 productService.updateProduct(product,null);
				 LOG.info("product "+product.getName()+" and ID "+product.getProductId() +" is updated successfully");
				 appendIntoProductUpdateSuccessBuffer(product.getName(), product.getProductId());
			}else if(extInUse){
				appendIntoProductFailureBuffer(product.getName(), "External ID already in use.");
			}else if(extIdTypeinUse){
				appendIntoProductFailureBuffer(product.getName(), "Duplicate external id set");
			} 
		 }
		 
	 }
 
	 private String externalIdmap(String row24, String row25, String row26, String productName) throws ErightsException{
		 String extIdToAdd="";
		    String e1=row24;
			String e2="";
			String e3="";
			
			boolean extFlag = false;
			for (ExternalSystem externalSystem : allExternalSystems) {
				if(externalSystem.getName().equalsIgnoreCase(row25)){
					e2=row25;
					List<ExternalSystemIdType> esitList = externalIdService.getExternalSystemIdTypesOrderedByName(externalSystem);
					for (ExternalSystemIdType externalSystemIdType : esitList) {
						if(externalSystemIdType.getName().equalsIgnoreCase(row26)){
							e3=row26;
							
							extFlag = true;
							break;
						}
					}
					if(extFlag == true){
						break;
					}
				}
			}
			if(StringUtils.isBlank(e2)){
				extIdToAdd = "exception";
				appendIntoProductFailureBuffer(productName, "Unknown value for external system type.");
			}else if(StringUtils.isBlank(e3)){
				extIdToAdd = "exception";
				appendIntoProductFailureBuffer(productName, "Unknown value for external system type.");
			}else if(StringUtils.isBlank(e1)){
				extIdToAdd = "exception";
				appendIntoProductFailureBuffer(productName, "Unknown value for external system type.");
			}else{
				extIdToAdd = e1+","+e2+","+e3+"|";
			}
			
		 return extIdToAdd;
	 }
	 
	 private boolean checkExtIdinUse(String productId,String externalId, String externalSystemType, String externalSystem ){
		 ExternalIdDto extDto=new ExternalIdDto(externalSystem, externalSystemType, externalId);
		 return externalIdService.externalProductIdInUse(productId, extDto);
	 }
	 
	 private boolean checkExtIdTypeinUse(EnforceableProductDto product, ExternalProductId externalProductId){
		 String externalSystemType=externalProductId.getExternalSystemIdType().getName();
		 String externalSystem=externalProductId.getExternalSystemIdType().getExternalSystem().getName();
		 boolean stopFlag = false;
		 
		 List<ExternalProductId> extList = product.getExternalIds();
		 if(!extList.isEmpty()){
			 for (ExternalProductId externalProductId2 : extList) {
				if(externalProductId2.getExternalSystemIdType().getExternalSystem().getName().equalsIgnoreCase(externalSystem) && externalProductId2.getExternalSystemIdType().getName().equalsIgnoreCase(externalSystemType)){
					stopFlag=true;
					LOG.info("External system and external system id type already exist in this product. Skipped updating product :"+ product.getName());
					break;
				}
			}
		 } 
		 return stopFlag;
	 }
	 
	 private void sendMail(AdminUser adminUser){
		 try {
			MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(new InternetAddress(adminUser.getEmailAddress()));
            mailCriteria.setSubject(EXPORT_EMAIL_SUBJECT);
            mailCriteria.setText(getEmailText());            
            mailCriteria.setAttachmentName(zipFileName +".zip");
       
            File tempDir = new File(tempFileLocation);
            if (!tempDir.exists()) tempDir.mkdirs();
            
            zipIt(zipFileName,productOutputFileName, linkedProductOutputFileName, productPlatformOutputFileName); 
            File zipOutputfile = new File(tempFileLocation + zipFileName +".zip");
            FileInputStream fis = new FileInputStream(zipOutputfile);
            byte[] zipData =  IOUtils.toByteArray(fis);
            
            mailCriteria.setAttachment(zipData);
            emailService.sendMail(mailCriteria);
            
            fis.close();
            
           if(zipOutputfile.exists()){
                boolean delFile = zipOutputfile.delete();
                LOG.info(zipOutputfile.getName() + " is deleted... Flag : " + delFile);                
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
	 }
	 
	 private void zipIt(String zipFile, String productFile, String linkedProductFile, String platformProductFile){
		 FileOutputStream fos = null;
		 ZipOutputStream zos = null;
	        try{
	        	String[] srcFiles = { tempFileLocation+ productFile+outputFileSuffix, tempFileLocation+ linkedProductFile+outputFileSuffix, tempFileLocation+ platformProductFile+outputFileSuffix};
	        	byte[] buffer = new byte[1024];
	        	fos = new FileOutputStream(tempFileLocation+zipFile+".zip");
				zos = new ZipOutputStream(fos);
				
				for (int i=0; i < srcFiles.length; i++) {
					File srcFile = new File(srcFiles[i]);
					if(srcFile.isFile())
					{	
					FileInputStream fis = new FileInputStream(srcFile);
					// begin writing a new ZIP entry, positions the stream to the start of the entry data
					zos.putNextEntry(new ZipEntry(srcFile.getName()));
					
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
					fis.close();
				 }
					zos.closeEntry();
					
				}
				zos.close();
			  
	        }catch(IOException ex){
	            ex.printStackTrace();
	            LOG.error(ex.getMessage(), ex);
	        }
	    }
	 
	 private String getEmailText() {
		 final Map<String, Object> templateParams = new HashMap<String, Object>();
		 templateParams.put("resource", getResource(Locale.getDefault()));
        return VelocityUtils.mergeTemplateIntoString(velocityEngine, EMAIL_TEMPLATE, templateParams);
	    }
	 
	 private MessageTextSource getResource(final Locale locale) {
	        MessageTextSource result = new MessageTextSource(this.messageSource, locale);
	        return result;
	    }
	 
	 private void appendIntoProductCreateSuccessBuffer(String productName, String id){
		 productCreateSuccessBuffer.append('\n');
		 productCreateSuccessBuffer.append("Product name : "+productName+pipeSep+"Product ID : "+id);
		 productCreateCount++;
	 }
	 
	 private void appendIntoProductUpdateSuccessBuffer(String productName, String id){
		 productUpdateSuccessBuffer.append('\n');
		 productUpdateSuccessBuffer.append("Product name : "+productName+pipeSep+"Product ID : "+id);
		 productUpdateCount++;
	 }
	 
	 private void appendIntoProductFailureBuffer(String productName, String reason){
		 productFailureBuffer.append('\n');
		 productFailureBuffer.append("Product name : "+productName+pipeSep+"Reason : "+reason);
		 productFailureCount++;
	 }
	 private void appendIntoActivationCodeBuffer(String batchId, String reason){
		 batchFailureBuffer.append('\n');
		 batchFailureBuffer.append("batch ID : "+batchId+pipeSep+"Reason : "+reason);
		 batchFailureCount++;
	 }
	 private void appendIntoLicenseCodeBuffer(String reason){
		 batchFailureBuffer.append('\n');
		 batchFailureBuffer.append("Reason : "+reason);
		 batchFailureCount++;
	 }
	 private void appendIntoLinkedProductCreateSuccessBuffer(String productName, String id){
		 linkedProductCreateSuccessBuffer.append('\n');
		 linkedProductCreateSuccessBuffer.append("Product name to Link: "+productName+pipeSep+"Linked Product Name : "+id);
		 linkedProductCreateCount++;
	 }
	 
	 private void appendIntoLinkedProductUpdateSuccessBuffer(String productName, String id){
		 linkedProductUpdateSuccessBuffer.append('\n');
		 linkedProductUpdateSuccessBuffer.append("Product name to Link : "+productName+pipeSep+"Linked Product Name : "+id);
		 linkedProductUpdateCount++;
	 }
	 
	 private void appendIntoLinkedProductFailureBuffer(String productName, String reason){
		 linkedProductFailureBuffer.append('\n');
		 linkedProductFailureBuffer.append("Product name to Link: "+productName+pipeSep+"Reason : "+reason);
		 linkedProductFailureCount++;
	 }
	 private void appendIntoProductPlatfromSuccessBuffer(String productName, String platfromCodes){
		 productPlatformSuccessBuffer.append('\n');
		 productPlatformSuccessBuffer.append("Product name to Platform: "+productName+pipeSep+"Platform code List : "+platfromCodes);
		 productPlatformSuccessCount++;
	 }
	 
	 private void appendIntoProductPlatfromFailureBuffer(String productName, String reason){
		 productPlatformFailureBuffer.append('\n');
		 productPlatformFailureBuffer.append("Product name to Platform: "+productName+pipeSep+"Reason : "+reason);
		 productPlatformFailureCount++;
	 }
	 private String getLinkedProductFromProduct(final String childId, EnforceableProductDto product) {
			
			String linkedProductId=null;
			//product de-duplication
			for (LinkedProductNew linkedProduct : product.getLinkedProducts()) {
				if (linkedProduct.getProductId()  == childId ) {
					
					linkedProductId=linkedProduct.getProductId() + "";
					
					break;
				}
			}
			return linkedProductId;
		}
	 private List<RegistrationDefinition> getRegistrationDefinitionsForProduct(final Product product){
			List<RegistrationDefinition> result;
			try{
				List<RegistrationDefinition> prodRegDefs = this.registrationDefinitionService.getRegistrationDefinitionsForProduct(product);
				result = prodRegDefs;
			}catch(ServiceLayerException sle){
				result = new ArrayList<RegistrationDefinition>();
			}
			return result;
		}
	 
	 private LicenceTemplate createLicenceTemplate(final ProductBean productBean) {
			LicenceType licenceType = productBean.getLicenceType();
			if (LicenceType.CONCURRENT == licenceType) return new ConcurrentLicenceTemplate();
			if (LicenceType.ROLLING == licenceType) return new RollingLicenceTemplate();
			if (LicenceType.USAGE == licenceType) return new UsageLicenceTemplate();
			throw new IllegalStateException("Unsupported licence type: " + licenceType);
		}
	 
	 private LicenceTemplate updateLicenceTemplate(final ProductBean productBean, LicenceTemplate licenceTemplate) {
			LicenceType licenceType = productBean.getLicenceType();
			
			switch (licenceType) {
			case CONCURRENT:
				((ConcurrentLicenceTemplate) licenceTemplate).setUserConcurrency(Integer.parseInt(productBean.getUserConcurrency()));
				((ConcurrentLicenceTemplate) licenceTemplate).setTotalConcurrency(Integer.parseInt(productBean.getTotalConcurrency()));
				break;
			case ROLLING:
				((RollingLicenceTemplate) licenceTemplate).setTimePeriod(Integer.parseInt(productBean.getTimePeriod()));
				((RollingLicenceTemplate) licenceTemplate).setBeginOn(productBean.getRollingBeginOn());
				((RollingLicenceTemplate) licenceTemplate).setUnitType(productBean.getRollingUnitType());
				break;
			case USAGE:
				((UsageLicenceTemplate) licenceTemplate).setAllowedUsages(Integer.parseInt(productBean.getLicenceAllowedUsages()));
				break;
			}
			
			licenceTemplate.setStartDate(productBean.getLicenceStartDate());
			licenceTemplate.setEndDate(productBean.getLicenceEndDate());
			
			return licenceTemplate;
		}
}
