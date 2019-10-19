import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();

        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        System.out.println("");

        // 1) Class name
        System.out.println("Class: " + c);

        // 2) Super-class name
        System.out.println("Super-class: " + c.getSuperclass());

        System.out.println("");

        // 3) Interface name
        Class[] interfaceList = c.getInterfaces();
        if (interfaceList.length == 0) {
            System.out.println("No interface(s) found");
        }
        else {
            for (Class inter : interfaceList) {
                System.out.println("Interface: " + inter);
            }
        }

        System.out.println("");

        // 4) Constructor name, parameter types and modifier
        Constructor[] constructorList = c.getDeclaredConstructors();
        if (constructorList.length == 0) {
            System.out.println("No constructor(s) found");
        }
        else {
            for (Constructor constructor : constructorList) {
                System.out.println("Constructor name: " + constructor.getName());

                Class[] cParamTypes = constructor.getParameterTypes();
                if (cParamTypes.length == 0) {
                    System.out.println("Constructor parameter type: None");
                }
                else {
                    for (Class paramType : cParamTypes) {
                        System.out.println("Constructor parameter type: " + paramType);
                    }
                }

                System.out.println("Constructor modifier: " + Modifier.toString(constructor.getModifiers()));

                System.out.println("");
            }
        }

        // 5) Method name, exceptions thrown, parameter types, return type, and modifier
        Method methodList[] = c.getDeclaredMethods();
        if (methodList.length == 0) {
            System.out.println("No method(s) found");
        }
        else {
            for (Method method : methodList) {
                System.out.println("Method name: " + method.getName());

                Class[] exceptionList = method.getExceptionTypes();
                if (exceptionList.length == 0) {
                    System.out.println("Method exception: None");
                }
                else {
                    for (Class except : exceptionList) {
                        System.out.println("Method exception: " + except);
                    }
                }

                Class[] mParamTypes = method.getParameterTypes();
                if (mParamTypes.length == 0) {
                    System.out.println("Method parameter type: None");
                }
                else {
                    for (Class paramType : mParamTypes) {
                        System.out.println("Method parameter type: " + paramType);
                    }
                }

                System.out.println("Method return type: " + method.getReturnType());
                System.out.println("Method modifier: " + Modifier.toString(method.getModifiers()));

                System.out.println("");
            }
        }

        // 6) Field name, type, modifier and current value
        Field[] fieldList = c.getDeclaredFields();
        if (fieldList.length == 0) {
            System.out.println("No field(s) found");
        }
        else {
            try {
                for (Field field : fieldList) {
                    field.setAccessible(true);
                    System.out.println("Field name: " + field.getName());
                    System.out.println("Field type: " + field.getType());
                    System.out.println("Field modifier: " + Modifier.toString(field.getModifiers()));
                    //int fieldVal = field.getInt(obj);
                    System.out.println("Field current value: " + field.get(obj));

                    System.out.println("");
                }
            }
            catch (IllegalAccessException e) {
                System.out.println("field not accessed");
            }
        }
    }


}