//_XXXXX_

//eagle/eagle-core/eagle-query/eagle-storage-jdbc/src/main/java/org/apache/eagle/storage/jdbc/criteria/impl/ExpressionCriterionBuilder.java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.eagle.storage.jdbc.criteria.impl;

import org.apache.eagle.log.entity.EntityQualifierUtils;
import org.apache.eagle.storage.jdbc.criteria.CriterionBuilder;
import org.apache.eagle.query.parser.*;
import org.apache.eagle.storage.jdbc.schema.JdbcEntityDefinition;
import org.apache.torque.ColumnImpl;
import org.apache.torque.criteria.Criterion;
import org.apache.torque.criteria.SqlEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @since 3/27/15
 */
public class ExpressionCriterionBuilder implements CriterionBuilder {
    private final String tableName;
    private final ORExpression expression;
    private final JdbcEntityDefinition jdbcEntityDefinition;

    public ExpressionCriterionBuilder(ORExpression expression, JdbcEntityDefinition entityDefinition) {
        this.expression = expression;
        this.tableName = entityDefinition.getJdbcTableName();
        this.jdbcEntityDefinition = entityDefinition;
    }

    @Override
    public Criterion build() {
        Criterion orCriterion = null;
        for(ANDExpression andExpression:expression.getANDExprList()){
            Criterion andCriterion = null;
            for(AtomicExpression atomicExpression : andExpression.getAtomicExprList()){
                Criterion atomicCriterion = toAtomicCriterion(atomicExpression);
                if(andCriterion == null){
                    andCriterion = atomicCriterion;
                }else{
                    andCriterion = andCriterion.and(atomicCriterion);
                }
            }

            if(andCriterion!=null){
                if(orCriterion == null){
                    orCriterion = andCriterion;
                }else{
                    orCriterion = orCriterion.or(andCriterion);
                }
            }
        }
        return orCriterion;
    }

    private Criterion toAtomicCriterion(AtomicExpression atomic){
        Class<?> columnType = locateColumnType(atomic);
        Object left = toColumn(atomic.getKeyType(), atomic.getKey(),atomic.getOp(),columnType);
        Object right = toColumn(atomic.getValueType(), atomic.getValue(), atomic.getOp(),columnType);
        SqlEnum op = toSqlEnum(atomic.getOp());
        return new Criterion(left,right,op);
    }

    private Class<?> _XXXXX_(AtomicExpression atomic) {
        String columnName = null;
        if(atomic.getKeyType().equals(TokenType.ID)){
            columnName =  parseEntityAttribute(atomic.getKey());
        }else if(atomic.getValueType().equals(TokenType.ID)){
            columnName = parseEntityAttribute(atomic.getValue());
        }
        if(jdbcEntityDefinition.getInternal().getDisplayNameMap().containsKey(columnName)){
            try {
                return jdbcEntityDefinition.getColumnType(columnName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }
    }

    /**
     * this place is used for rewriting query for jdbc connection
     * @param tokenType
     * @param value
     * @param op
     * @return
     */
    private Object toColumn(TokenType tokenType,String value,ComparisonOperator op, Class<?> columnType) {
        if (op.equals(ComparisonOperator.CONTAINS) && tokenType.equals(TokenType.STRING)) {
            return "%" + value + "%";
        } else if (tokenType.equals(TokenType.ID)) {
            return new ColumnImpl(this.tableName, parseEntityAttribute(value));
        } else if (!tokenType.equals(TokenType.ID) && op.equals(ComparisonOperator.IN)) {
            return EntityQualifierUtils.parseList(value);
        } else if (tokenType.equals(TokenType.NUMBER)) {
            // TODO: currently only treat all number value as double
            // NOTE: Must use Number Object instead of primitive type
            if(columnType.equals(Long.class) || columnType.equals(long.class)) {
                return Long.parseLong(value);
            } else if(columnType.equals(int.class) || columnType.equals(Integer.class)){
                return Integer.parseInt(value);
            }else {
                return Double.parseDouble(value);
            }
        } else if (op.equals(ComparisonOperator.LIKE) && value.equals(".*")){
            return "%";
        } else{
            if((boolean.class.equals(columnType) || Boolean.class.equals(columnType)) && value != null){
                return Boolean.valueOf(value);
            }
            // TODO: parse type according entity field type
            return value;
        }
    }

    private SqlEnum toSqlEnum(ComparisonOperator op){
        SqlEnum sqlEnum = _opSqlEnum.get(op);
        if(sqlEnum == null){
            throw new IllegalArgumentException("Failed to convert ComparisonOperator:"+op+" to SqlEnum");
        }
        return sqlEnum;
    }

    private static String parseEntityAttribute(String fieldName){
        Matcher m = TokenConstant.ID_PATTERN.matcher(fieldName);
        if(m.find()){
            return m.group(1);
        }
        return fieldName;
    }

    private final static Map<ComparisonOperator,SqlEnum> _opSqlEnum = new HashMap<ComparisonOperator,SqlEnum>();
    static{
        _opSqlEnum.put(ComparisonOperator.CONTAINS, SqlEnum.LIKE);
        _opSqlEnum.put(ComparisonOperator.EQUAL, SqlEnum.EQUAL);
        _opSqlEnum.put(ComparisonOperator.NOT_EQUAL, SqlEnum.NOT_EQUAL);
        _opSqlEnum.put(ComparisonOperator.GREATER, SqlEnum.GREATER_THAN);
        _opSqlEnum.put(ComparisonOperator.GREATER_OR_EQUAL, SqlEnum.GREATER_EQUAL);
        _opSqlEnum.put(ComparisonOperator.IN, SqlEnum.IN);
        _opSqlEnum.put(ComparisonOperator.IS, SqlEnum.EQUAL);
        _opSqlEnum.put(ComparisonOperator.IS_NOT, SqlEnum.NOT_EQUAL);
        _opSqlEnum.put(ComparisonOperator.LESS, SqlEnum.LESS_THAN);
        _opSqlEnum.put(ComparisonOperator.LESS_OR_EQUAL, SqlEnum.LESS_EQUAL);
        _opSqlEnum.put(ComparisonOperator.LIKE, SqlEnum.LIKE);
    }
}
