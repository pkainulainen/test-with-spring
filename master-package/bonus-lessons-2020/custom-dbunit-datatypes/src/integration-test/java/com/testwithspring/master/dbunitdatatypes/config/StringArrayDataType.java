package com.testwithspring.master.dbunitdatatypes.config;

import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;

/**
 * This class provides support for the PostgreSQL's array data type when
 * the array in question is a TEXT array.
 */
public class StringArrayDataType extends AbstractDataType {

    public StringArrayDataType(String name, int sqlType) {
        super(name, sqlType, String.class, false);
    }

    @Override
    public Object typeCast(Object value) throws TypeCastException {
        if (value instanceof String) {
            return ((String) value).split(",");
        }

        throw new IllegalArgumentException(String.format(
                "Unsupported type. Expected that the column value is a String but was: %s",
                value.getClass().getName()
        ));
    }
}
