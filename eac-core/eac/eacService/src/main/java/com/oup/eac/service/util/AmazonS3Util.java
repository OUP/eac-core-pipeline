package com.oup.eac.service.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.oup.eac.common.utils.EACSettings;

/**
 * @author 511390 The Class AmazonS3TestUtil.
 *
 */
public class AmazonS3Util {

	/*public static void main(String[] args) {

		S3Object s3Object = AmazonS3Util.downloadObject("reports",
				"registrationReport15-11-2017.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				s3Object.getObjectContent()));
		String line;
		try {
			// br.readLine(); // skip first line
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("end");
	}*/

	private static final Logger LOGGER = Logger.getLogger(AmazonS3Util.class);

	// private static ApplicationContext context = new
	// ClassPathXmlApplicationContext(
	// "classpath*:/aces-web-services-bulk-upload-code.xml");

	private static AmazonS3 s3 = null;
	private static final String BUCKETNAME = getProperty("aws.s3.report.bucketname");

	/** AWS Access Credentials */
	public static final String AWS_S3_ACCESS_KEY = getProperty("aws.s3.accesskey");
	public static final String AWS_S3_SECRET_KEY = getProperty("aws.s3.secretkey");

	public static final String SUFFIX = "/";

	static {
		s3 = createAwsCredential();
	}

	/**
	 * This method will give value for passed key.
	 * 
	 * @param message
	 * @return String
	 */
	public static String getProperty(String message) {
		
		return EACSettings.getProperty(message);
	}

	/**
	 * This method will create AmazonS3 connection.
	 * 
	 * @return
	 */
	public static AmazonS3 createAwsCredential() {
		AWSCredentials credentials = null;
		try {
			//change
			credentials = new BasicAWSCredentials(AWS_S3_ACCESS_KEY,
					AWS_S3_SECRET_KEY);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("kjfkjf-----------------------------------------------");
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.",
					e);
		}

		AmazonS3 s3 = null;
		s3 = new AmazonS3Client(credentials);
		return s3;
	}

	/**
	 * This method will upload file on AmazonS3 bucket.
	 * 
	 * @return
	 */
	public static void uploadFilesOnS3(String folderName, File folder) {

		// createFolderOnS3(folderName);

		// if a file, upload it else iterate all files in the folder and upload
		// it to s3
		if (folder.isFile()) {
			PutObjectResult result = s3.putObject(new PutObjectRequest(
					BUCKETNAME, folderName + folder.getName(), folder));
		} else {
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				PutObjectResult result = s3.putObject(new PutObjectRequest(
						BUCKETNAME, folderName + "/" + file.getName(), file));
			}
		}
	}

	/**
	 * This method will create folder on s3 with current date.
	 * 
	 * @param BUCKETNAME
	 * @param folderName
	 * @param client
	 */
	public static void createFolderOnS3(String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKETNAME,
				folderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		s3.putObject(putObjectRequest);
	}

	/**
	 * Download an object - When you download an object, you get all of the
	 * object's metadata and a stream from which to read the contents. It's
	 * important to read the contents of the stream as quickly as possibly since
	 * the data is streamed directly from Amazon S3 and your network connection
	 * will remain open until you read all the data or close the input stream.
	 * 
	 * @param s3
	 * @param bulkuploadIncomingFolder
	 * @param inputRequest
	 * @param inputField
	 * @throws IOException
	 */
	public static S3Object downloadObject(String fileName) {
		S3Object object = s3.getObject(new GetObjectRequest(BUCKETNAME,
				fileName));
		return object;
	}

	/**
	 * Check if file is present on Amazon s3 aces-bulkupload bucket in the
	 * folder provided
	 * 
	 * @param folderName
	 * @param fileName
	 * @throws IOException
	 * @return fileExists true/false
	 */
	public static boolean isFileOnS3(String folderName, String fileName) {
		boolean fileExists = true;
		fileName = folderName + "/" + fileName;
		System.out.println("file name is : " + fileName);
		try {
			GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest(
					BUCKETNAME, fileName);
			long fileSize = s3.getObjectMetadata(getObjectMetadataRequest)
					.getContentLength();
		} catch (Exception e) {
			fileExists = false;
		}
		return fileExists;
	}

	/**
	 * This method will give us currentDate.
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateformatddMMyyyy.format(new Date());
		return date;
	}

	/**
	 * deletes the file
	 * 
	 * @param fileName
	 * @return file
	 */
	public static File createFile(final String fileName) {
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
		System.out.println("temp directory for file : " + tempDir);
		FileWriter fw = null;
		File template = new File(tempDir + "/" + fileName);

		if (!template.exists()) {
			try {
				template.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return template;
	}

	/**
	 * Writes content provided in the file
	 * 
	 * @param file
	 * @param content
	 * 
	 */
	public static void writeToFile(File file, String content) {
		FileWriter fw = null;
		if (content == null) {
			content = "";
		}
		System.out.println("content : " + content);
		try {
			fw = new FileWriter(file.getAbsoluteFile(), true);
			fw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * deletes the file
	 * 
	 * @param fileToDelete
	 * 
	 */
	public static void deleteFile(File fileToDelete) {
		fileToDelete.delete();
	}

	/**
	 * deletes files on Amazon S3
	 * 
	 * @param fileName
	 *            on Amazon S3 aces-bulkupload bucket to delete
	 */
	public static void deleteFilesOnS3(Map<String, String> fileMap,
			String folder) {
		for (String fileName : fileMap.keySet()) {
			s3.deleteObject(BUCKETNAME, folder + "/" + fileName);
		}
	}
}
