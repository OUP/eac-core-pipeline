package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.EacGroupsDao;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EacGroupsSearchCriteria;

@Repository("eacGroupsDao")
public class EacGroupsHibernateDao extends HibernateBaseDao<EacGroups, String> implements EacGroupsDao{

	private static Logger LOG = Logger.getLogger(EacGroupsHibernateDao.class);
	
	@Autowired
	public EacGroupsHibernateDao(SessionFactory sf) {
		super(sf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EacGroups> searchProductGroups(EacGroupsSearchCriteria searchCriteria, PagingCriteria pagingCriteria) {
		Query query= getQueryForSearchProductGroups(searchCriteria);
		setQueryParamsForSearchProductGroups(query, searchCriteria);
		query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
	    query.setMaxResults(pagingCriteria.getItemsPerPage());
	    
		return query.list();
	}

	@Override
	public int countSearchProductGroups(EacGroupsSearchCriteria searchCriteria) {
		Query query = getQueryForCountProductGroups(searchCriteria);
		setQueryParamsForSearchProductGroups(query, searchCriteria);
    	
    	Long count = (Long) query.uniqueResult();

    	return count.intValue();
	}

	private Query getQueryForSearchProductGroups(final EacGroupsSearchCriteria searchCriteria){
		StringBuilder builder = new StringBuilder("select distinct eg from EacGroups eg ");
		builder.append("join fetch eg.products pr ");
		builder.append("left join fetch pr.externalIds extIds ");
		builder.append(getBaseQueryStringForSearchProductGroups(searchCriteria));
		builder.append("order by eg.groupName asc");
		return getSession().createQuery(builder.toString());
	}
	
	private Query getQueryForCountProductGroups(final EacGroupsSearchCriteria searchCriteria){
		StringBuilder builder = new StringBuilder("select count(distinct eg) from EacGroups eg ");
		builder.append("join eg.products pr ");
		builder.append("left join pr.externalIds extIds ");
		builder.append(getBaseQueryStringForSearchProductGroups(searchCriteria));
		return getSession().createQuery(builder.toString());
	}
	
	private String getBaseQueryStringForSearchProductGroups(final EacGroupsSearchCriteria searchCriteria){
		StringBuilder builder = new StringBuilder();
		builder.append("where 1=1 ");
		
		if	(StringUtils.isNotBlank(searchCriteria.getGroupName())){
			builder.append("and lower(eg.groupName) like :groupName ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getProductName())) {
			builder.append("and lower(pr.productName) like :productName ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getProductId())) {
			builder.append("and lower(pr.id) like :productId ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getExternalId())) {
			builder.append("and lower(extIds.externalId) like :externalId ");
		}
		return builder.toString();
	}
	
	private void setQueryParamsForSearchProductGroups(final Query query, final EacGroupsSearchCriteria searchCriteria) {
		
		if	(StringUtils.isNotBlank(searchCriteria.getGroupName())){
			query.setParameter("groupName", "%" + searchCriteria.getGroupName().toLowerCase() + "%");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getProductName())) {
    		query.setParameter("productName", "%" + searchCriteria.getProductName().toLowerCase() + "%");
    	}
    	
		if (StringUtils.isNotBlank(searchCriteria.getProductId())) {
			query.setParameter("productId", "%" +  searchCriteria.getProductId().toLowerCase() + "%");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getExternalId())) {
			query.setParameter("externalId", "%" + searchCriteria.getExternalId().toLowerCase() + "%");
		}
		
	}

	@Override
	public EacGroups getEacGroupById(String id) {
		return (EacGroups)getSession().createQuery("select eg from EacGroups eg "+
													"join fetch eg.products pr "+
													"where eg.id = :id")
													.setParameter("id", id)
													.uniqueResult();
	}

	@Override
	public boolean isEacGroupUsed(String id) {
		boolean used = false;
		
             //If activationCodeBatch  is created that means Product Group is used.
             long countRegistrationsDefinition = (Long) getSession()
            		 .createQuery("select count(*) from ActivationCodeBatch acb " +
            		 			"join acb.activationCodeRegistrationDefinition rd " +
            		 			"where rd.eacGroup.id = :eacGroupId")
            		 .setParameter("eacGroupId", id).uniqueResult();
             used = countRegistrationsDefinition > 0;
  
		return used;
	}

	@Override
	public EacGroups getEacGroupByName(String name) {
		return (EacGroups)getSession().createQuery("select eg from EacGroups eg "+
				"where eg.groupName = :name")
				.setParameter("name", name)
				.uniqueResult();
	}

	@Override
	public boolean deleteRegistrationDefinitionOfEacGroup(String id) {
		 boolean result = false;
		 try {
			 int deleted = getSession().createQuery("delete from RegistrationDefinition rd where rd.eacGroup.id = :eacGroupId").setParameter("eacGroupId", id).executeUpdate();
			 result = deleted >= 0;
		 } catch (HibernateException ex) {
			 String msg = String.format("unexpected exception attempting to delete unused product group " + id);
			 LOG.error(msg,ex);
		 }	 
		return result;
	}

	@Override
    public String getEacGroupIdByName(String name) {
        return (String)getSession().createQuery("select eg.id from EacGroups eg "+
                "where eg.groupName = :name")
                .setParameter("name", name)
                .uniqueResult();
    }
}
