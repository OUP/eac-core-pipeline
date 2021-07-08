package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ExternalSystemIdTypeDao;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;

/**
 * The Class ExternalSystemIdTypeHibernateDao.
 * 
 * @author David Hay
 * @author Ian Packard
 */
@Repository(value="externalSystemIdTypeDao")
public class ExternalSystemIdTypeHibernateDao extends HibernateBaseDao<ExternalSystemIdType, String> implements ExternalSystemIdTypeDao {

	@Autowired
    public ExternalSystemIdTypeHibernateDao(SessionFactory sf) {
        super(sf);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ExternalSystemIdType> findByExternalSystem(ExternalSystem exSys) {
         Query q = getSession().createQuery(
                "select exSysIdType from ExternalSystemIdType exSysIdType " +
                "where  exSysIdType.externalSystem = :exSys");
         q.setParameter("exSys", exSys);
         List<ExternalSystemIdType> result = q.list();
         return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ExternalSystemIdType> findByExternalSystemOrderedByName(ExternalSystem exSys) {
       /*  Query q = getSession().createQuery(
                "select exSysIdType from ExternalSystemIdType exSysIdType " +
                "where  exSysIdType.externalSystem = :exSys order by exSysIdType.name");
         q.setParameter("exSys", exSys);*/
         List<ExternalSystemIdType> result = null ;// q.list();
         return result;
    }

    @Override
    public ExternalSystemIdType findByExternalSystemAndName(ExternalSystem exSys, String name) {        
        if(exSys == null){
            return null;
        }
        Query q = getSession().createQuery(
                " select e from ExternalSystemIdType e " +
                " where  e.externalSystem.id = :exSysId "  +
                " and    e.name = :name");
         q.setParameter("exSysId", exSys.getId());
         q.setParameter("name", name);
         ExternalSystemIdType result = (ExternalSystemIdType) q.uniqueResult();
         return result;
    }
    
    @Override
    public ExternalSystemIdType findByExternalSystemNameAndTypeName(String externalSystemName, String typeName) {
        Query q = getSession().createQuery(
                " select e from ExternalSystemIdType e " +
                " where  e.externalSystem.name = :externalSystemName " +
                " and    e.name = :typeName");
         q.setParameter("externalSystemName", externalSystemName);
         q.setParameter("typeName", typeName);
         ExternalSystemIdType result = (ExternalSystemIdType) q.uniqueResult();
         return result;
    }

}
