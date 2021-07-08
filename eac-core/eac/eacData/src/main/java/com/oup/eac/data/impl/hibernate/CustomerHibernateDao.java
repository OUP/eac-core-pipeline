package com.oup.eac.data.impl.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.User.UserType;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.CustomerSearchCriteria;

/**
 * The Customer dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="customerDao")
public class CustomerHibernateDao extends OUPHibernateBaseDao<Customer, String> implements CustomerDao {

    private static final int MAX_ALLOWED_PARAMS = 2100;  // SQL Server restriction

	/**
     * @param sf
     *            the session factory
     */
	@Autowired
    public CustomerHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	public final boolean hasCustomerEmailChanged(final String username, final String emailAddress) {
	    Integer count = (Integer)getSession().createSQLQuery("select count(*) from Customer c where c.username = :username and lower(c.email_address) = :emailAddress")
	                                    .setParameter("username", username)
	                                    .setParameter("emailAddress", emailAddress.toLowerCase())
	                                    .uniqueResult();
	    return count.intValue() == 0;
	}
	
    /**
     * @param erightsId
     *            the erights Id
     * @param customerType
     *            the customerType
     * @return the user
     */
    public final Customer getCustomerByErightsId(final Integer erightsId, final List<CustomerType> customerTypes) {
        return (Customer) getSession().createQuery("select u from Customer u " 
                                                    + "where u.erightsId = :erightsId "
                                                    + "and u.customerType in (:customerTypes)")
                                                    .setParameter("erightsId", erightsId)
                                                    .setParameterList("customerTypes", customerTypes)
                                                    .uniqueResult();
    }    
    
    /**
     * @param erightsId
     *            the erights Id
     * @param customerType
     *            the customerType
     * @return the user
     */
    public final Customer getCustomerWithAnswersByErightsId(final Integer erightsId) {
        return (Customer) getSession().createQuery("select u from Customer u " 
        											+ "left join fetch u.answers a " 
        											+ "left join fetch a.question q " 
        											+ "where u.erightsId = :erightsId "
        											)
        											.setParameter("erightsId", erightsId)
        											.uniqueResult();
    }

    /**
     * @param emailAddress
     *            the email address
     * @return the user
     */
    public final Customer getCustomerByEmailAddress(final String emailAddress) {
        return (Customer) getSession().createQuery("select u from Customer u " 
        											+ "where u.emailAddress = :emailAddress")
        											.setParameter("emailAddress", emailAddress)
        											.uniqueResult();
    }

    /**
     * @param username
     *            the username
     * @return the user
     */
    public final Customer getCustomerByUsername(final String username) {
        return (Customer) getSession().createSQLQuery("select * from customer c "
        							+ "where c.username = :username "
        							+ "and c.user_type = :userType")
									.addEntity(Customer.class)
									.setParameter("username", username)
									.setParameter("userType", UserType.CUSTOMER.toString())
									.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> getCustomersByProductOwner(final List<String> divisionTypes) {
        return getSession().createQuery("select distinct c from Customer c "
                                        + "join c.registrations r "
                                        + "join r.registrationDefinition rd "
                                        + "join rd.product pd "
                                        + "join pd.asset a " 
                                        + "where a.division.divisionType in (:divisionTypes)")
                                        .setParameterList("divisionTypes", divisionTypes)
                                        .list();
    }
    //As part of customer de-duplication
    @Override
	public int countSearchCustomers(CustomerSearchCriteria customerSearchCriteria, PagingCriteria pagingCriteria) {
    	String[] keywords = customerSearchCriteria.getRegistrationDataKeywords();
    	
    	List result = new ArrayList<>();
		for (int i = 0; i < keywords.length; i++) {
			String keyword = keywords[i];
			String hql = "select distinct answer.customerId from Answer answer where lower(answer.answerText) like :keyword order by answer.customerId";
			Query answerSearchQuery = getSession().createQuery(hql).setParameter("keyword", keyword.toLowerCase() + "%");
			answerSearchQuery.setMaxResults(MAX_ALLOWED_PARAMS);
			System.out.println("Count"+ result.size());
			if (i == 0) {
				result.addAll(answerSearchQuery.list());
			} else {
				result.retainAll(answerSearchQuery.list());
			}
		/*Query query = getQueryForCountSearchCustomers(customerSearchCriteria);
    	setQueryParamsForSearchCustomers(query, customerSearchCriteria, pagingCriteria);
    	*/
			}
    	
    	return result.size();
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Customer> searchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria) {

		Query query = getQueryForSearchCustomers(customerSearchCriteria, pagingCriteria);
		setQueryParamsForSearchCustomers(query, customerSearchCriteria, pagingCriteria);

        query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
        query.setMaxResults(pagingCriteria.getItemsPerPage());
        
		return query.list();
    }

	private Query getQueryForSearchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria) {
		final StringBuilder sb = new StringBuilder();
        
		sb.append("select distinct customer from Customer customer " +
				"left join customer.externalIds extid where 1=1 ");
		sb.append(getBaseQueryStringForSearchCustomers(customerSearchCriteria));
		sb.append(" order by customer.");
		
		if (pagingCriteria.getSortColumn() != null) {
			sb.append(pagingCriteria.getSortColumn());
		} else {
			sb.append("createdDate");
		}
		
		sb.append(" ");
		sb.append(pagingCriteria.getSortDirection());

		return getSession().createQuery(sb.toString());
	}
	
	private String getBaseQueryStringForSearchCustomers(final CustomerSearchCriteria customerSearchCriteria) {
		
		final StringBuilder sb = new StringBuilder();
		
		if (customerSearchCriteria.getUsername() != null) {
			sb.append("and customer.username like :username ");
        }
        
		if (customerSearchCriteria.getEmail() != null) {
			sb.append("and customer.emailAddress like :email ");
        }
        
		if (customerSearchCriteria.getFirstName() != null) {
			sb.append("and customer.firstName like :firstName ");
        }
        
		if (customerSearchCriteria.getFamilyName() != null) {
			sb.append("and customer.familyName like :familyName ");
        }
		
		if (customerSearchCriteria.getExternalId() != null) {
			sb.append("and extid.externalId like :externalId");
		}
        
		if (customerSearchCriteria.getCreatedDateFrom() != null && customerSearchCriteria.getCreatedDateTo() != null) {
			if(customerSearchCriteria.getCreatedDateFrom().isEqual(customerSearchCriteria.getCreatedDateTo())){
				sb.append("and (customer.createdDate >= :createdDateFrom) ");
				customerSearchCriteria.setCreatedDateTo(null);
			}else{
				DateTime createdToDate=customerSearchCriteria.getCreatedDateTo();
				customerSearchCriteria.setCreatedDateTo(createdToDate.plusHours(23));
			sb.append("and (customer.createdDate between :createdDateFrom and :createdDateTo) ");
			}
		} else if (customerSearchCriteria.getCreatedDateFrom() != null) {
			sb.append("and (customer.createdDate >= :createdDateFrom) ");
		} else if (customerSearchCriteria.getCreatedDateTo() != null) {
			sb.append("and (customer.createdDate <= :createdDateTo)");
        }

		if (ArrayUtils.isNotEmpty(customerSearchCriteria.getRegistrationDataKeywords())) {
			if (sb.toString().contains("and")) {
				sb.append(" or ");
			} else {
				sb.append(" and ");
			}
			sb.append("customer.id in (:customerIds)");
		}
		
		return sb.toString();
	}

	private void setQueryParamsForSearchCustomers(final Query query, final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria) {
		if (customerSearchCriteria.getUsername() != null) {
			query.setParameter("username", "%" + customerSearchCriteria.getUsername() + "%");
		}
		if (customerSearchCriteria.getEmail() != null) {
			query.setParameter("email", "%" + customerSearchCriteria.getEmail() + "%");
		}
		if (customerSearchCriteria.getFirstName() != null) {
			query.setParameter("firstName", "%" + customerSearchCriteria.getFirstName() + "%");
		}
		if (customerSearchCriteria.getFamilyName() != null) {
			query.setParameter("familyName", "%" + customerSearchCriteria.getFamilyName() + "%");
		}
		if (customerSearchCriteria.getExternalId() != null) {
			query.setParameter("externalId", "%" + customerSearchCriteria.getExternalId() + "%");
		}
		if (customerSearchCriteria.getCreatedDateFrom() != null) {
			query.setParameter("createdDateFrom", customerSearchCriteria.getCreatedDateFrom());
		}
		if (customerSearchCriteria.getCreatedDateTo() != null) {
			query.setParameter("createdDateTo", customerSearchCriteria.getCreatedDateTo());
		}
		if (ArrayUtils.isNotEmpty(customerSearchCriteria.getRegistrationDataKeywords())) {
			query.setParameterList("customerIds", getCustomerIdsByRegistrationData(customerSearchCriteria, pagingCriteria));
		}
	}
	
	private Query getQueryForCountSearchCustomers(final CustomerSearchCriteria customerSearchCriteria) {
		final StringBuilder sb = new StringBuilder();
        
		sb.append("select count(distinct customer) from Customer customer " +
					"where 1=1 ");
        
		sb.append(getBaseQueryStringForSearchCustomers(customerSearchCriteria));

		return getSession().createQuery(sb.toString());
	}

	@SuppressWarnings("unchecked")
	public Set<String> getCustomerIdsByRegistrationData(final CustomerSearchCriteria customerSearchCriteria, PagingCriteria pagingCriteria) {
		Set<String> customerIds = new HashSet<String>();
		String[] keywords = customerSearchCriteria.getRegistrationDataKeywords();
		for (int i = 0; i < keywords.length; i++) {
			String keyword = keywords[i];
			String hql = "select distinct answer.customerId from Answer answer where lower(answer.answerText) like :keyword order by answer.customerId";
			Query answerSearchQuery = getSession().createQuery(hql).setParameter("keyword", keyword.toLowerCase() + "%");
			answerSearchQuery.setMaxResults(MAX_ALLOWED_PARAMS);
			
			answerSearchQuery.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
			int pageNumber = ((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
			System.out.println("Search crieria : " + pageNumber);
			// criteria.setFirstResult((pageNumber - 1) * pageSize);
			List result = answerSearchQuery.list();
			if (i == 0) {
				customerIds.addAll(result);
			} else {
				customerIds.retainAll(result);
			}
		}
		if (customerIds.size() == 0) {
			customerIds.add("");
		}
		Set<String> custIds = new HashSet<String>();
		if(customerIds.size()>pagingCriteria.getItemsPerPage()){
			for(String custId : customerIds){
				if(custIds.size()<=pagingCriteria.getItemsPerPage())
					custIds.add(custId);
			}
		customerIds = custIds;
		}
		// Only the customer ids that have answers matching all
		// supplied keywords are returned.
		System.out.println(custIds);
		return customerIds;
	}

	@Override
	public List<String> getCustomerIdsByRegistrationDataSearch(final CustomerSearchCriteria customerSearchCriteria, PagingCriteria pagingCriteria) {
		List<String> customerIds = new ArrayList<String>();
		String[] keywords = customerSearchCriteria.getRegistrationDataKeywords();
		for (int i = 0; i < keywords.length; i++) {
			String keyword = keywords[i];
			String hql = "select distinct answer.customerId from Answer answer where lower(answer.answerText) like :keyword order by answer.customerId";
			Query answerSearchQuery = getSession().createQuery(hql).setParameter("keyword", keyword.toLowerCase() + "%");
			answerSearchQuery.setMaxResults(MAX_ALLOWED_PARAMS);
			List result = answerSearchQuery.list();
			if (i == 0 ) {
				customerIds = new ArrayList<String>(result) ;
			} else {
				customerIds.retainAll(result);
			}
		}
		return customerIds;
	}
	
	@Override
	public List<Integer> getCustomerIdListByCustomerIdListInSortedOrderOfDateCreated(final ArrayList<String> customerErightsId) {
		String ER_DB_NAME_WITH_SCHEMA = EACSettings.getProperty("rs.db.name") + "." + EACSettings.getProperty("rs.db.schema")  ;
		String query = "SELECT USER_ID FROM "+ ER_DB_NAME_WITH_SCHEMA+ ".ERS_USERS WHERE USER_ID IN (:ids) ORDER BY DATE_CREATED DESC" ;
		
		@SuppressWarnings("unchecked")
		List<Integer> customerErightsIdInSortedOrder = (List<Integer>)getSession()
	            .createSQLQuery(query)
	            .setParameterList("ids", customerErightsId)
	            .list();
		return customerErightsIdInSortedOrder ;
	}
	
    @Override
    public Customer getCustomerByExternalCustomerId(String systemId, String typeId, String externalId) {
        return (Customer) getSession().createQuery(
                "select c from Customer c " +
                " join fetch c.externalIds extid   " +                
                " join fetch extid.externalSystemIdType typeid   " +
                " join fetch typeid.externalSystem system   " +
                " where extid.class = com.oup.eac.domain.ExternalCustomerId" +
                " and   extid.externalId = :externalId" +
                " and   typeid.name = :typeId" +
                " and   system.name = :systemId")
                .setParameter("typeId", typeId)
                .setParameter("systemId", systemId)
                .setParameter("externalId", externalId)
                .uniqueResult();
    }

    @Override
    public void markCustomerAsUpdated(Customer customer) {
        String hql = "update Customer set updatedDate = :updatedDate where id = :id";
        Query query = getSession().createQuery(hql);
        java.sql.Date timestamp = new java.sql.Date(new DateTime().toDate().getTime());
        query.setTimestamp("updatedDate",timestamp);
        query.setString("id",customer.getId());
        query.executeUpdate();        
    }
    
    @Override
    /**
     * inefficent but it works
     */
    public void deleteCustomer(Customer customer) {

        getSession().evict(customer);// detach customer so that it's not
                                     // involved in transaction

        List<String> sqls = new ArrayList<String>();
        sqls.add("delete from Answer A where A.customer.id = :customerId");
        /*sqls.add("delete from ExternalCustomerId EXT where EXT.customer.id = :customerId");*/
        sqls.add("delete from LinkedRegistration LINK where LINK.id in (select LR.id from Registration R join R.linkedRegistrations as LR where R.customer.id = :customerId)");
        sqls.add("delete from Registration R where R.customer.id = :customerId");
        sqls.add("delete from Customer C where C.id = :customerId");

        for (String sql : sqls) {
            try {
                getSession().createQuery(sql).setParameter("customerId", customer.getId()).executeUpdate();
                getSession().flush();
            } catch (RuntimeException ex) {
                throw new RuntimeException(sql, ex);
            }
        }
    }
    
    @Override
    public Set<Answer> getAnswersByCustomerId(String customerId) {
        /*return (Customer) getSession().createQuery("select u from Customer u " 
                + "left join fetch u.answers a " 
                + "left join fetch a.question q " 
                + "where u.id = :id"
                )
                .setParameter("id", customerId)
                .uniqueResult();
    }*/
    	List<Answer> answers=  getSession().createQuery("select a from Answer a " 
                + "left join fetch a.question q " 
                + "where a.customerId = :customerId"
                )
                .setParameter("customerId", customerId).list() ;
    	
    	return new HashSet<Answer>(answers);
    }
    
    /**
     * This method will identify all the invalid email address.
     */
    @Override
    public List<Customer> getCustomerInvalidEmailAddress(){
    	List listOfCustomer=null;
    	listOfCustomer=getSession().createQuery("select u from Customer u where  patindex ('%[ &'',\":;!+=\\/()<>]%', u.emailAddress) >0 or patindex ('[@.-_]%', u.emailAddress) > 0  " +
    			"or patindex ('%[@.-_]', u.emailAddress) > 0  or u.emailAddress not like '%@%.%' or u.emailAddress like '%..%' or u.emailAddress like '%@%@%'  or u.emailAddress like '%.@%' or u.emailAddress like '%@.%' " +
    			"or u.emailAddress like '%.cm' or u.emailAddress like '%.co' or u.emailAddress like '%.or' or u.emailAddress like '%.ne' or u.emailAddress like '%.uk'   ").list();
    	
    	return listOfCustomer;
    }
}
