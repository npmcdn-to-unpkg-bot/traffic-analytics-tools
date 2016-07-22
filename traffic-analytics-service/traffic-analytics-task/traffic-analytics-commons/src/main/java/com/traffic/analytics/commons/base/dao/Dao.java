package com.traffic.analytics.commons.base.dao;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;
import com.traffic.analytics.commons.base.model.Model;
import com.traffic.analytics.commons.pagination.PageData;
import com.traffic.analytics.commons.pagination.PageableFilter;
import com.traffic.analytics.commons.pagination.PageableThreadContext;

/**
 * MONGO DB的持久化操作接口，如果有常用的DB操作，需要更新方法，请联系我(++)
 *
 * @author SEAN
 *
 * @param <T>
 *            泛型，PO（Persistent Object）统一从Model继承
 */
interface Dao<T extends Model> {

	/**
	 * 查询单个对象(根据参数对象的ID)
	 *
	 * @param t
	 *            泛型PO，对象的ID是Required，{@link #get(Serializable)}
	 * @return 泛型对象
	 */
	T get(T t);

	/**
	 * 查询单个对象(根据Query条件)
	 * @param query 查询条件
	 * @return	泛型对象
	 */
	T get(Query query);
	
	/**
	 * 查询单个对象(根据ID)
	 *
	 * @param id
	 *            主键
	 * @return 泛型对象
	 */
	T get(Serializable id);

	/**
	 * 根据字段获取单个对象，在调用此方法的时候请确保查询的字段是Unique的
	 *
	 * @param fieldName
	 *            字段名称
	 * @param fieldValue
	 *            字段的值
	 * @return 泛型对象
	 */
	T get(String fieldName, Object fieldValue);

	/**
	 * 插入单个对象，如果ID为空，会自动生成ID，ID生成策略请参考{@link ObjectId}
	 *
	 * @param t
	 *            泛型对象
	 * @return 被插入的泛型对象
	 */
	T insert(T t);

	/**
	 * 修改单个对象，传入的对象ID不允许为空
	 *
	 * @param t
	 *            要修改的泛型对象
	 */
	void update(T t);

	/**
	 * 删除单个对象（根据ID删除）
	 *
	 * @param id
	 *            对象的ID
	 */
	void delete(Serializable id);

	/**
	 * 删除单个对象（根据ID删除），{@link #delete(Serializable)}
	 *
	 * @param t
	 *            泛型对象
	 */
	void delete(T t);

	/**
	 * 按照条件删除数据
	 * 
	 * @param query
	 *            删除数据的条件
	 */
	void delete(Query query);

	/**
	 * 根据字段名称和字段的值查询对象的集合
	 *
	 * @param fieldName
	 *            字段名称
	 * @param fieldValue
	 *            字段的值
	 * @return 泛型对象的List列表
	 */
	List<T> find(String fieldName, Object fieldValue);

	/**
	 * 根据查询条件{@link Query}查询对象的集合
	 *
	 * @param query
	 *            查询条件
	 * @return 泛型对象的List列表
	 */
	List<T> find(Query query);

	/**
	 * 查询所有对象的集合
	 *
	 * @return 泛型对象的List列表
	 */
	List<T> findAll();

	/**
	 * 分页查询,此方法要结合{@link PageableFilter}和{@link PageableThreadContext}
	 * 使用，PageableFilter需要在web.xml中进行配置
	 *
	 * @param fieldName
	 *            字段名称
	 * @param fieldValue
	 *            字段的值
	 * @return 分页对象，{@link PageData}
	 */
	PageData<T> page(String fieldName, Object fieldValue);

	/**
	 * 分页查询,此方法要结合{@link PageableFilter}和{@link PageableThreadContext}
	 * 使用，PageableFilter需要在web.xml中进行配置
	 *
	 * @param query
	 *            查询条件
	 * @return 分页对象，{@link PageData}
	 */
	PageData<T> page(Query query);

	/**
	 * 根据查询条件获取数据条数
	 *
	 * @param query
	 *            查询条件
	 * @return 符合查询条件的记录条数
	 */
	Long count(Query query);

	/**
	 * 统计查询
	 * 
	 * @param aggregation
	 *            {@link Aggregation}
	 * @param inputClassType
	 *            输入的Class类型
	 * @param outputClassType
	 *            输出的Class类型
	 * @return {@link AggregationResults}带泛型参数的结果集
	 */
	List<DBObject> aggregate(String collectionName, List<AggregationOperation> aggregationOperations);
	
	/**
	 * 统计查询
	 * 
	 * @param aggregation
	 *            {@link Aggregation}
	 * @param inputClassType
	 *            输入的Class类型
	 * @param outputClassType
	 *            输出的Class类型
	 * @return {@link AggregationResults}带泛型参数的结果集
	 */
	PageData<DBObject> aggregateWithPage(String collectionName, List<AggregationOperation> aggregationOperations);
	
	
}
