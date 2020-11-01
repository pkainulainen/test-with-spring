package com.testwithspring.master.dbunitdatatypes.config;

import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This data type adds support for the PostgreSQL's json data type.
 */
public class JsonDataType extends AbstractDataType {

    private final static String POSTGRESQL_TYPE_JSON = "json";

    public JsonDataType() {
        super(POSTGRESQL_TYPE_JSON, Types.OTHER, String.class, false);
    }

    @Override
    public Object typeCast(Object obj) throws TypeCastException {
        return obj.toString();
    }

    @Override
    public Object getSqlValue(int column, ResultSet resultSet) throws SQLException, TypeCastException {
        return resultSet.getString(column);
    }

    @Override
    public void setSqlValue(Object value, int column, PreparedStatement statement) throws SQLException, TypeCastException {
        final PGobject jsonValue = new PGobject();
        jsonValue.setType(POSTGRESQL_TYPE_JSON);
        jsonValue.setValue(value == null ? null : value.toString());

        statement.setObject(column, jsonValue);
    }
}
