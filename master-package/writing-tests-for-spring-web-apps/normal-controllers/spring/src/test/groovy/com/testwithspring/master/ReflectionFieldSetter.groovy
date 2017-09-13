package com.testwithspring.master

/**
 * This is an utility class that allows us to set field values by using
 * reflection. The different between this and other similar Java utility classes
 * (such as the {@code ReflectionTestUtils} class) is that this class
 * can set field values of fields found from the super classes.
 *
 * Note that we should use this class only when we are setting private
 * fields of a Java super class because these fields aren't inherited and
 * cannot be accessed with Groovy (even if you use field access).
 */
final class ReflectionFieldSetter {

    private ReflectionFieldSetter() {}

    /**
     * Sets the field value by using reflection.
     * @param target        The object whose field value is set.
     * @param fieldName     The name of the field whose value is set.
     * @param fieldValue    The new field value.
     */
    static def setFieldValue(target, fieldName, fieldValue) {
        try {
            def targetField = findFieldFromClassHierarchy(target.class, fieldName)
            targetField.accessible = true
            targetField.set(target, fieldValue)
        }
        catch (Exception ex) {
            throw new RuntimeException(
                    String.format('Cannot set the value of the field: %s because of an error', fieldName),
                    ex
            )
        }
    }

    /**
     * Finds the requested field from the class hierarchy.
     * @param clazz     The type of the actual object.
     * @param fieldName The name of the requested field.
     * @return
     */
    private static def findFieldFromClassHierarchy(clazz, fieldName) throws NoSuchFieldException {
        Class<?> current = clazz

        //Iterate the class hierarchy from the actual class
        //to the super class and try to find the requested field.
        def continueSearch = true
        while (continueSearch) {
            try {
                return current.getDeclaredField(fieldName)
            }
            catch(Exception e) {
                //An exception simply means that field is not found.
            }

            current = current.superclass
            continueSearch = current != null
        }

        //This signals that the requested field is not found.
        throw new NoSuchFieldException(String.format(
                'No field found with the field name %s',
                fieldName
        ))
    }
}
