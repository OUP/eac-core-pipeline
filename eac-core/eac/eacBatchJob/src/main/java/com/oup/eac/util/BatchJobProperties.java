package com.oup.eac.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class BatchJobProperties {
	private static Logger logger = Logger.getLogger(BatchJobProperties.class
			.getName());
	static Properties props;
	static {
		props = new Properties();
		String SEPARATOR = System.getProperty("file.separator");
		String propertiesPath = new File("").getAbsolutePath() + SEPARATOR
				+ "eacBatch.properties";

		try {
			props.load(new FileInputStream(new File(propertiesPath)));
		} catch (IOException e) {
			logger.error("IOException", e);
			e.printStackTrace();
		}
	}

	public static String EAC_DB_NAME = props.getProperty("EAC_DB_NAME");
	public static String ER_DB_NAME = props.getProperty("ER_DB_NAME");
	public static String DB_USER = props.getProperty("DB_USER");
	public static String DB_PASSWORD = props.getProperty("DB_PASSWORD");
	public static final String DB_HOST_NAME = props.getProperty("DB_HOST_NAME");
	public static final String ER_CONN_STR = "jdbc:sqlserver://" + DB_HOST_NAME
			+ ":1433;" + "databaseName=" + EAC_DB_NAME + ";" + "user=" + DB_USER
			+ ";" + "password=" + DB_PASSWORD + ";" + "database=" + EAC_DB_NAME
			+ ";" + "useUnicode=true;" + "characterEncoding=UTF-8";
	
	public static final String AWS_LAMBDA_ACCESSKEY = props.getProperty("AWS_LAMBDA_ACCESSKEY");
	public static final String AWS_LAMBDA_SECRETKEY = props.getProperty("AWS_LAMBDA_SECRETKEY");
	public static final String AWS_REGION = props.getProperty("AWS_REGION");
	public static final String AWS_DYNAMODB_TABLENAME = props.getProperty("AWS_DYNAMODB_TABLENAME");
	public static final String GET_OUP_ID_FROM_ERIGHTS_ID = props.getProperty("GET_OUP_ID_FROM_ERIGHTS_ID");
	public static final String GET_ERIGHTS_ID_FROM_OUP_ID = props.getProperty("GET_ERIGHTS_ID_FROM_OUP_ID");
	
	public static final String EMAIL_SENT_DEBUG_FORMAT = "EEE dd/MM/yyyy hh:mm";
	public static final String EAC_EMAIL_REPLY_TO_ADDRESS = props.getProperty("EAC_EMAIL_REPLY_TO_ADDRESS");
	public static final String EMAIL_FROM_ADDRESS = props.getProperty("EMAIL_FROM_ADDRESS");
	public static final String EMAIL_FROM_TITLE = props.getProperty("EMAIL_FROM_TITLE");
	public static final String EAC_ADMIN_EMAIL = props.getProperty("EAC_ADMIN_EMAIL");
	public static final boolean EMAIL_DISABLED = Boolean.valueOf(props.getProperty("EMAIL_DISABLED"));
	public static final boolean FTP_DISABLED = Boolean.valueOf(props.getProperty("FTP_DISABLED"));
	
	public static final String CMDP_HOST = props.getProperty("CMDP_HOST");
	public static final String CMDP_USERNAME = props.getProperty("CMDP_USERNAME");
	public static final String CMDP_PASSWORD = props.getProperty("CMDP_PASSWORD");
	public static final String CMDP_DIRECTORY = props.getProperty("CMDP_DIRECTORY");
	public static final String CMDP_SUPPORT_EMAIL = props.getProperty("CMDP_SUPPORT_EMAIL");
	
	public static final String MAIL_SMTP_PORT = props.getProperty("MAIL_SMTP_PORT");
	public static final String MAIL_SMTP_HOST = props.getProperty("MAIL_SMTP_HOST");
	public static final String MAIL_SMTP_USERNAME = props.getProperty("MAIL_SMTP_USERNAME");
	public static final String MAIL_SMTP_PASSWORD = props.getProperty("MAIL_SMTP_PASSWORD");
	
	public static final String FAJAR_ADMIN_EMAIL = props.getProperty("FAJAR_ADMIN_EMAIL");
	public static final String OXED_ADMIN_EMAIL = props.getProperty("OXED_ADMIN_EMAIL");
	
	public static final String ORG_UNIT_ADMIN_EMAIL = props.getProperty("ORG_UNIT_ADMIN_EMAIL");
}
