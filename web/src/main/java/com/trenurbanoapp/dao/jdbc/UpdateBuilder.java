package com.trenurbanoapp.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/14/13
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
class UpdateBuilder {

    private StringBuilder params;
    private StringBuilder where;
    private List<Object> values = new ArrayList<>();
    private List<Object> whereValues = new ArrayList<>();

    public UpdateBuilder(String table) {
        params = new StringBuilder("update ").append(table).append(" set ");
    }

    public UpdateBuilder append(String param, Object value) {
        params.append(param).append("=?, ");
        values.add(value);
        return this;
    }

    public UpdateBuilder appendAll(Map<String, Object> params) {
        for (String s : params.keySet()) {
            append(s, params.get(s));
        }
        return this;
    }

    public UpdateBuilder appendLiteral(String param, String literal) {
        params.append(param).append("=").append(literal).append(", ");
        return this;
    }

    public UpdateBuilder whereEquals(String param, Object value) {
        if (where == null) where = new StringBuilder(" where ");
        where.append(param).append("=? and ");
        whereValues.add(value);
        return this;
    }

    public String sql() {
        //trim last comma on set clause
        String paramsFinal = params.toString().substring(0, params.toString().lastIndexOf(','));
        //trim last "and" from where clause
        if (where != null) paramsFinal += where.toString().substring(0, where.toString().lastIndexOf("and"));
        return paramsFinal;
    }

    public Object[] values() {
        List<Object> allValues = new ArrayList<Object>(values.size() + whereValues.size());
        allValues.addAll(values);
        allValues.addAll(whereValues);
        return allValues.toArray();
    }
}

