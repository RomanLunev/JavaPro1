import annotation.AfterSuite;
import annotation.BeforeSuite;
import annotation.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void main(String[] args){
        runTests(Animal.class);
    }

    private static void runTests(Class c){
        Method methodBeforeSuite = null;
        Method methodAfterSuite = null;
        Method[] methods = c.getDeclaredMethods();
        List<Method>  testMethodList = new ArrayList<>();
        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                if ((m.getModifiers() & Modifier.STATIC) == 0) {
                    throw new RuntimeException(m.getName() + " is not static");
                }
                if (methodBeforeSuite != null) {
                    throw new RuntimeException(c.toString() + " has more than one BeforeSuite");
                }
                methodBeforeSuite = m;
            } else if (m.isAnnotationPresent(AfterSuite.class)) {
                if ((m.getModifiers() & Modifier.STATIC) == 0) {
                    throw new RuntimeException(m.getName() + " is not static");
                }
                if (methodAfterSuite != null) {
                    throw new RuntimeException(c.toString() + " has more than one AfterSuite");
                }
                methodAfterSuite = m;
            } else if (m.isAnnotationPresent(Test.class)) {
                if ((m.getModifiers() & Modifier.STATIC) != 0) {
                    System.out.println(m.getName() + " is static. Skipping test.");
                    continue;
                }
                int priority = m.getAnnotation(Test.class).priority();
                if (priority < 0 || priority > 10) {
                    System.out.println(m.getName() + " has wrong priority." + priority + " Skipping test.");
                    continue;
                }
                testMethodList.add(m);
            }
        }
        System.out.println("------------Tests----------");
        invokeStatic(c, methodBeforeSuite);
        processTests(c, testMethodList);
        invokeStatic(c, methodAfterSuite);

        List<Method> testMethodListSorted =
                testMethodList.stream()
                        .sorted(Comparator.comparing(t->t.getDeclaredAnnotation(Test.class).priority(), Comparator.reverseOrder()))
                        .toList();

    }

    static void invokeStatic(Class clazz, Method method) {
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

    private static void processTests(Class clazz, List<Method> methods){
        List<Method> methodsSortred = methods.stream()
                .sorted(Comparator.comparing(m->m.getDeclaredAnnotation(Test.class).priority(), Comparator.reverseOrder()))
                .toList();
        for (Method method : methodsSortred) {
            method.setAccessible(true);
            try {
                System.out.println("Invoke " + method.getName());
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("Test " + method.getName() + " failed");
            }
        }
    }

}