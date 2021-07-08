package com.oup.eac.common.utils;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.crypto.SimpleCipher;
import com.oup.eac.common.utils.spring.ApplicationContextSupport;

/**
 * @author harlandd Singleton class for handling all EAC settings.
 */
public final class EACSettings {

    /** Key for date format. */
    public static final String TEST_COOKIE_TIMEOUT = "test.cookie.timeout";

    /** EAC property for email status. */
    public static final String EMAIL_DISABLED = "email.disabled";

    /** EAC hibernate dialect. */
    public static final String HIBERNATE_DIALECT = "hibernate.dialect";

    /** EAC login url. */
    public static final String EAC_LOGIN_URL = "eac.login.url";    
    
    /** EAC Erights webs service url. */
    public static final String ERIGHTS_WEBSERVICE_URL = "erights.webservice.url";

    /** EAC product registration token allow url. */
    public static final String PRODUCT_REGISTRATION_TOKEN_ALLOW_URL = "product.registration.token.allow.url";
    
    /** EAC product registration token allow url. */
    public static final String PRODUCT_REGISTRATION_VALIDATOR_TOKEN_ALLOW_URL = "product.registration.validator.token.allow.url";
    
    /** EAC product registration token deny url. */
    public static final String PRODUCT_REGISTRATION_VALIDATOR_TOKEN_DENY_URL = "product.registration.validator.token.deny.url";

    /** EAC product registration email subject. */
    public static final String PRODUCT_REGISTRATION_EMAIL_SUBJECT = "product.registration.email.subject";
    
    public static final String PRODUCT_REGISTRATION_DENIED_EMAIL_SUBJECT = "product.registration.denied.email.subject";
    
    public static final String PRODUCT_REGISTRATION_VALIDATE_EMAIL_SUBJECT = "product.registration.validate.email.subject";
    
    public static final String PRODUCT_REGISTRATION_ACTIVATED_EMAIL_SUBJECT = "product.registration.activated.email.subject";

    /** EAC email from address. */
    public static final String EMAIL_FROM_ADDRESS = "mail.from";

    /** EAC email from title. */
    public static final String EMAIL_FROM_TITLE = "email.from.title";

    /** EAC password reset email subject. */
    public static final String PASSWORD_RESET_EMAIL_SUBJECT = "password.reset.email.subject";
    
    /** EAC password reset email subject. */
    public static final String PASSWORD_RESET_EMAIL_SUBJECT_NEW = "password.reset.email.subject.new";
    
    /** EAC password reset email url. */
    public static final String PASSWORD_RESET_EMAIL_URL = "password.reset.email.url";
    
    /** EAC email validation subject. */
    public static final String EMAIL_VALIDATION = "email.validation";
    
    /** EAC product registration token allow url. */
    public static final String EMAIL_VALIDATION_TOKEN_URL = "email.validation.token.url";

    /** EAC disable host checking during https webservice calls. **/
    public static final String WS_HTTPS_TRUST_ALL_HOSTS = "ws.https.trustallhosts";

    /** EAC trust all certificates during https webservice calls. **/
    public static final String WS_HTTPS_TRUST_ALL_CERTIFICATES = "ws.https.certificates";

    /** EAC should webservice calls be WS-Security enabled? **/
    public static final String WS_SECURITY_ENABLED = "ws.security.enabled";

    /** EAC username used for authenticating webservice calls via WS-Security. **/
    public static final String WS_SECURITY_USERNAME = "ws.security.username";

    /** EAC password used for authenticating webservice calls via WS-Security. **/
    public static final String WS_SECURITY_PASSWORD = "ws.security.password";

    /** EAC parameter name used for communication with the adaptor. **/
    public static final String ERIGHTS_URL_PARAMETER_NAME = "erights.url.parameter.name";
    
    /** EAC parameter name used for receiving auth failure reason from erights adaptor. **/
    public static final String ERIGHTS_AUTH_FAILURE_REASON_PARAMETER_NAME = "erights.auth.failure.reason.parameter.name";    
    
    /** EAC parameter name used for receiving deny reason from erights adaptor. **/
    public static final String ERIGHTS_DENY_REASON_PARAMETER_NAME = "erights.deny.reason.parameter.name";
    
