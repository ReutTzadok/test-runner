package my_junit;

import lombok.SneakyThrows;

import my_spring.ObjectFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    ObjectFactory factory = ObjectFactory.getInstance();


    private void removeMethods(List<Method> classMethods) {
        List<Method> notTestMethods = new ArrayList<>();

        for (Method methode : classMethods) {
            if (!methode.getName().startsWith("test"))
                notTestMethods.add(methode);
        }

        classMethods.removeAll(notTestMethods);
    }

    //---------------------------------------------------------------------------------

    private List<Method> methodsShouldRunBeforeTests(List<Method> classMethods){
        List<Method> methodRunBeforeTest = new ArrayList<>();

        for (Method method : classMethods) {
            if (method.isAnnotationPresent(RunBeforeEachTest.class))
                methodRunBeforeTest.add(method);
        }

        classMethods.removeAll(methodRunBeforeTest);

        return methodRunBeforeTest;
    }

    //---------------------------------------------------------------------------------

    @SneakyThrows
    public void runAllTestsOfClass(String className) {
        Class<?> aClass = Class.forName(className);

        List<Method> classTestMethods = new ArrayList<>(Arrays.asList(aClass.getDeclaredMethods()));

        List<Method> methodRunBeforeTest = methodsShouldRunBeforeTests(classTestMethods);

        removeMethods(classTestMethods);

        for (Method method : classTestMethods) {
            Object tmp = factory.createAndConfigObject(aClass);

            //I'm not sure if this methods should run before the first test or before each test.
            //if this methods should be run only once, this loop move out from the current loop.
            for (Method firstMethods : methodRunBeforeTest) {
                firstMethods.invoke(tmp);
            }

            method.invoke(tmp);
        }
    }
}
