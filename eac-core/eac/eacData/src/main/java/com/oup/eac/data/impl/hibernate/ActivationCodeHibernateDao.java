package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.ActivationCodeDao;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeState;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeReportDto;
import com.oup.eac.dto.ActivationCodeSearchCriteria;

/**
 * Load activation codes. 
 * 
 * @author Ian Packard
 *
 */
@Repository(value="activationCodeDao")
public class ActivationCodeHibernateDao extends HibernateBaseDao<ActivationCode, String> implements ActivationCodeDao {

	@Autowired
    public ActivationCodeHibernateDao(SessionFactory sf) {
        super(sf);
    }
    
	@SuppressWarnings("unchecked")
    public final List<ActivationCode> getActivationCodeByBatch(ActivationCodeBatch activationCodeBatch) {
    	return getSession().createQuery("select ac from ActivationCode ac " +
    									"where ac.activationCodeBatch = :activationCodeBatch")
    									.setParameter("activationCodeBatch", activationCodeBatch)
    									.list();
    }
	
	public final ActivationCode getActivationCodeAndBatchBy(final ActivationCodeRegistration activationCodeRegistration) {
	    return (ActivationCode) getSession().createQuery("select ac from ActivationCode ac " +
	    		                                         "join ac.registrations r " +
	    		                                         "join fetch ac.activationCodeBatch acb " +
	    		                                         "where r = :registration")
	    		                                         .setParameter("registration", activationCodeRegistration)
	    		                                         .uniqueResult();
	}

