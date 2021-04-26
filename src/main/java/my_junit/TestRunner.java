package my_junit;

import lombok.SneakyThrows;

import my_spring.ObjectFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    ObjectFactory factory = ObjectFactory.getInstance();


    private List<Method> methodsShouldRunBeforeTests(List<Method> methods){
        List<Method> methodRunBeforeTest = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RunBeforeEachTest.class))
                methodRunBeforeTest.add(method);
        }

//        methods.removeAll(methodRunBeforeTest);

        return methodRunBeforeTest;
    }

    @SneakyThrows
    public void runAllTestsOfClass(String className) {
        Class<?> aClass = Class.forName(className);
        Object o = factory.createAndConfigObject(Class.forName(className));

        List<Method> classMethods = Arrays.asList(o.getClass().getDeclaredMethods());
        List<Method> methodRunBeforeTest = methodsShouldRunBeforeTests(classMethods);

        for (Method method : classMethods) {
            if (methodRunBeforeTest.contains(method))
                    continue;

            Object tmp = factory.createAndConfigObject(aClass);
            for (Method firstMethods : methodRunBeforeTest) {
                firstMethods.invoke(tmp);
            }

//            Class<?>[] args = method.getParameterTypes();
            method.invoke(tmp);
        }


        //todo finish this by JUnit convention
    }
}
