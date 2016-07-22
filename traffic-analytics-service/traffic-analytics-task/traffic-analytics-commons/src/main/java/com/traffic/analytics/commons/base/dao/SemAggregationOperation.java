package com.traffic.analytics.commons.base.dao;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * Created by david on 2016/5/4.
 */
public class SemAggregationOperation implements AggregationOperation {
    private DBObject operation;

    public SemAggregationOperation (DBObject operation) {
        this.operation = operation;
    }

    @Override
    public DBObject toDBObject(AggregationOperationContext context) {
        return context.getMappedObject(operation);
    }
}
