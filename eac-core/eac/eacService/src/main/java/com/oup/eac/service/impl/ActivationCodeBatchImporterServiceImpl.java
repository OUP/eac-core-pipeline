package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.ActivationCodeBatchImporterDao;
import com.oup.eac.data.impl.hibernate.ActivationCodeBatchImporterHibernateDao;
import com.oup.eac.domain.ActivationCodeBatchImporter;
import com.oup.eac.domain.ActivationCodeBatchImporterStatus;
import com.oup.eac.domain.ActivationCodeBatchImporterStatus.StatusCode;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchImporterSearchCriteria;
import com.oup.eac.service.ActivationCodeBatchImporterService;
import com.oup.eac.service.ServiceLayerException;

@Service(value="activationCodeBatchImporterService")
public class ActivationCodeBatchImporterServiceImpl implements
        ActivationCodeBatchImporterService {
    
    private static Logger LOG = Logger.getLogger(ActivationCodeBatchImporterHibernateDao.class);
    
    @Autowired
    private ActivationCodeBatchImporterDao activationCodeBatchImporterDao;

    @Override
    public boolean importActivationCodeBatch(final ActivationCodeBatchImporter obj) throws ServiceLayerException {
        // TODO Auto-generated method stub
        boolean retVal = false;
        try{
                if(obj!=null){
                    
                    //activationCodeBatchImporterDao.saveActivationCodeBatch(obj);
                    activationCodeBatchImporterDao.saveOrUpdate(obj);
                    activationCodeBatchImporterDao.flush();
                    LOG.debug("ActivationCodeBatchImporterServiceImpl :: code processed .. " + obj.getCode());
                    retVal = true;
                }else{
                    retVal = false;
                }
        }catch(Exception ex){
            ex.printStackTrace();
            retVal = false;
        }
        return retVal;
    }

	@Override
	public List<String> getUniqueActivationCodesForGrouping(StatusCode status) throws ServiceLayerException{
		return activationCodeBatchImporterDao.getUniqueActivationCodesForGrouping(status);
	}

	@Override
	public List<String> getProductsForActivationCode(String code) throws ServiceLayerException{
		return activationCodeBatchImporterDao.getProductsForActivationCode(code);
	}

	@Override
	public boolean updateStatusofCodeForGroup(String code, String eacGroupId, StatusCode statusCode) throws ServiceLayerException{
		List<ActivationCodeBatchImporter> acbi = activationCodeBatchImporterDao.getActivationCodeBatchImportorByCode(code);
		for (ActivationCodeBatchImporter activationCodeBatchImporter : acbi) {
			activationCodeBatchImporter.getActivationCodeBatchImporterStatus().setProductGroupId(eacGroupId);
			activationCodeBatchImporter.getActivationCodeBatchImporterStatus().setStatus(statusCode);
			activationCodeBatchImporterDao.update(activationCodeBatchImporter);
		}
		
		return true;
	}
	
	public List<ActivationCodeBatchImporter> getActivationCodeBatchList() throws ServiceLayerException{
        ActivationCodeBatchImporterSearchCriteria searchCriteria = new ActivationCodeBatchImporterSearchCriteria();
        
        List<StatusCode> statues = new ArrayList<ActivationCodeBatchImporterStatus.StatusCode>();
        statues.add(StatusCode.PRODUCT_GROUP_CREATED);
        
        searchCriteria.setStatues(statues);
        List<ActivationCodeBatchImporter> retList =  activationCodeBatchImporterDao.getActivationCodes(searchCriteria, null);
        return retList;
    }

	@Override
	public List<ActivationCodeBatchImporter> getActivationCodesImporters(ActivationCodeBatchImporterSearchCriteria searchCriteria,
			PagingCriteria pagingCriteria) {
		 List<ActivationCodeBatchImporter> retList =  activationCodeBatchImporterDao.getActivationCodesImporters(searchCriteria, pagingCriteria);
	        return retList;
	}

	@Override
	public boolean updateStatus(ActivationCodeBatchImporter obj,
			StatusCode newStatusCode, StatusCode currentStatus) {
		boolean retVal = false;
        if(obj==null)
            retVal = false;
        
        try
        {
            if(currentStatus!=null && !obj.getActivationCodeBatchImporterStatus().getStatus().toString().equalsIgnoreCase(currentStatus.toString()))
            {
                retVal = false;
            }
            else
            {
                obj.getActivationCodeBatchImporterStatus().setStatus(newStatusCode);
                activationCodeBatchImporterDao.update(obj);
                retVal = true;
            }
        }catch(Exception ex){
            retVal = false;
            ex.printStackTrace();
        }
        
        return retVal;
	}

	
}