    /** EAC parameter name used for receiving license decision from erights adaptor. **/
    public static final String ERIGHTS_LICENCE_DECISIONS_PARAMETER_NAME = "erights.licence.decisions.parameter.name";
    
    /** EAC default url to forward to when no forward url is available in the users session. **/
    public static final String EAC_DEFAULT_FORWARD_URL = "eac.default.forward.url";
    
    /** EAC maximum failed authentication attempts allowed before a customer is locked. **/
    public static final String MAX_FAILED_AUTH_ATTEMPTS = "eac.max.failed.auth.attempts";
    
    /** 16 character AES shared secret to encrypt eac session token */
    public static final String EAC_SESSION_TOKEN_ENCRYPTION_KEY = "eac.session.token.encryption.key";

    public static final String VERSION = "eac.version";
    
    public static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    
    public static final String EAC_ADMIN_EMAIL = "eac.admin.email";
    
    public static final String EAC_EMAIL_REPLY_TO_ADDRESS = "mail.support";
    
    public static final String ERIGHTS_WS_SLA_MILLIS = "erights.ws.sla.millis";
    
    public static final String EAC_WEBTRENDS_REPORTING_ID = "eac.webtrends.reporting.id";
    
    public static final String EAC_WEBTRENDS_REPORTING_DOMAIN = "eac.webtrends.reporting.domain";
    
    /** EAC property for FTP status. */
    public static final String FTP_DISABLED = "ftp.disabled";
    
    public static final String EAC_EMAIL_DATE_STYLE = "email.date.style";

    public static final String EMAIL_SENT_DEBUG_FORMAT = "email.sent.debug.format";
    
    /** EAC username regex. */
    public static final String USERNAME_POLICY_REGEX = "username.policy.regex";
	
    /** EAC password regex . */
    public static final String PASSWORD_POLICY_REGEX = "password.policy.regex";
    
    /** EAC password resetForget regex . */
    public static final String PASSWORD_POLICY_RESETFORGET_REGEX = "password.policy.resetForget.regex";
    
    /** EAC username text. */
    public static final String USERNAME_POLICY_TEXT = "username.policy.text";
	
	 /** EAC password text */
    public static final String PASSWORD_POLICY_TEXT = "password.policy.text";
    
    /** EAC resetpasswordWs baseurl */
    public static final String EAC_RESETPASSWORD_WS_BASEURL = "eac.resetpasswordWS.baseurl";
    														   
    /** Search AccessKey cloud endpoint  */
    public static final String SEARCH_ACCESS_KEY = "aws.searchdoc.accesskey";
    
    /** Search SecretKey cloud endpoint  */
    public static final String SEARCH_SECRET_KEY = "aws.searchdoc.secretkey";
    
    /** Search user cloud endpoint  */
    public static final String SEARCH_USER_ENDPOINT = "aws.user.search.endpoint";
    
    /** Search group cloud endpoint  */
    public static final String SEARCH_GROUP_ENDPOINT = "aws.group.search.endpoint";
    
    /** Search usergroupmembership cloud endpoint  */
    public static final String SEARCH_USERGROUPMEMBERSHIP_ENDPOINT = "aws.usergroupmembership.search.endpoint";
    
    /** Search product cloud endpoint  */
    public static final String SEARCH_PRODUCT_ENDPOINT = "aws.product.search.endpoint";
    
    /** Search activationcode cloud endpoint  */
    public static final String SEARCH_ACTIVATIONCODE_ENDPOINT = "aws.activationcode.search.endpoint";

    /** Search license cloud endpoint  */
    public static final String SEARCH_LICENSE_ENDPOINT = "aws.license.search.endpoint";
    
    /** Search productgroup cloud endpoint  */
    public static final String SEARCH_PRODUCTGROUP_ENDPOINT = "aws.productgroup.search.endpoint";
    
    /** Search activationcodeassignment cloud endpoint  */
    public static final String SEARCH_ACTIVATIONCODEASSIGNMENT_ENDPOINT = "aws.activationcodeassignment.search.endpoint";
    
    /**
     * EMAILBROKER_FAILURE_URL
     */
    public static final String EMAILBROKER_FAILURE_URL = "emailbroker.failure.url";
    /**
     * EMAILBROKER_ADMIN_HOST
     */
	/*public static final String EMAILBROKER_ADMIN_HOST = "emailbroker.admin.host";*/
	
