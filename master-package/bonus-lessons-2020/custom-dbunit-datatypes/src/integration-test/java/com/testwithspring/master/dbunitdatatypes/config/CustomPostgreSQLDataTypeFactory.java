package com.testwithspring.master.dbunitdatatypes.config;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

import java.sql.Types;

/**
 *  Ensures that our custom data types are used by DbUnit when our
 *  integration tests use the PostgreSQL database.
 */
public class CustomPostgreSQLDataTypeFactory extends PostgresqlDataTypeFactory {

    private static final String SQL_TYPE_NAME_JSON = "json";
    private static final String SQL_TYPE_NAME_TEXT_ARRAY = "_text";

    @Override
    public DataType createDataType(int sqlType, String sqlTypeName, String tableName, String columnName) throws DataTypeException {
        if (sqlType == Types.ARRAY) {
            if (sqlTypeName.equals(SQL_TYPE_NAME_TEXT_ARRAY)) {
                return new StringArrayDataType(sqlTypeName, sqlType);
            }
            throw new IllegalArgumentException("Unsupported array type: " + sqlTypeName);
        }
        else if (sqlTypeName.equals(SQL_TYPE_NAME_JSON)) {
            return new JsonDataType();
        }

        return super.createDataType(sqlType, sqlTypeName, tableName, columnName);
    }
}
