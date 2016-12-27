package com.testwithspring.intermediate;

import java.lang.reflect.Field;

/**
 * This is an utility class that allows us to set field values by using
 * reflection. The different between this and other similar utility classes
 * (such as the {@code ReflectionTestUtils} class) is that this class
 * can set field values of fields found from the super classes.
 */
public final class ReflectionFieldUtil {

    ReflectionFieldUtil() {}

    /**
     * Sets the field value by using reflection.
     * @param target        The object whose field value is set.
     * @param fieldName     The name of the field whose value is set.
     * @param fieldValue    The new field value.
     */
    public static void setFieldValue(Object target, String fieldName, Object fieldValue) {
        try {
            Field targetField = findFieldFromClassHierarchy(target.getClass(), fieldName);
            targetField.setAccessible(true);
            targetField.set(target, fieldValue);
        }
        catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Cannot set the value of the field: %s because of an error", fieldName),
                    ex
            );
        }
    }

    /**
     * Finds the requested field from the class hierarchy.
     * @param clazz     The type of the actual object.
     * @param fieldName The name of the requested field.
     * @return
     */
    private static Field findFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;

        //Iterate the class hierarchy from the actual class
        //to the super class and try to find the requested field.
        do {
            try {
                return current.getDeclaredField(fieldName);
            }
            catch(Exception e) {
                //An exception simply means that field is not found.
            }
        } while((current = current.getSuperclass()) != null);

        //This signals that the requested field is not found.
        throw new NoSuchFieldException(String.format(
                "No field found with the field name %s",
                fieldName
        ));
    }
}
