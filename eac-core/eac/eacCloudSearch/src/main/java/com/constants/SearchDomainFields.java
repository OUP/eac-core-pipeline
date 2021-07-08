package com.constants;

public interface SearchDomainFields {
	// field names for user domain
	public static final int USER_DOMAIN = 1;
	public static final String USER_USERID = "userid";
	public static final String USER_USERNAME = "username";
	public static final String USER_EMAILADDRESS = "emailaddress";
	public static final String USER_FIRSTNAME = "firstname";
	public static final String USER_LASTNAME = "lastname";
	public static final String USER_CREATEDDATE = "createddate";
	public static final String USER_LASTLOGIN = "lastlogin";
	public static final String USER_LICENSE_COUNT = "licensecount";
	public static final String USER_EXTERNALID = "externalids";
	public static final String USER_EXTERNALSYSTEMS = "externalsystems";
	public static final String USER_STATUS = "status";
	public static final String USER_ARCHIVESTATUS = "archivestatus";
	
	// field names for product domain
	public static final int PRODUCT_DOMAIN = 2;
	public static final String PRODUCT_PRODUCTNAME = "productname";
	public static final String PRODUCT_PRODUCTID = "productid";
	public static final String PRODUCT_EXTERNALIDS = "externalids";
	public static final String PRODUCT_REGISTRABLETYPE = "registrabletype";
	public static final String PRODUCT_ACTIVATION_METHOD = "activationmethod";
	public static final String PRODUCT_URLS = "urls";
	public static final String PRODUCT_PRODUCTGROUPIDS = "productgroupids";
	public static final String PRODUCT_DIVISIONID = "divisionid";
	public static final String PRODUCT_STATE = "state";
	public static final String PRODUCT_REGISTRATION_DEFINITION_TYPE = "registrationdefinitiontype";
	public static final String PRODUCT_EXTERNALSYSTEMS = "externalsystems";
	public static final String PRODUCT_PLATFORMS = "platforms";

	// field names for group domain
	public static final int GROUP_DOMAIN = 3;
	public static final String GROUP_ARCHIVESTATUS = "archivestatus";
	public static final String GROUP_CREATEDDATE = "createddate";
	public static final String GROUP_GROUPID = "groupid";
	public static final String GROUP_GROUPNAME = "groupname";
	public static final String GROUP_GROUPTYPE = "grouptype";
	public static final String GROUP_LICENSES = "licenses";
	public static final String GROUP_PARENTIDS = "parentids";
	public static final String GROUP_GROUPUNIQUEID = "groupuniqueid";
	public static final String GROUP_PRIMARYEMAILADDRESS = "primaryemailaddress";
	public static final String GROUP_COUNTRYCODE = "groupcountrycode";
	public static final String GROUP_CURRICULUMTYPE = "groupcurriculumtype";
	public static final String GROUP_REGISTRATIONSTATUS = "groupregistrationstatus";

	// field names for Activation Code domain
	public static final int ACT_CODE_DOMAIN = 4;
	public static final String CLAIM_TICKET_CLAIMTICKETID = "claimticketid";
	public static final String CLAIM_TICKET_ACTIVATIONCODE = "activationcode";
	public static final String CLAIM_TICKET_BATCHNAME = "batchname";
	public static final String CLAIM_TICKET_PRODUCTID = "productid";
	public static final String CLAIM_TICKET_PRODUCTGROUPID = "productgroupid";
	public static final String CLAIM_TICKET_LICENSETYPE = "licensetype";
	public static final String CLAIM_TICKET_CREATEDDATE = "createddate";
	public static final String CLAIM_TICKET_STARTDATE = "startdate";
	public static final String CLAIM_TICKET_ENDDATE = "enddate";
	public static final String CLAIM_TICKET_CLAIMFOLDERID = "claimfolderid";
	public static final String CLAIM_TICKET_OWNER = "owner";
	public static final String CLAIM_TICKET_ALLOWEDUSAGES = "allowedusages";
	public static final String CLAIM_TICKET_MANAGEDBY = "managedby";
	public static final String CLAIM_TICKET_AVAILABLESTATE = "availablestate";
	public static final String CLAIM_TICKET_LICENSESTARTDATE = "licensestartdate";
	public static final String CLAIM_TICKET_LICENSEENDDATE = "licenseenddate";
	public static final String CLAIM_TICKET_UNITTYPE = "unittype";
	public static final String CLAIM_TICKET_TIMEPERIOD = "timeperiod";
	public static final String CLAIM_TICKET_MASTERACTIVATIONCODE = "masteractivationcode";
	public static final String CLAIM_TICKET_BEGINON = "beginon";
	public static final String CLAIM_TICKET_CLAIMDIRECTLICENSEID = "claimdirectlicenseid";
	public static final String CLAIM_TICKET_ACTUAL_USAGES = "actualusages";
	

