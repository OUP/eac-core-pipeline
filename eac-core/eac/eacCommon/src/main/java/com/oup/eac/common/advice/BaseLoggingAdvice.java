package com.oup.eac.common.advice;

import org.apache.log4j.Logger;

public abstract class BaseLoggingAdvice {

	public Logger getLogger(){
		return Logger.getLogger(this.getClass().getName());
	}

}
