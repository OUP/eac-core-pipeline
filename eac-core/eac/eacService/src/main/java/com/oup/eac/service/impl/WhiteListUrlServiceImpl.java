package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.WhiteListUrlDao;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;

@Service(value="whiteListUrlService")
public class WhiteListUrlServiceImpl implements WhiteListUrlService{
	
	private final WhiteListUrlDao whiteListUrlDao;

	@Autowired
	public WhiteListUrlServiceImpl(final WhiteListUrlDao whiteListUrlDao) {
		this.whiteListUrlDao = whiteListUrlDao;
	}
	
	@Override
	public WhiteListUrl getListUrl(final String url) {
		return whiteListUrlDao.findById(url, true);
	}

	@Override
	public List<String> getUrls() {
		List<WhiteListUrl> whiteListUrls =  getAllUrls();
		List<String> urls = new ArrayList<String>();
		for(WhiteListUrl whiteListUrl : whiteListUrls){
			urls.add(whiteListUrl.getUrl());
		}
		return urls;
	}

	@Override
	public void saveWhiteListUrl(final WhiteListUrl whiteListUrl) {
		whiteListUrlDao.saveOrUpdate(whiteListUrl);
	}

	@Override
	public void deleteWhiteListUrl(final WhiteListUrl whiteListUrl) {
		whiteListUrlDao.delete(whiteListUrl);
	}
	
	@Override
	public List<WhiteListUrl> getAllUrls() {
		List<WhiteListUrl> allUrls = whiteListUrlDao.getAllUrls();
		return allUrls;
	}
	
	@Override
	public boolean updateUrls(List<WhiteListUrl> toDelete, List<WhiteListUrl> toUpdate, List<WhiteListUrl> toAdd) throws ServiceLayerException{
	        boolean isSuccess = insertData(toDelete, toUpdate, toAdd) ;
	       if(isSuccess){
	    	   return isSuccess;
	       }
	       return false;
	}
	
	 private boolean insertData(List<WhiteListUrl> toDelete, List<WhiteListUrl> toUpdate, List<WhiteListUrl> toAdd) throws ServiceLayerException{
	    	try {
	    		if ( toDelete.size() > 0 && toDelete != null){
	    			for (WhiteListUrl deleteURL : toDelete) {
	    				deleteURL.setDate_deleted(new Date());
	    				whiteListUrlDao.update(deleteURL);
	    			}

	    		}
				if ( toUpdate.size() > 0 && toUpdate != null){
					for (WhiteListUrl updateURL : toUpdate) {
						updateURL.setVersion(updateURL.getVersion()+1);
						updateURL.setDate_updated(new Date());
	    				whiteListUrlDao.update(updateURL);
	    			}
		    	}
				if ( toAdd.size() > 0 && toAdd != null){
					for (WhiteListUrl addURL : toAdd) {
						addURL.setVersion(0);
						addURL.setDate_created(new Date());
	    				whiteListUrlDao.save(addURL);
	    			}
				}
	    	} catch (Exception e) {
				e.printStackTrace();
				return false ;
			} 
	    	return true ;
	 }
}
