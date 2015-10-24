package com.trenurbanoapp.dao.jdbc;

import java.util.Map;

/**
 * User: victor
 * Date: 10/11/11
 */
interface ReverseRowMapper<T> {

    static enum Action {
        UPDATE, INSERT
    }

    Map<String, Object> reverseMap(T obj, Action action);
}