	// field names for user group membership domain
	public static final int USER_GRP_MEMBERSHIP_DOMAIN = 5;
	public static final String USER_GRP_USERID = "userid";
	public static final String USER_GRP_GROUPID = "groupid";
	public static final String USER_GRP_ROLENAME = "rolename";
	public static final String USER_GRP_INVITATIONSTATUS = "invitationstatus";
	public static final String USER_GRP_ARCHIVESTATUS = "archivestatus";
	public static final String USER_GRP_INVITATIONTIMESTAMP = "invitationtimestamp";
	public static final String USER_GRP_REMINDERTIMESTAMP = "remindertimestamp";
	public static final String USER_GRP_INVITATIONEXPIRYTIMESTAMP = "invitationexpirytimestamp";
	public static final String USER_GRP_DISMISSFLAG = "dismissedflag";
	
	// field names for product group domain
	public static final int PRODUCT_GROUP_DOMAIN = 6;
	public static final String PRODUCT_GRP_PRODUCTGROUPID = "productgroupid";
	public static final String PRODUCT_GRP_PRODUCTGROUPNAME = "productgroupname";

	//field names for license domain
	public static final int LICENSE_DOMAIN = 8 ;
	public static final String LICENSE_PRODUCT_ID = "productid" ;
	public static final String LICENSE_USER_ID = "userid" ;
	public static final String LICENSE_LICENSE_TYPE = "licensetype" ;
	public static final String LICENSE_LICENSE_START_DATE = "licensestartdate" ;
	public static final String LICENSE_LICENSE_END_DATE = "licenseenddate" ;
	public static final String LICENSE_UNIT_TYPE = "unittype" ;
	public static final String LICENSE_TIME_PERIOD = "timeperiod" ;
	public static final String LICENSE_GROUP_ID = "groupid" ;
	public static final String LICENSE_ARCHIVE_STATUS = "archivestatus" ;
	public static final String LICENSE_IS_LEARNING_ASSIGNMENT = "islearningassignment" ;
	public static final String LICENSE_LICENSE_ID = "licenseid" ;
	public static final String LICENSE_BEGIN_ON = "beginon" ;
	public static final String LICENSE_IS_LICENSE_STARTED = "islicensestarted" ;
	public static final String LICENSE_LICENSE_EXPIRY_DATE = "licenseexpirydate" ;
	public static final String ACTIVATION_CODE = "activationcode" ;
	public static final String LICENSE_CREATED_DATE = "licensecreateddate" ;

	
	// field names for Activation Code Assignment domain
	public static final int ACTIVATION_CODE_ASSIGNMENT_DOMAIN = 7;
	public static final String ACT_CODE_ASSIGNMENT_ACTIVATIONCODE = "activationcode";
	public static final String ACT_CODE_ASSIGNMENT_USERID = "userid";
	public static final String ACT_CODE_ASSIGNMENT_LICENSEID = "licenseid";
	public static final String ACT_CODE_ASSIGNMENT_STATUS = "status";
	
	
}
