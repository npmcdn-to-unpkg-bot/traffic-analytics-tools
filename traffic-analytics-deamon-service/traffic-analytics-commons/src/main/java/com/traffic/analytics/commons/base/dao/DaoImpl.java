package com.traffic.analytics.commons.base.dao;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mongodb.DBObject;
import com.traffic.analytics.commons.base.model.Model;
import com.traffic.analytics.commons.exception.DaoException;
import com.traffic.analytics.commons.pagination.PageData;
import com.traffic.analytics.commons.pagination.PageValue;
import com.traffic.analytics.commons.pagination.PageableThreadContext;

/**
 * MONGO DB的持久化操作接口的实现类
 * 
 * @author SEAN
 *
 * @param <T> 泛型，要求是Model的子类
 */
public class DaoImpl<T extends Model> implements Dao<T> {

	@Autowired
	@Qualifier("mongoTemplate")
	protected MongoTemplate mongoTemplate;

	protected Log log = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#get(java.io.Serializable)
	 */
	@Override
	public T get(Serializable id) {
		return mongoTemplate.findOne(this.createIdQuery(id), this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#get(com.traffic.analytics.commons.base.model.Model)
	 */
	@Override
	public T get(T t) {
		return mongoTemplate.findOne(this.createIdQuery(t), this.getEntityClass());
	}

	/*(non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#get(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public T get(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#get(java.lang.String, java.lang.Object)
	 */
	@Override
	public T get(String fieldName, Object fieldValue) {
		return mongoTemplate.findOne(this.createQuery(fieldName, fieldValue), this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#insert(com.traffic.analytics.commons.base.model.Model)
	 */
	@Override
	public T insert(T t) {
		if (StringUtils.isEmpty(t.getId())) {
			t.setId(ObjectId.get().toString());
		}
		mongoTemplate.insert(t);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#update(com.traffic.analytics.commons.base.model.Model)
	 */
	@Override
	public void update(T t) {
		if (StringUtils.isEmpty(t.getId())) {
			throw new DaoException("the updated object id is null");
		}
		Query query = this.createIdQuery(t);
		Update update = this.createUpdate(t);
		mongoTemplate.updateFirst(query, update, this.getEntityClass());

	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) {
		mongoTemplate.remove(this.createIdQuery(id), this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#delete(com.traffic.analytics.commons.base.model.Model)
	 */
	@Override
	public void delete(T t) {
		if (StringUtils.isEmpty(t.getId())) {
			throw new DaoException("the deete object id is null");
		}
		mongoTemplate.remove(this.createIdQuery(t), this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#delete(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public void delete(Query query) {
		mongoTemplate.remove(query, this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#find(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#find(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<T> find(String fieldName, Object fieldValue) {
		return mongoTemplate.find(this.createQuery(fieldName, fieldValue), this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#findAll()
	 */
	@Override
	public List<T> findAll() {
		return mongoTemplate.findAll(this.getEntityClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#page(java.lang.String, java.lang.Object)
	 */
	@Override
	public PageData<T> page(String fieldName, Object fieldValue) {
		PageValue pageValue = this.getPageValue();
		Query query = this.createQuery(fieldName, fieldValue);
		query.skip(pageValue.getStart());// skip相当于从那条记录开始  
        query.limit(pageValue.getLength());// 从skip开始,取多少条记录 
        query.with(pageValue.getSort() == null ? getIdAscSort() : pageValue.getSort());//排序
        List<T> list = mongoTemplate.find(query, this.getEntityClass());
		Long totalCount = mongoTemplate.count(query, this.getEntityClass());
		PageData<T> page = new PageData<T>(list, totalCount);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#page(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public PageData<T> page(Query query) {
		PageValue pageValue = this.getPageValue();
		query.skip(pageValue.getStart());// skip相当于从那条记录开始  
        query.limit(pageValue.getLength());// 从skip开始,取多少条记录 
		query.with(pageValue.getSort() == null ? getIdAscSort() : pageValue.getSort());//排序
		List<T> list = mongoTemplate.find(query, this.getEntityClass());
		Long totalCount = mongoTemplate.count(query, this.getEntityClass());
		PageData<T> page = new PageData<T>(list, totalCount);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * @see com.traffic.analytics.commons.base.dao.Dao#count(org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public Long count(Query query) {
		return mongoTemplate.count(query, this.getEntityClass());
	}

	/**
	 * 获取子类的泛型类型
	 *
	 * @author SEAN
	 * @return Class	返回实现类的Class对象
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		Class<T> entityClass = null;
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		}
		return entityClass;
	}

	/**
	 * 根据传递的对象创建Update对象
	 *
	 * @param t	泛型参数
	 * @return	{@link Update}
	 */
	private Update createUpdate(T t) {
		Update update = null;
		try {
			update = new Update();
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getEntityClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (key.compareToIgnoreCase("class") == 0) {
					continue;
				}
				Method getter = property.getReadMethod();
				Object value = getter != null ? getter.invoke(t) : null;
				if (value == null) {
					continue;
				}
				update.set(key, value);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
			throw new DaoException("Can't create mongodb update condition by the model!");
		}
		return update;

	}

	/**
	 * 创建根据ID查询的Query对象
	 *
	 * @param id	Model对象的ID
	 * @return {@link Query}
	 */
	private Query createIdQuery(Serializable id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return query;
	}

	/**
	 * 创建根据ID查询的Query对象
	 *
	 * @param t	泛型对象
	 * @return {@link Query}
	 */
	private Query createIdQuery(T t) {
		Query query = new Query();
		query.addCriteria(new Criteria("_id").is(this.getIdValue(t)));
		return query;
	}

	/**
	 * 创建根据字段查询的Query对象
	 *
	 * @param fieldName	字段名
	 * @param fieldValue	字段值
	 * @return {@link Query}
	 */
	private Query createQuery(String fieldName, Object fieldValue) {
		Query query = new Query();
		query.addCriteria(new Criteria(fieldName).is(fieldValue));
		return query;
	}

	/**
	 * 获取ID的值
	 *
	 * @param t
	 *            泛型对象
	 * @return id的值，id统一是String类型
	 */
	private String getIdValue(T t) {
		String id = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getEntityClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (key.compareToIgnoreCase("class") == 0) {
					continue;
				}
				String fieldName = property.getName();
				if (fieldName.equals("id")) {
					Method getter = property.getReadMethod();
					Object value = getter != null ? getter.invoke(t) : null;
					if (value == null) {
						throw new DaoException("The object id is null!");
					} else {
						id = value.toString();
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
			throw new DaoException("Can't get the object id value!");
		}
		return id;
	}

	/**
	 * 获取线程中的分页数据
	 * 
	 * @return {@link PageValue}
	 */
	protected PageValue getPageValue() {
		if (PageableThreadContext.getPageValue() != null) {
			PageValue p = PageableThreadContext.getPageValue();
			return p;
		} else {
			throw new DaoException("this is not a pagination request");
		}
	}

	/**
	 * 获取ID的排序对象:{@link Sort}
	 * @return	{@link Sort}
	 */
	private Sort getIdAscSort() {
		return new Sort(Direction.ASC, "_id");
	}

	@Override
	public PageData<DBObject> aggregateWithPage(String collectionName, List<AggregationOperation> aggregationOperations) {
		PageValue pageValue = this.getPageValue();
		List<AggregationOperation> countOperations = new ArrayList<AggregationOperation>(aggregationOperations);
		countOperations.add(Aggregation.group(new String[]{}).count().as("totalCount"));
		Aggregation aggCount = Aggregation.newAggregation(countOperations);
		AggregationResults<DBObject> resultCount = mongoTemplate.aggregate(aggCount, collectionName, DBObject.class);
		List<DBObject> totalCountResults = resultCount.getMappedResults();
		if(CollectionUtils.isEmpty(totalCountResults)){
			return new PageData<DBObject>(new ArrayList<DBObject>(), 0);
		}
		
		int totalCount = (int) resultCount.getMappedResults().get(0).get("totalCount");
		
		// page list
		AggregationOperation limit = Aggregation.limit(pageValue.getLength());//从skip开始,取多少条记录
		AggregationOperation skip = Aggregation.skip(pageValue.getStart());// skip相当于从那条记录开始
		
		aggregationOperations.add(skip);
		aggregationOperations.add(limit);
		
		if(pageValue.getSort() != null){
			AggregationOperation sort = Aggregation.sort(pageValue.getSort());
			aggregationOperations.add(sort);
		}
		
		Aggregation aggList = Aggregation.newAggregation(aggregationOperations);
		AggregationResults<DBObject> resultList = mongoTemplate.aggregate(aggList, collectionName, DBObject.class);
		List<DBObject> list = resultList.getMappedResults();
		PageData<DBObject> page = new PageData<DBObject>(list, totalCount);
		return page;
	}

	@Override
	public List<DBObject> aggregate(String collectionName, List<AggregationOperation> aggregationOperations) {
		Aggregation aggAll = Aggregation.newAggregation(aggregationOperations);
		AggregationResults<DBObject> resultList = mongoTemplate.aggregate(aggAll, collectionName, DBObject.class);
		List<DBObject> list = resultList.getMappedResults();
		return list;
	}
}
