package my_spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactory {
    private static ObjectFactory objectFactory = new ObjectFactory();
    private Reflections scanner = new Reflections("my_spring");

    private List<ObjectConfigurator> configurators = new ArrayList<>();

    public static ObjectFactory getInstance() {
        return objectFactory;
    }

    //---------------------------------------------------------------------------

    @SneakyThrows
    private ObjectFactory() {
        Set<Class<? extends ObjectConfigurator>> classes = scanner.getSubTypesOf(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> aClass : classes)
            if (!Modifier.isAbstract(aClass.getModifiers()))
                configurators.add(aClass.getDeclaredConstructor().newInstance());
    }

    //---------------------------------------------------------------------------

    @SneakyThrows
    public <T> T createAndConfigObject(Class<T> type) {
        T t = create(type);
        configure(t);
        return t;
    }

    //---------------------------------------------------------------------------

    private <T> T create(Class<T> type)
            throws InstantiationException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException, NoSuchMethodException {

        T t = type.getDeclaredConstructor().newInstance();
        return t;
    }

    //---------------------------------------------------------------------------

    private <T> void configure(T t) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(t);
        }
    }
}
