package com.oup.eac.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.domain.FTPCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.FTPService;

@Service("ftpService")
public class FTPServiceImpl implements FTPService {

	private static final Logger LOGGER = Logger.getLogger(FTPServiceImpl.class);
	
	@Override
	public void ftp(FTPCriteria criteria) throws SocketException, IOException {
        if (EACSettings.getBoolProperty(EACSettings.FTP_DISABLED)) {
            LOGGER.info("FTP is disabled. Details below:");
            //LOGGER.info(criteria);
            return;
        }		
		FTPClient ftpClient = new FTPClient();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
		    ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(baos)));
		    ftpClient.connect(criteria.getHost());
		    ftpFile(criteria, ftpClient); 
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
			    if(LOGGER.isDebugEnabled()) {
			    	LOGGER.debug(obfuscate(baos.toString()));
			    }
			    if(ftpClient.isConnected()) {
    				ftpClient.disconnect();
    				boolean disconnectSuccess = ftpClient.getReplyCode() == FTPReply.SERVICE_CLOSING_CONTROL_CONNECTION;
    				logFTPEvent(disconnectSuccess, "Disconnection from host %s successful: ", criteria.getHost());
			    }
			} catch (Exception e) {
			    LOGGER.error(e.getMessage(), e);
			}
		}
	}

    private void ftpFile(FTPCriteria criteria, FTPClient ftpClient) throws IOException {
        boolean loginSuccess = ftpClient.login(criteria.getUsername(), criteria.getPassword());
        logFTPEvent(loginSuccess, "Login to host %s successful: ", criteria.getHost());
        if(loginSuccess) {
            ftpClient.enterLocalPassiveMode();
            boolean changeDirectoryRequired = StringUtils.isNotBlank(criteria.getDirectory());
            if(changeDirectoryRequired) {
                boolean changeDirSuccess = changeDirectory(criteria, ftpClient);
                if(changeDirSuccess) {
                    uploadFile(criteria, ftpClient);
                }
            } else {
                uploadFile(criteria, ftpClient);
            }
            logout(criteria, ftpClient);
        }
    }

    private boolean changeDirectory(FTPCriteria criteria, FTPClient ftpClient) throws IOException {
        ftpClient.changeWorkingDirectory(criteria.getDirectory());
        boolean changeDirSuccess = ftpClient.getReplyCode() == FTPReply.FILE_ACTION_OK;
        logFTPEvent(changeDirSuccess, "Change directory to %s successful: ", criteria.getDirectory());
        return changeDirSuccess;
    }

    private void uploadFile(FTPCriteria criteria, FTPClient ftpClient) throws IOException {
        boolean uploadFileSuccess = ftpClient.storeFile(criteria.getFileName(), criteria.getInputStream());
        logFTPEvent(uploadFileSuccess, "Upload of file %s successful: ", criteria.getFileName());
        AuditLogger.logSystemEvent("FTP", "Source:"+criteria.getFileName(), "Host:"+criteria.getHost(), "Dir:"+criteria.getDirectory());	
    }

    private void logout(FTPCriteria criteria, FTPClient ftpClient) throws IOException {
        boolean logoutSuccess = ftpClient.logout();
        logFTPEvent(logoutSuccess, "Logout of host %s successful: ", criteria.getHost());
    }
	
	private void logFTPEvent(boolean success, String format, Object... args) {
	    String message = String.format(format, args);
	    if(success) {
	        if(LOGGER.isDebugEnabled()) LOGGER.debug(message + Boolean.toString(success));
	    } else {
	        LOGGER.error(message + Boolean.toString(success));
	    }
	}
 	
	private String obfuscate(String text) {
		StringBuilder fixed = new StringBuilder();
		for(String line : text.split("\r\n")) {
			if(line.startsWith("PASS")) {
				fixed.append("PASS ***************\r\n");
			} else {
				fixed.append(line).append("\r\n");
			}
		}
		return fixed.toString();
	}

}