    public final ActivationCode getActivationCodeByCode(final String code) {
        return (ActivationCode) getSession().createQuery("select ac from ActivationCode ac " +
        		"join fetch ac.activationCodeBatch acb " +
        		"where ac.code = :code")
        		.setParameter("code", code)
        		.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public final List<ActivationCode> searchActivationCodeByCode(final String code, final boolean likeSearch) {
    	StringBuilder hqlQuery = buildQuery(likeSearch);
        Query query = setParameters(code, likeSearch, hqlQuery);
        return query.setMaxResults(EACSettings.getIntProperty("activationCode.search.maxResults")).list();
    }

	private Query setParameters(final String code, final boolean likeSearch, final StringBuilder hqlQuery) {
		Query query = getSession().createQuery(hqlQuery.toString());
    	if(likeSearch) {
    		query.setParameter("code", code + '%');
    	} else {
    		query.setParameter("code", code);
    	}
		return query;
	}

	private StringBuilder buildQuery(final boolean likeSearch) {
		StringBuilder hqlQuery = new StringBuilder("select ac from ActivationCode ac " +
													"join fetch ac.activationCodeBatch acb " +
													"join fetch acb.activationCodeRegistrationDefinition acrd " +
													"join fetch acrd.product p ");
    	if(likeSearch) {
    		hqlQuery.append("where ac.code like :code ");
    	} else {
    		hqlQuery.append("where ac.code = :code ");
    	}
    	hqlQuery.append("order by ac.code");
		return hqlQuery;
	}
    
    public final ActivationCode getActivationCodeAndDefinitionByCode(final String code) {
    	
        return (ActivationCode) getSession().createQuery("select ac from ActivationCode ac " +
                "join fetch ac.activationCodeBatch acb " +
                "join fetch acb.activationCodeRegistrationDefinition acrd " +
                "join fetch acrd.product pd " +
                "join fetch acrd.licenceTemplate lt " +
                "join fetch acrd.registrationActivation la " +
        		"where ac.code = :code")
        		.setParameter("code", code)
        		.uniqueResult();
    }
    
    public final ActivationCode getActivationCodeByCodeAndBatch(final String code, final ActivationCodeBatch activationCodeBatch) {
        return (ActivationCode) getSession().createQuery("select ac from ActivationCode ac " +                
        		"where ac.code = :code " +
        		"and ac.activationCodeBatch = :activationCodeBatch")
        		.setParameter("code", code)
        		.setParameter("activationCodeBatch", activationCodeBatch)
        		.uniqueResult();
    }
    
    public final boolean checkActivationCodeIsUnique(final String code) {
        
        boolean noCurrentCode = getSession().createQuery("select ac.code from ActivationCode ac " +
                "where ac.code = :code")
                .setParameter("code", code)
                .list().size() == 0;
        
        if ( ! noCurrentCode) {
            return false;
        }
        int pastCodeCount = (Integer)getSession().createSQLQuery("select count(*) from arc_activation_code where code = :code").setParameter("code", code).uniqueResult();
        boolean noPastCode = pastCodeCount == 0;
        return noPastCode;
    }

    @Override
    public ActivationCode getActivationCodeAndProductByCode(String activationCode) {
        ActivationCode result = (ActivationCode) getSession().createQuery("select distinct ac from ActivationCode ac " +
                "join fetch ac.activationCodeBatch acb " +
                "join fetch acb.activationCodeRegistrationDefinition acrd " +
                "join fetch acrd.product pd " +
                "where ac.code = :code")
        .setParameter("code", activationCode)
        .uniqueResult();
        return result;
    }
    
    public final ActivationCode getActivationCodeWithDetails(final String id) {
    	return (ActivationCode)getSession().createQuery("select ac from ActivationCode ac " +
    									"join fetch ac.activationCodeBatch acb " +
    									"join fetch acb.activationCodeRegistrationDefinition acrd " +
    									"left join fetch acrd.eacGroup eg " +
    									"left join fetch acrd.product p " +
    									"left join fetch ac.registrations r " +
    									"left join fetch r.customer c " +
    									"where ac.id = :id")
    									.setParameter("id", id)
    									.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<ActivationCodeReportDto> getActivationCodeSearch(ActivationCodeSearchCriteria criteria, PagingCriteria pagingCriteria, AdminUser adminUser) {
        Query query = getSession().createQuery(buildQuery(criteria, false,adminUser));
        addParameters(query, criteria,adminUser);
        return query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage())
                    .setMaxResults(pagingCriteria.getItemsPerPage())
                    .list();
    }
    
    public Long getActivationCodeReportCount(ActivationCodeSearchCriteria criteria, AdminUser adminUser) {
        Query query = getSession().createQuery(buildQuery(criteria, true, adminUser));
        addParameters(query, criteria, adminUser);
        return (Long)query.uniqueResult();
    }
    
    private void addParameters(final Query query, final ActivationCodeSearchCriteria criteria, AdminUser adminUser) {
        query.setParameter("adminUser", adminUser.getId());
        if(StringUtils.isNotBlank(criteria.getCode())) {
            query.setParameter("code", criteria.getCode());
        }
        if(StringUtils.isNotBlank(criteria.getProductId())) {
            query.setParameter("productId", criteria.getProductId());
        }
        
        if(StringUtils.isNotBlank(criteria.getEacGroupId())) {
            query.setParameter("eacGroupId", criteria.getEacGroupId());
        }
    }
    
    private String buildQuery(ActivationCodeSearchCriteria criteria, boolean count, AdminUser adminUser) {
        StringBuilder strQuery = new StringBuilder();
        if(count) {
            strQuery.append("select count(distinct ac) from ActivationCode ac ");
        } else {
            strQuery.append("select distinct new com.oup.eac.dto.ActivationCodeReportDto(ac.id, ac.code, p.productName, ac.allowedUsage, ac.actualUsage, eg.groupName ) " +
            		"from ActivationCode ac ");
        }
        strQuery.append("join ac.activationCodeBatch acb ");
        strQuery.append("join acb.activationCodeRegistrationDefinition rd ");
        strQuery.append("left join rd.eacGroup eg ");
        strQuery.append("left join rd.product p ");
        strQuery.append("left join p.division div " );
        strQuery.append("left join div.divisionAdminUsers dad ");
        strQuery.append("left join dad.adminUser duser ");

        strQuery.append(buildWhereClause(criteria));
        /*if(!count) {
            strQuery.append("order by ac.code ");
        }*/
        return strQuery.toString();
    }
    
    private String buildWhereClause(ActivationCodeSearchCriteria criteria) {
        StringBuilder whereClause = new StringBuilder();
        addWhereClause(whereClause, "coalesce(duser.id,:adminUser) = :adminUser ");
        if(StringUtils.isNotBlank(criteria.getCode())) {
            addWhereClause(whereClause, "ac.code = :code ");
        }
        if(StringUtils.isNotBlank(criteria.getProductId())) {
            addWhereClause(whereClause, "p.id = :productId ");
        }
        
        if(StringUtils.isNotBlank(criteria.getEacGroupId())) {
            addWhereClause(whereClause, "eg.id = :eacGroupId ");
        }
        
        if(criteria.getActivationCodeState() != ActivationCodeState.ALL) {
            if(criteria.getActivationCodeState() == ActivationCodeState.AVAILABLE) {
                addWhereClause(whereClause, "ac.allowedUsage > ac.actualUsage ");
            } else {
                addWhereClause(whereClause, "ac.allowedUsage = ac.actualUsage ");
            }
        }
        return whereClause.toString();
    }
    
    private void addWhereClause(StringBuilder whereClauseBuilder, String whereClause) {
        if(whereClauseBuilder.length() > 0) {
            whereClauseBuilder.append("and ");
        } else {
            whereClauseBuilder.append("where ");
        }
        whereClauseBuilder.append(whereClause);
    }

	@Override
	public ActivationCode getActivationCodeAndProductAndEacGroupByCode(String activationCode) {
		  ActivationCode result = (ActivationCode) getSession().createQuery("select distinct ac from ActivationCode ac " +
	                "join fetch ac.activationCodeBatch acb " +
	                "join fetch acb.activationCodeRegistrationDefinition acrd " +
	                "left join fetch acrd.eacGroup eg " +
	                "left join fetch acrd.product pd " +
	                "where ac.code = :code")
	        .setParameter("code", activationCode)
	        .uniqueResult();
	        return result;
	}

	@Override
	public ActivationCode getActivationCodeAndDefinitionByCodeForEacGroup(String code) {
		return (ActivationCode) getSession().createQuery("select ac from ActivationCode ac " +
                "join fetch ac.activationCodeBatch acb " +
                "join fetch acb.activationCodeRegistrationDefinition acrd " +
                "join fetch acrd.eacGroup eg " +
                "join fetch eg.products pd " +
                "join fetch acrd.licenceTemplate lt " +
                "join fetch acrd.registrationActivation la " +
        		"where ac.code = :code")
        		.setParameter("code", code)
        		.uniqueResult();
	}

	@Override
	public Session getSessionFromDao() {
		return getSession();
	}

	@Override
	public ActivationCode getActivationCodeFullDetails(String activationCode) {
		ActivationCode result = (ActivationCode) getSession().createQuery("select distinct ac from ActivationCode ac " +
                "join fetch ac.activationCodeBatch acb " +
                "join fetch acb.activationCodeRegistrationDefinition acrd " +
                "left join fetch acrd.eacGroup eg " +
                "left join fetch acrd.product pd " +
                "join fetch acrd.licenceTemplate lt " +
                "join fetch acrd.registrationActivation la " +
                "where ac.code = :code")
        .setParameter("code", activationCode)
        .uniqueResult();
        return result;
	}

}
