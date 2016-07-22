package com.traffic.analytics.commons.base.dao;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
/**
 * Mongodb复杂查询的解析器
 * 
 * @author SEAN
 */
public class MongoQueryParser {
    private Criteria criteria = null;
    private boolean isFirstQueryCondition = true;

    private String[] fields = new String[]{ "websiteId", "accountId", "campaignId", "campaign", "date", "adgroupId", "adgroup", "keywordId", "keyword", "source", "destinationUrl" };
    /**
     * 解析自定义类型{@link MongoQuery}
     * 
     * @param querys
     *            {@link MongoQuery}的集合
     * @return Spring封装的{@link Query}对象
     */
    public Criteria parse(List<MongoQuery> querys) {
        //Criteria criteria = null;
        if (CollectionUtils.isEmpty(querys)) {
            return criteria;
        }
        Set<String> multiQueryFields = getMultiQueryFields(querys);
        Map<String, List<MongoQuery>> map = new HashMap<String, List<MongoQuery>>();
        // 单一字段的单一查询条件
        for (MongoQuery mongoQuery : querys) {
            String fieldName = mongoQuery.getFieldName();
            String fieldType = mongoQuery.getFieldType();
            String condition = mongoQuery.getCondition();
            String value = mongoQuery.getValue();
            // 四个必备字段都不能为空
            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(fieldType) || StringUtils.isBlank(condition) || StringUtils.isBlank(value)) {
                continue;
            }
            // 将多次查询的条件放到map中，并跳过
            if (multiQueryFields.contains(fieldName)) {
                if (!map.containsKey(fieldName)) {
                    List<MongoQuery> queryConditions = new ArrayList<MongoQuery>();
                    map.put(fieldName, queryConditions);
                }
                map.get(fieldName).add(mongoQuery);
                continue;
            }
            // add fieldName to where
            addFieldToCriteria(fieldName);
            if (fieldType.equals(FieldType.number.value)) {
                this.processNumberType(condition, value);
            } else if (fieldType.equals(FieldType.string.value)) {
                this.processStringType(condition, value);
            }
        }
        // 单一字段的多条件查询
        for (Entry<String, List<MongoQuery>> entry : map.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue().get(0).getFieldType();
            List<MongoQuery> singleFieldQuerys = entry.getValue();
            List<Criteria> criterias = new ArrayList<Criteria>(singleFieldQuerys.size());
            for (MongoQuery mongoQuery : singleFieldQuerys) {
                String condition = mongoQuery.getCondition();
                String value = mongoQuery.getValue();
                Criteria multiCriteria = Criteria.where(fieldName);
                if (fieldType.equals(FieldType.number.value)) {
                    this.processNumberType(multiCriteria, condition, value);
                } else if (fieldType.equals(FieldType.string.value)) {
                    this.processStringType(multiCriteria, condition, value);
                }
                criterias.add(multiCriteria);
            }
            if(isFirstQueryCondition){
            	criteria = new Criteria();
            }
            criteria = criteria.andOperator(criterias.toArray(new Criteria[criterias.size()]));
        }
        return criteria;
    }
    /**
     * 在Mongodb中，如果一个字段是数值型，并且同时要满足gt和lt这种范围的查询，需要用addOperate来实现，
     * 不能用addCriteria来实现
     * 
     * @param querys
     *            自定义的查询条件
     * @return 返回有多个查询条件的字段
     */
    private Set<String> getMultiQueryFields(List<MongoQuery> querys) {
        Set<String> fieldNames = new HashSet<String>();
        Set<String> rtn = new HashSet<String>();
        for (MongoQuery mongoQuery : querys) {
            String fieldName = mongoQuery.getFieldName();
            System.out.println(fieldName);
            if (fieldNames.contains(fieldName)) {
                rtn.add(fieldName);
            }
            fieldNames.add(fieldName);
        }
        return rtn;
    }
    private void addFieldToCriteria(String fieldName) {
        if (Arrays.asList(fields).contains(fieldName)) {
            fieldName = "_id." + fieldName;
        }
        if (isFirstQueryCondition) {
            criteria = Criteria.where(fieldName);
            isFirstQueryCondition = false;
        } else {
            criteria = criteria.and(fieldName);
        }
    }
    /**
     * 处理String类型的查询
     * 
     * @param condition
     *            查询的条件语句
     * @param value
     *            输入的值
     */
    private void processStringType(String condition, String value) {
        if(condition.equals(StringFieldCondition.equals.value)){
            criteria = criteria.is(value);
            return;
        }
        
        if(condition.equals(StringFieldCondition.like.value)){
            criteria = criteria.regex("^.*" + value + ".*$");
            return;
        }
    }
    /**
     * 处理String类型的查询
     * 
     * @param condition
     *            查询的条件语句
     * @param value
     *            输入的值
     */
    private void processStringType(Criteria multiCriteria, String condition, String value) {
        if(condition.equals(StringFieldCondition.equals.value)){
            multiCriteria = multiCriteria.is(value);
            return;
        }
        
        if(condition.equals(StringFieldCondition.like.value)){
            multiCriteria = multiCriteria.regex("^.*" + value + ".*$");
            return;
        }
    }
    /**
     * 处理Number类型的查询
     * 
     * @param condition
     *            查询的条件语句
     * @param value
     *            输入的值
     */
    private void processNumberType(String condition, String value) {
        if(condition.equals(NumberFieldCondition.lt.value)){
            criteria = criteria.lt(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.lte.value)){
            criteria = criteria.lte(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.gt.value)){
            criteria = criteria.gt(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.gte.value)){
            criteria = criteria.gte(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.equals.value)){
            criteria = criteria.is(Double.parseDouble(value));
            return;
        }
    }
    /**
     * 处理Number类型的查询
     * 
     * @param condition
     *            查询的条件语句
     * @param value
     *            输入的值
     */
    private void processNumberType(Criteria multiCriteria, String condition, String value) {
        if(condition.equals(NumberFieldCondition.lt.value)){
            multiCriteria = multiCriteria.lt(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.lte.value)){
            multiCriteria = multiCriteria.lte(Double.parseDouble(value));
            return;
        }
        
        
        if(condition.equals(NumberFieldCondition.gt.value)){
            multiCriteria = multiCriteria.gt(Double.parseDouble(value));
            return;
        }
        
        
        if(condition.equals(NumberFieldCondition.gte.value)){
            multiCriteria = multiCriteria.gte(Double.parseDouble(value));
            return;
        }
        
        if(condition.equals(NumberFieldCondition.equals.value)){
            multiCriteria = multiCriteria.is(Double.parseDouble(value));
            return;
        }
    }
    
    private enum FieldType {
        
        string("string"),number("number");
        
        private String value;
        
        private FieldType(String value){
            this.value = value;
        }
        
        @Override
        public String toString(){
            return this.value;
        }
    }
    
    
    private enum NumberFieldCondition {
        lt("lt"),
        gt("gt"),
        lte("lte"),
        gte("gte"),
        equals("equals");
        private String value;
        
        private NumberFieldCondition(String value){
            this.value = value;
        }
        
        @Override
        public String toString(){
            return this.value;
        }
    }
    
    
    private enum StringFieldCondition {
        like("like"),
        equals("equals");
        private String value;
        
        private StringFieldCondition(String value){
            this.value = value;
        }
        
        @Override
        public String toString(){
            return this.value;
        }
    }
    
    
    
}
