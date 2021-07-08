package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.AdminAccountSearchDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.AdminAccountSearchBean;


@Repository(value="adminAccountDao")
public class AdminAccountSearchHibernateDao extends
		OUPHibernateBaseDao<AdminUser, String> implements AdminAccountSearchDao {

	@Autowired
	public AdminAccountSearchHibernateDao(final SessionFactory sf) {
		super(sf);
	}

	public List<AdminUser> searchCustomers(
			final AdminAccountSearchBean customerSearchCriteria,
			final PagingCriteria pagingCriteria) {

		Query query = getQueryForSearchCustomers(customerSearchCriteria,
				pagingCriteria);
		setQueryParamsForSearchCustomers(query, customerSearchCriteria);

		//query.setFirstResult((pagingCriteria.getRequestedPage() - 1)				* pagingCriteria.getItemsPerPage());
		//query.setMaxResults(pagingCriteria.getItemsPerPage());

		return query.list();
	}

	private Query getQueryForSearchCustomers(
			final AdminAccountSearchBean customerSearchCriteria,
			final PagingCriteria pagingCriteria) {
		final StringBuilder sb = new StringBuilder();

		sb.append("select distinct customer from AdminUser customer where 1=1 ");
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

	private void setQueryParamsForSearchCustomers(final Query query,
			final AdminAccountSearchBean customerSearchCriteria) {
		if (customerSearchCriteria.getUserName() != null) {
			query.setParameter("username",
					"%" + customerSearchCriteria.getUserName() + "%");
		}
		if (customerSearchCriteria.getEmailAddress() != null) {
			query.setParameter("email",
					"%" + customerSearchCriteria.getEmailAddress() + "%");
		}
		if (customerSearchCriteria.getFirstName() != null) {
			query.setParameter("firstName",
					"%" + customerSearchCriteria.getFirstName() + "%");
		}
		if (customerSearchCriteria.getFamilyName() != null) {
			query.setParameter("familyName",
					"%" + customerSearchCriteria.getFamilyName() + "%");
		}
	}

	private String getBaseQueryStringForSearchCustomers(
			final AdminAccountSearchBean customerSearchCriteria) {

		final StringBuilder sb = new StringBuilder();
		if (customerSearchCriteria.getUserName() != null) {
			sb.append("and customer.username like :username ");
		}

		if (customerSearchCriteria.getEmailAddress() != null) {
			sb.append("and customer.emailAddress like :email ");
		}

		if (customerSearchCriteria.getFirstName() != null) {
			sb.append("and customer.firstName like :firstName ");
		}

		if (customerSearchCriteria.getFamilyName() != null) {
			sb.append("and customer.familyName like :familyName ");
		}
		return sb.toString();
	}

	@Override
	public int countSearchCustomers(
			AdminAccountSearchBean customerSearchCriteria) {

		Query query = getQueryForCountSearchCustomers(customerSearchCriteria);
		setQueryParamsForSearchCustomers(query, customerSearchCriteria);

		Long count = (Long) query.uniqueResult();

		return count.intValue();
	}

	private Query getQueryForCountSearchCustomers(
			final AdminAccountSearchBean customerSearchCriteria) {
		final StringBuilder sb = new StringBuilder();

		sb.append("select count(distinct customer) from AdminUser customer "
				+ " where 1=1 ");

		sb.append(getBaseQueryStringForSearchCustomers(customerSearchCriteria));

		return getSession().createQuery(sb.toString());
	}
	

}
