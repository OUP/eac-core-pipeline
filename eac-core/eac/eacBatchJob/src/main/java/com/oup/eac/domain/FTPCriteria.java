package com.oup.eac.domain;

import java.io.InputStream;

public class FTPCriteria {

    
    private String fileName;
    
    private InputStream inputStream;
    
    private String host;
    
    private String username;
    
    private String password;
    
    private String directory;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FTPCriteria ["
                + (fileName != null ? "fileName=" + fileName + ", " : "")
                + (host != null ? "host=" + host + ", " : "")
                + (username != null ? "username=" + username + ", " : "")
                + (password != null ? "password=" + password + ", " : "")
                + (directory != null ? "directory=" + directory : "") + "]";
    }

    public static FTPCriteria valueOf(final String fileName, final InputStream inputStream, final String host, final String username, final String password, final String directory) {
    	FTPCriteria ftpCriteria = new FTPCriteria();
    	ftpCriteria.setFileName(fileName);
    	ftpCriteria.setInputStream(inputStream);
    	ftpCriteria.setHost(host);
    	ftpCriteria.setUsername(username);
    	ftpCriteria.setPassword(password);
    	ftpCriteria.setDirectory(directory);
    	return ftpCriteria;
	}

}