	public static final String EMAILBROKER_VALIDATOR_REGISTRATION_ALLOW_URL = "emailbroker.admin.validatorRegistrationAllow.url";
	
	public static final String EMAILBROKER_VALIDATOR_REGISTRATION_DENY_URL = "emailbroker.admin.validatorRegistrationDeny.url";
	

    /** EAC product registration token allow url. */
    public static final String PRODUCT_REGISTRATION_TOKEN_ALLOW_URL_NEW = "product.registration.token.allow.url.new";
    
    /** EAC product registration token allow url. */
    public static final String PRODUCT_REGISTRATION_VALIDATOR_TOKEN_ALLOW_URL_NEW = "product.registration.validator.token.allow.url.new";
    
    /** EAC product registration token deny url. */
    public static final String PRODUCT_REGISTRATION_VALIDATOR_TOKEN_DENY_URL_NEW = "product.registration.validator.token.deny.url.new";
    
    /** EACSettings instance. */
    private static EACSettings instance;
    
    /*public static final String ADMIN_PASSWORD_RESET_EMAIL_URL = "admin.password.reset.email.url";*/
    
    public static final String EAC_HOST_URL = "eac.host.url";
    
    public static final String OLB_LANDING_PAGE_URL = "olb.landing.page.url";
    public static final String OLB_WS_USER = "olb.ws.user";
    
    /** Upload of Bulk activation Codes. */
    public static final String ERROR_INVALID_TOKEN = "error.invalidtokens";
    public static final String ERROR_INVALID_ALLOWEDUSAGE = "error.invalidallowedusage";
    public static final String ERROR_INVALID_CODEFORMAT = "error.invalidcodeformat";
    public static final String ERROR_INVALID_DATE = "error.invaliddate";
    public static final String ERROR_INVALID_LICENSE = "error.invalidlicense";
    public static final String ERROR_INVALID_BATCHNAME = "error.invalidbatchname";
    public static final String ERROR_BATCHNAME_LENGTH = "error.batchnamelength";
    public static final String ERROR_SHEET_ROWNUMBER = "error.sheetrownumber";
    public static final String ERROR_PRODUCTORGROUPNAME = "error.productorgroupname";
    public static final String ERROR_INVALID_ROLLINGLICENSE = "error.invalid.rollinglicense";
    public static final String ERROR_INVALID_USAGELICENSE = "error.invalid.usagelicense";
    public static final String ERROR_INVALID_CONCURRENTLICENSE = "error.invalid.concurrentlicense";
    public static final String ERROR_SAME_BATCHNAME = "error.same.batchname";

    public static final String ERIGHTS_REST_WEBSERVICE_URL = "erights.rest.webservice.url";
    
    public static final String CES_MASTER_GROUP_NAME = "ces.master.group.name";
    public static final String CES_MASTER_GROUP_TYPE = "ces.master.group.name";
    
/** AWS configurattion kms */
    
    public static final String AWS_CLIENT_CONFIG_PROXY_ENABLE = "aws.clientconfig.oupproxy.enabled";
    public static final String AWS_CLIENT_CONFIG_PROXY_HOST = "aws.clientconfig.oupproxy.host";
    public static final String AWS_CLIENT_CONFIG_PROXY_PORT = "aws.clientconfig.oupproxy.port";
    public static final String AWS_CLIENT_CONFIG_PROXY_USER = "aws.clientconfig.oupproxy.user";
    public static final String AWS_CLIENT_CONFIG_PROXY_PASSWORD = "aws.clientconfig.oupproxy.password";
    public static final String AWS_KMS_PASSWORD_ACCESSKEY = "aws.kms.password.accesskey";
    public static final String AWS_KMS_PASSWORD_SECRETKEY = "aws.kms.password.secretkey";
    public static final String AWS_KMS_PASSWORD_ENDPOINT = "aws.kms.password.endPoint";
    public static final String AWS_KMS_PASSWORD_KEYID = "aws.kms.password.keyId";
    
    public static final String PASSWORD_ENCRYPTION_PUBLIC_KEY = "trusted.system.encrypt.public.key" ;
    
