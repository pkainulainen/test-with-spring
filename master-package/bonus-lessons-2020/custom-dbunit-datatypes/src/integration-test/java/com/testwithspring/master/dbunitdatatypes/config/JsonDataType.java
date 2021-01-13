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
    public Object typeCast(Object value) throws TypeCastException {
        if (value instanceof String) {
            return value;
        }
        //This is required if we want to write assertions for the data
        //which is stored in a table which uses the json data type.
        else if (value instanceof PGobject) {
            PGobject jsonCandidate = (PGobject) value;
            if (jsonCandidate.getType().equals(POSTGRESQL_TYPE_JSON)) {
                return jsonCandidate.getValue();
            }
        }

        throw new IllegalArgumentException(String.format(
                "Unsupported type. Expected that the column value is a String but was: %s",
                value.getClass().getName()
        ));
    }
}
