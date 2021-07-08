package com.oup.eac.data;

import java.io.Serializable;
import java.util.List;

import com.oup.eac.domain.BaseDomainObject;

/**
 * 
 * @author harlandd
 * 
 *         Generic DAO interface that all DAO interfaces should extend.
 * 
 * @param <T>
 *            the Class type
 * @param <ID>
 *            the ID
 */
public interface BaseDao<T extends BaseDomainObject, ID extends Serializable> {

    /**
     * @param id
     *            the id
     * @param lock
     *            the lock
     * @return entity T
     */
    T findById(ID id, boolean lock);

    /**
     * @param id
     *            the id
     * @param lock
     *            the lock
     * @return entity T
     */
    T getById(ID id, boolean lock);

    /**
     * @return all entities
     */
    List<T> findAll();

    /**
     * @param exampleInstance
     *            the example instance
     * @param excludeProperty
     *            array of exclude properties
     * @return entities by example
     */
    List<T> findByExample(T exampleInstance, String... excludeProperty);

    /**
     * @param entity
     *            the entity
     * @return entity
     */
    void makePersistent(T entity);

    /**
     * @param entity
     *            the entity
     */
    void makeTransient(T entity);

    /**
     * Flush the current session state.
     */
    void flush();

    /**
     * Clear the current session.
     */
    void clear();

    /**
     * @param id
     *            the id
     * @return the entity by id
     */
    T getEntity(String id);

    /**
     * @param id
     *            the id
     * @return the entity using criteria
     */
    T getEntityByCriteria(Long id);

    /**
     * @param obj
     *            merge this entity
     */
    T merge(T obj);

    /**
     * @param id
     *            the id
     * @return load the entity by id
     */
    T loadEntity(String id);

    /**
     * @param entity
     *            the entity
     * @return the saved or updated entity
     */
    void saveOrUpdate(T entity);

    /**
     * @param entity
     *            the entity
     * @return the saved entity
     */
    void save(T entity);

    /**
     * @param entity
     *            the entity
     * @return the updated entity
     */
    void update(T entity);

    /**
     * @param entity
     *            delete the entity
     */
    void delete(T entity);

    /**
     * Refresh.
     * 
     * @param entity
     * 			Refresh the entity.
     */
    public void refresh(T entity);
}
