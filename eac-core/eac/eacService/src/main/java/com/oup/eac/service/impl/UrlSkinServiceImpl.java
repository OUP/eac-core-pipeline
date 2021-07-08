package com.oup.eac.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.UrlSkinDao;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.UrlSkinService;

@Service("urlSkinService")
public class UrlSkinServiceImpl implements UrlSkinService {

	private final UrlSkinDao urlSkinDao;

	@Autowired
	public UrlSkinServiceImpl(final UrlSkinDao urlSkinDao) {
		this.urlSkinDao = urlSkinDao;
	}

	@Override
	public UrlSkin getUrlSkinById(final String id) {
		return urlSkinDao.findById(id, true);
	}

	@Override
	public List<UrlSkin> getAllUrlSkinsOrderedBySiteName() {
		List<UrlSkin> urlSkins = urlSkinDao.findAll();
		Collections.sort(urlSkins, new Comparator<UrlSkin>() {
			@Override
			public int compare(UrlSkin o1, UrlSkin o2) {
				return o1.getSiteName().compareTo(o2.getSiteName());
			}
		});
		return urlSkins;
	}

	@Override
	public void saveUrlSkin(final UrlSkin urlSkin) {
		urlSkinDao.saveOrUpdate(urlSkin);
		AuditLogger.logEvent("Saved Skin", "Id:"+urlSkin.getId(), AuditLogger.urlSkin(urlSkin));
	}

	@Override
	public void deleteUrlSkin(final UrlSkin urlSkin) {
		urlSkinDao.delete(urlSkin);
		AuditLogger.logEvent("Deleted Skin", "Id:"+urlSkin.getId(), AuditLogger.urlSkin(urlSkin));
	}

}
