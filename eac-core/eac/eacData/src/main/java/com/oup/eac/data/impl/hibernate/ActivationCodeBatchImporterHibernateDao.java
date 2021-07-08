package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ActivationCodeBatchImporterDao;
import com.oup.eac.domain.ActivationCodeBatchImporter;
import com.oup.eac.domain.ActivationCodeBatchImporterStatus.StatusCode;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchImporterSearchCriteria;

@Repository("activationCodeBatchImporterDao")
public class ActivationCodeBatchImporterHibernateDao extends HibernateBaseDao<ActivationCodeBatchImporter, String> implements ActivationCodeBatchImporterDao{

    private static Logger LOG = Logger.getLogger(ActivationCodeBatchImporterHibernateDao.class);
    
    @Autowired
    public ActivationCodeBatchImporterHibernateDao(SessionFactory sf) {
        super(sf);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<String> getUniqueActivationCodesForGrouping(StatusCode status) {
		
			return getSession().createQuery("select distinct acbi.code from ActivationCodeBatchImporter acbi "+
											"join acbi.activationCodeBatchImporterStatus acbie "+
											"where acbie.status = :status")
											.setParameter("status", status)
											.list();
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<String> getProductsForActivationCode(String code) {
		return getSession().createQuery("select distinct acbi.productName from ActivationCodeBatchImporter acbi "+
			 "where acbi.code = :code")
             .setParameter("code", code)
             .list();
				
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public List<ActivationCodeBatchImporter> getActivationCodeBatchImportorByCode(String code) {
		return getSession().createQuery("select acbi from ActivationCodeBatchImporter acbi "+
				"join fetch acbi.activationCodeBatchImporterStatus acbie "+
				 "where acbi.code = :code")
	             .setParameter("code", code)
	             .list();
			
	}
    
    @SuppressWarnings("unchecked")
    public List<ActivationCodeBatchImporter> getActivationCodes(ActivationCodeBatchImporterSearchCriteria searchCriteria, PagingCriteria pagingCriteria){
        Query query= getQueryForSearchActivationCode(searchCriteria);
        setQueryParamsForSearchActivationCodeBatch(query, searchCriteria);
        if(pagingCriteria!=null){
            query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
            query.setMaxResults(pagingCriteria.getItemsPerPage());
        }
        return query.list();
    }
    
    private Query getQueryForSearchActivationCode(final ActivationCodeBatchImporterSearchCriteria searchCriteria){
        StringBuilder builder = new StringBuilder("select aci from ActivationCodeBatchImporter aci ");
        builder.append("join fetch aci.activationCodeBatchImporterStatus acie ");
        builder.append(getBaseQueryStringForActivationCodeBatchImporter(searchCriteria));
        return getSession().createQuery(builder.toString());
    }
    
    private String getBaseQueryStringForActivationCodeBatchImporter(final ActivationCodeBatchImporterSearchCriteria searchCriteria){
        StringBuilder builder = new StringBuilder();
        builder.append("where 1=1 ");
        
        if  (StringUtils.isNotBlank(searchCriteria.getActivationCodeBatchImporterId())){
            builder.append("and acie.activationCodeBatchImporterId like :activationCodeBatchImporterId ");
        }
        
        if (StringUtils.isNotBlank(searchCriteria.getProductGroupName())) {
            builder.append("and acie.productGroupName like :productGroupName ");
        }
        
        if (searchCriteria.getStatues()!=null && searchCriteria.getStatues().size()>0) {
            builder.append("and acie.status in (:status) ");
        }
        
        if (StringUtils.isNotBlank(searchCriteria.getActivationCode())) {
            builder.append("and aci.code = :code ");
        }
        return builder.toString();
    }
    
    private void setQueryParamsForSearchActivationCodeBatch(final Query query, final ActivationCodeBatchImporterSearchCriteria searchCriteria) {
        
        if  (StringUtils.isNotBlank(searchCriteria.getActivationCodeBatchImporterId())){
            query.setParameter("activationCodeBatchImporterId", "%" + searchCriteria.getActivationCodeBatchImporterId() + "%");
        }
        
        if (StringUtils.isNotBlank(searchCriteria.getProductGroupName())) {
            query.setParameter("productGroupName", "%" + searchCriteria.getProductGroupName().toLowerCase() + "%");
        }
        
        if(searchCriteria.getStatues()!=null && searchCriteria.getStatues().size()>0) {
            query.setParameterList("status", searchCriteria.getStatues());
        }  
        
        if (StringUtils.isNotBlank(searchCriteria.getActivationCode())) {
            query.setParameter("code", searchCriteria.getActivationCode().toLowerCase());
        }
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<ActivationCodeBatchImporter> getActivationCodesImporters(ActivationCodeBatchImporterSearchCriteria searchCriteria,	PagingCriteria pagingCriteria) {
    	Query query= getQueryForSearchActivationCode(searchCriteria);
    	setQueryParamsForSearchActivationCodeBatch(query, searchCriteria);
    	if(pagingCriteria!=null){
    		query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
    		query.setMaxResults(pagingCriteria.getItemsPerPage());
    	}
    	return query.list();
	}

}
