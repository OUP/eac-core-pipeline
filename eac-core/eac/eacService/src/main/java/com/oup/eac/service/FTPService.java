package com.oup.eac.service;

import java.io.IOException;
import java.net.SocketException;

import com.oup.eac.domain.FTPCriteria;

public interface FTPService {

	void ftp(FTPCriteria criteria) throws SocketException, IOException;
	
}