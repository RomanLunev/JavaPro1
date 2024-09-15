import annotation.AfterSuite;
import annotation.BeforeSuite;
import annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void main(String[] args){
        runTests(Animal.class);
    }

    public static void runTests(Class c){
        Method beforeSuite = checkOnlyOneMethodAnnotation(c, BeforeSuite.class);
        Method afterSuite = checkOnlyOneMethodAnnotation(c, AfterSuite.class);
        invokeStatic(c, beforeSuite);
        processTests(c);
        invokeStatic(c, afterSuite);

    }

    private static void processTests(Class clazz){
        List<Method> methods =
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m->m.isAnnotationPresent(Test.class))
                .sorted(Comparator.comparing(m->m.getDeclaredAnnotation(Test.class).priority(), Comparator.reverseOrder()))
                .toList();

        for (Method method : methods) {
            System.out.println("----------------------");
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                System.out.println(method.getName() + " is static. Skipping test.");
                continue;
            }

            int priority = method.getAnnotation(Test.class).priority();
            if (priority < 0 || priority > 10) {
                System.out.println(method.getName() + " has wrong priority." + priority + " Skipping test.");
                continue;
            }


            method.setAccessible(true);
            try {
                System.out.println("Invoke " + method.getName());
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("Test " + method.getName() + " failed");
            }
        }
    }

    private static Method checkOnlyOneMethodAnnotation(Class clazz, Class annotationClass){
        Method method = null;
        int found = 0;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(annotationClass)) {

                if ((m.getModifiers() & Modifier.STATIC) == 0) {
                    throw new RuntimeException(m.getName() + " is not static");
                }

                found++;
                if (found > 1) {
                    throw new RuntimeException(clazz.toString() + " has more than one " + annotationClass.toString());
                }
                method = m;
            }
        }
        return method;
    }

    static void invokeStatic(Class clazz, Method method) {
        System.out.println("----------------------");
        if (method == null) return;
        method.setAccessible(true);
        try {
            System.out.println("Invoke " + method.getName());
            method.invoke(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can not invoke " + method.getName() + " Tests failed! " + e.getMessage());
        }
    }
}