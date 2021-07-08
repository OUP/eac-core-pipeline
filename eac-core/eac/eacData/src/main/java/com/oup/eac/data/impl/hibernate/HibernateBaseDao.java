package com.oup.eac.data.impl.hibernate;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.oup.eac.data.BaseDao;
import com.oup.eac.domain.BaseDomainObject;

/**
 * Generic DAO abstract class that all DAO's should extend.
 * 
 * @author harlandd
 * 
 * @param <T>
 *            the Class type
 * @param <ID>
 *            the ID
 */
public abstract class HibernateBaseDao<T extends BaseDomainObject, ID extends Serializable>  implements BaseDao<T, ID> {

    private Class<T> persistentClass;
    private SessionFactory sessionFactory;

    /**
     * @param sf
     *            the session factory
     */
    @SuppressWarnings("unchecked")
    public HibernateBaseDao(final SessionFactory sf) {
        this.sessionFactory = sf;
        Class clazz = getClass();
        while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        List<Class<?>> types = getTypeArguments(HibernateBaseDao.class, this.getClass());                               
        this.persistentClass = (Class<T>)types.get(0);

    }
    
    /**
     * Get the actual type arguments a child class has used to extend a generic base class.
     *
     * @param baseClass the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    @SuppressWarnings("unchecked")
	public static <T> List<Class<?>> getTypeArguments(
      Class<T> baseClass, Class<? extends T> childClass) {
      Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
     Type type = childClass;
      // start walking up the inheritance hierarchy until we hit baseClass
      while (! getClass(type).equals(baseClass)) {
        if (type instanceof Class) {
          // there is no useful information for us in raw types, so just keep going.
          type = ((Class) type).getGenericSuperclass();
        }
        else {
          ParameterizedType parameterizedType = (ParameterizedType) type;
          Class<?> rawType = (Class) parameterizedType.getRawType();
    
          Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
          TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
          for (int i = 0; i < actualTypeArguments.length; i++) {
            resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
          }
    
          if (!rawType.equals(baseClass)) {
            type = rawType.getGenericSuperclass();
          }
        }
      }
    
      // finally, for each actual type argument provided to baseClass, determine (if possible)
      // the raw class for that type argument.
      Type[] actualTypeArguments;
      if (type instanceof Class) {
        actualTypeArguments = ((Class) type).getTypeParameters();
      }
      else {
        actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
      }
      List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
      // resolve types by chasing down type variables.
      for (Type baseType: actualTypeArguments) {
        while (resolvedTypes.containsKey(baseType)) {
          baseType = resolvedTypes.get(baseType);
        }
        typeArgumentsAsClasses.add(getClass(baseType));
      }
      return typeArgumentsAsClasses;
    }
    
    /**
     * Get the underlying class for a type, or null if the type is a variable type.
     * @param type the type
     * @return the underlying class
     */
    @SuppressWarnings("unchecked")
	public static Class<?> getClass(Type type) {
      if (type instanceof Class) {
        return (Class) type;
      }
      else if (type instanceof ParameterizedType) {
        return getClass(((ParameterizedType) type).getRawType());
      }
      else if (type instanceof GenericArrayType) {
        Type componentType = ((GenericArrayType) type).getGenericComponentType();
        Class<?> componentClass = getClass(componentType);
        if (componentClass != null ) {
          return Array.newInstance(componentClass, 0).getClass();
        }
        else {
          return null;
        }
      }
      else {
        return null;
      }
    }

    
    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    /**
     * @return the persistent class
     */
    public final Class<T> getPersistentClass() {
        return persistentClass;
    }

    /**
     * @param id
     *            the id
     * @param lock
     *            the lock
     * @return entity T
     */
    @SuppressWarnings("unchecked")
    public final T findById(final ID id, final boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
        } else {
            entity = (T) getSession().load(getPersistentClass(), id);
        }
        return entity;
    }

    /**
     * @param id
     *            the id
     * @param lock
     *            the lock
     * @return entity T
     */
    @SuppressWarnings("unchecked")
    public final T getById(final ID id, final boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().get(getPersistentClass(), id, LockOptions.UPGRADE);
        } else {
            entity = (T) getSession().get(getPersistentClass(), id);
        }
        return entity;
    }

    /**
     * @return all entities
     */
    public final List<T> findAll() {
        return findByCriteria();
    }

    /**
     * @param exampleInstance
     *            the example instance
     * @param excludeProperty
     *            array of exclude properties
     * @return entities by example
     */
    @SuppressWarnings("unchecked")
    public final List<T> findByExample(final T exampleInstance, final String... excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    /**
     * @param entity
     *            the entity
     * @return entity
     */
    public final void makePersistent(final T entity) {
        getSession().saveOrUpdate(entity);
    }

    /**
     * @param entity
     *            the entity
     */
    public final void makeTransient(final T entity) {
        getSession().delete(entity);
    }

    /**
     * Flush the current session state.
     */
    public final void flush() {
        getSession().flush();
    }

    /**
     * Clear the current session.
     */
    public final void clear() {
        getSession().clear();
    }

    /**
     * @param criterion
     *            the criterion
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
    protected final List<T> findByCriteria(final Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    /**
     * @param id
     *            the id
     * @return entity
     */
    @SuppressWarnings("unchecked")
    public final T getEntity(final String id) {
        return (T) getSession().get(persistentClass, id);
    }

    /**
     * @param id
     *            the id
     * @return the entity using criteria
     */
    @SuppressWarnings("unchecked")
    public final T getEntityByCriteria(final Long id) {
        return (T) getSession().createCriteria(persistentClass).add(Restrictions.eq("id", id)).uniqueResult();
    }

    /**
     * @param obj
     *            merge this entity
     */
    @SuppressWarnings("unchecked")
	public final T merge(final T obj) {
        return (T )getSession().merge(obj);
    }

    /**
     * @param id
     *            the id
     * @return load the entity by id
     */
    @SuppressWarnings("unchecked")
    public final T loadEntity(final String id) {
        return (T) getSession().load(persistentClass, id);
    }

    /**
     * @param entity
     *            the entity
     * @return the saved or updated entity
     */
    public final void saveOrUpdate(final T entity) {
        getSession().saveOrUpdate(entity);
    }

    /**
     * @param entity
     *            the entity
     * @return the saved entity
     */
    public final void save(final T entity) {
        getSession().save(entity);
    }

    /**
     * @param entity
     *            the entity to be deleted
     */
    public final void delete(final T entity) {
        getSession().delete(entity);
    }

    /**
     * @param entity
     *            update the entity
     * @return the updated entity
     */
    public final void update(final T entity) {
        getSession().update(entity);
    }

    /**
     * Get the entity from the database when entity is out of sync with cache.
     * e.g. perhaps database trigger or user type has manipulated column values. Use sparingly
     */
    public void refresh(final T entity) {
    	getSession().refresh(entity);
    }
}