    public static final String HTTPS_PROXY_HOST = "https.proxyHost";
    public static final String HTTPS_PROXY_PORT = "https.proxyPort";
    
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    public static final String HTTP_PROXY_PORT = "http.proxyPort";
    
    public static final String IS_PROXY_ENABLED = "is.proxy.enabled";
        
    private RuntimeContext runtimeContext;
	
	public static final String EMAIL_EXCLUSION_DIVISIONTYPE="product.email.exclusion.divisionType";

	public static final String EMAIL_EXCLUSION = "email.exclusion"; 
	
	
	/**
	 * Added for ssm parameter property details 
	 * */
	public static final String AWS_REGION = "aws.region";
	
	public static final String AWS_SSM_URL = "aws.ssm.endPoint";
	public static final String AWS_SSM_PARAMS = "aws.ssm.params";
	public static final String AWS_SSM_PARAMS_PREFIX = "aws.eac.envName";
	public static final String AWS_SSM_REGION = "aws.ssm.region";
	public static final String AWS_SSM_SERVICE = "aws.ssm.service";
	public static final String AWS_SSM_ACCESS_KEY_ID = "aws.accesskey";
	public static final String AWS_SSM_SECRET_KEY = "aws.secretkey";
	
	
	/**
	 * Added for Redis Cache configuration
	 * */
	/*Redis Configuration*/
	public static final String REDIS_CLIENT_NAME = "redis.client.name";
	public static final String REDIS_HOST_NAME = "redis.hostname";
	public static final String REDIS_PORT = "redis.port";
	public static final String REDIS_CACHE_TTL = "redis.caches.ttl.seconds";
	public static final String REDIS_CONNPOOL_MAX = "redis.connectionpool.maxTotal";
	public static final String REDIS_CONNPOOL_MAXIDLE = "redis.connectionpool.maxIdle";
	public static final String REDIS_CONNPOOL_MINIDLE = "redis.connectionpool.minIdle";
	public static final String REDIS_CONNPOOL_MAXWAIT = "redis.connectionpool.maxWaitMillis";
	
	/**
	 * CEB user datasync Queque name
	 * */
	
	public static final String SQS_CEB_USER_DATA = "aws.sqs.ceb.user.queueName";
	
    /**
     * Default constructor.
     */
    private EACSettings() {
        runtimeContext = (RuntimeContext) ApplicationContextSupport.getBean("runtimeContext");
    }

    /**
     * The underlying context for these properties.
     * 
     * @return The runtime context
     */
    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    /**
     * Initialises the settings.
     * 
     * @return singleton instance of EACSettings
     */
    private static synchronized EACSettings getInstance() {
        if (instance == null) {
            instance = new EACSettings();
        }
        return instance;
    }

    /**
     * Get a property.
     * 
     * @param key
     *            The property name
     * @return The value of the property
     */
    public static String getProperty(final String key) {
        return getInstance().getRuntimeContext().getProperty(key);
    }

    /**
     * Get a property, returning default value if no property exists.
     * 
     * @param key
     *            The property name
     * @param defaultValue
     *            Value to return if no property exists
     * @return The value of the property
     */
    public static String getProperty(final String key, final String defaultValue) {
        return getInstance().getRuntimeContext().getProperty(key, defaultValue);
    }
    
    /**
     * Returns a property from the runtime context. Will throw an exception if the property is not available.
     * 
     * @param key
     *            The key of the property to return.
     * @return The string value of the property.
     */
    public static String getRequiredProperty(final String key) {
        return getInstance().getRuntimeContext().getRequiredProperty(key);
    }

    /**
     * @param property
     *            name of property
     * @return String property
     */
    public static int getIntProperty(final String property) {
        return getInstance().getRuntimeContext().getIntProperty(property);
    }

    /**
     * @param property
     *            name of property
     * @return String property
     */
    public static boolean getBoolProperty(final String property) {
        return getInstance().getRuntimeContext().getBoolProperty(property);
    }
    
    public static String getDecryptedProperty(final String key) throws Exception {
        return SimpleCipher.decrypt(getInstance().getRuntimeContext().getProperty(key));
    }
    
    public static String getEncryptedProperty(final String key) throws Exception {
        return SimpleCipher.encrypt(getInstance().getRuntimeContext().getProperty(key));
    }

}
