import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();

        System.out.println("");
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        // 1) Class name
        printClass(c, depth);

        // 2) Super-class name
        printSuperclass(c, obj, recursive, depth);

        // 3) Interface name
        printInterfaces(c, obj, recursive, depth);

        // 4) Constructor name, parameter types and modifier
        printConstructor(c, obj, recursive, depth);

        // 5) Method name, exceptions thrown, parameter types, return type, and modifier
        printMethods(c, obj, recursive, depth);

        // 6) Field name, type, modifier and current value
        printFields(c, obj, recursive, depth);
    }

    // Print class information
    public void printClass(Class c, int depth) {
        formatPrint("CLASS", depth);
        formatPrint("Class: " + c + "\n", depth);
    }

    // Print superclass information
    public void printSuperclass(Class c, Object obj, boolean recursive, int depth) {
        formatPrint("SUPERCLASS (" + c.getName() + ")", depth);

        Class superclass = c.getSuperclass();
        if (superclass != null) {
            formatPrint("Superclass: " + superclass + "\n", depth);
            inspectClass(c.getSuperclass(),obj,recursive, depth + 1);
        }
        else {
            formatPrint("Superclass: null\n", depth);
        }
    }

    // Print interface information
    public void printInterfaces(Class c, Object obj, boolean recursive, int depth) {
        formatPrint("INTERFACES (" + c.getName() + ")", depth);

        Class[] interfaceList = c.getInterfaces();
        if (interfaceList.length != 0) {
            for (Class inter : interfaceList) {
                formatPrint("Interface: " + inter + "\n", depth);
                inspectClass(inter,obj,recursive,depth + 1);
            }
        }
        else {
            formatPrint("No interface(s) found\n", depth);
        }
    }

    // Print constructor information
    public void printConstructor(Class c, Object obj, boolean recursive, int depth) {
        formatPrint("CONSTRUCTOR (" + c.getName() + ")", depth);

        Constructor[] constructorList = c.getDeclaredConstructors();
        if (constructorList.length != 0) {
            for (Constructor constructor : constructorList) {
                formatPrint("Constructor name: " + constructor.getName(), depth);

                Class[] cParamTypes = constructor.getParameterTypes();
                if (cParamTypes.length != 0) {
                    for (Class paramType : cParamTypes) {
                        formatPrint("Constructor parameter type: " + paramType, depth);
                    }
                }
                else {
                    formatPrint("Constructor parameter type: None", depth);
                }

                formatPrint("Constructor modifier: " + Modifier.toString(constructor.getModifiers()) + "\n", depth);
            }
        }
        else {
           formatPrint("No constructor(s) found\n", depth);
        }
    }

    // Print method information
    public void printMethods(Class c, Object obj, boolean recursive, int depth) {
        formatPrint("METHODS (" + c.getName() + ")", depth);

        Method methodList[] = c.getDeclaredMethods();
        if (methodList.length != 0) {
            for (Method method : methodList) {
                formatPrint("Method name: " + method.getName(), depth);

                Class[] exceptionList = method.getExceptionTypes();
                if (exceptionList.length != 0) {
                    for (Class exception : exceptionList) {
                        formatPrint("Method exception: " + exception, depth);
                    }
                }
                else {
                    formatPrint("Method exception: None", depth);
                }

                Class[] mParamTypes = method.getParameterTypes();
                if (mParamTypes.length != 0) {
                    for (Class paramType : mParamTypes) {
                        formatPrint("Method parameter type: " + paramType, depth);
                    }
                }
                else {
                    formatPrint("Method parameter type: None", depth);
                }

                formatPrint("Method return type: " + method.getReturnType(), depth);
                formatPrint("Method modifier: " + Modifier.toString(method.getModifiers()) + "\n", depth);
            }
        }
        else {
            formatPrint("No method(s) found\n", depth);
        }
    }

    // Print field information
    public void printFields(Class c, Object obj, boolean recursive, int depth) {
        formatPrint("FIELDS (" + c.getName() + ")", depth);

        Field[] fieldList = c.getDeclaredFields();
        if (fieldList.length != 0) {
            for (Field field : fieldList) {
                formatPrint("Field name: " + field.getName(), depth);
                formatPrint("Field type: " + field.getType(), depth);
                formatPrint("Field modifier: " + Modifier.toString(field.getModifiers()), depth);

                field.setAccessible(true);

                Object fieldObject = null;
                try {
                    fieldObject = field.get(obj);
                }
                catch (IllegalAccessException e) {
                    System.out.println("Cannot access field");
                }

                if (fieldObject == null) {
                    formatPrint("Field value: null\n", depth);
                }
                else if (field.getType().isPrimitive()) {
                    formatPrint("Field value: " + fieldObject + "\n", depth);

                    if (recursive == true) {
                        inspectClass(field.getType(), fieldObject, recursive, depth + 1);
                    }
                }
                else if (field.getType().isArray()) {
                    inspectArray(field.getType(), fieldObject, recursive, depth);
                }
                else {
                    formatPrint("Field value (obj ref): " + fieldObject + "@" +  System.identityHashCode(fieldObject) + "\n",depth);

                    if (recursive == true) {
                        inspectClass(fieldObject.getClass(), fieldObject, recursive, depth + 1);
                    }
                }
            }
        }
        else {
            formatPrint("No field(s) found\n", depth);
        }
    }

    // Used to inspect elements within an array field
    public void inspectArray(Class field, Object fieldObject, boolean recursive, int depth) {
        int arrayLen = Array.getLength(fieldObject);

        formatPrint("Component type: " + field.getComponentType(), depth);
        formatPrint("Array length: " + arrayLen, depth);
        formatPrint("--- Array content --- ", depth);

        for (int i = 0; i < arrayLen; i++) {
            Object arrayObj = Array.get(fieldObject, i);

            if (arrayObj == null) {
                formatPrint("Array content value (" + i + "): null", depth);
            }
            else if (field.getComponentType().isPrimitive()) {
                formatPrint("Array primitive content value (" + i + "): " + arrayObj, depth);
            }
            else if (field.getComponentType().isArray()) {
                formatPrint("Array content value (" + i + "): " + arrayObj, depth);
                if (recursive == true) {
                    inspectArray(arrayObj.getClass(), arrayObj, recursive, depth + 1);
                }
            }
            else {
                formatPrint("Array object ref. content value (" + i + "): " + arrayObj.getClass().getName() + "@" + System.identityHashCode(fieldObject), depth);
                if (recursive == true) {
                    inspectClass(arrayObj.getClass(), arrayObj, recursive, depth + 1);
                }
            }
        }
        System.out.println();
    }

    // Format content printed to console based on the depth level of recursion
    public void formatPrint(String content, int depth) {
        System.out.println(tabIndication(depth) + content);
    }

    // Number of tabs needed based on the depth level of recursion
    public String tabIndication(int depth) {
        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs = tabs + "\t";
        }
        return(tabs);
    }
}