package my_spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class ApplicationContext {

    private HashMap <Class<?>, Object> singletonsInstance = new HashMap<>();


    @SneakyThrows
    public ApplicationContext() {
        singletonsInstance.put(IRobotImpl.class, null);
    }

    //---------------------------------------------------------------------------

    private <T> Class<? extends T> resolveRealImpl(Class<? extends T> type) {
        Config config = new JavaConfig();
        Reflections scanner = new Reflections("my_spring");

        if (type.isInterface()) {
            Class<? extends T> implClass = config.getImplClass(type);
            if (implClass == null) {
                Set<Class<? extends T>> classes = scanner.getSubTypesOf((Class <T>)type);
                if (classes.size() != 1) {
                    throw new IllegalStateException("0 or more than one impl found for type " + type);
                }
                type = classes.iterator().next();
            } else {
                type = implClass;
            }
        }
        return type;
    }

    //---------------------------------------------------------------------------

    private <T> T getClassInstance(Class<T> type) {
        if (singletonsInstance.get(type) == null)
            singletonsInstance.put(type, ObjectFactory.getInstance().createAndConfigObject(type));

        return (T) singletonsInstance.get(type);
    }

    //---------------------------------------------------------------------------

    public <T> T getObject(Class<T> type) {
        type = (Class<T>) resolveRealImpl(type);

        if (type.isAnnotationPresent(Singleton.class))
            return getClassInstance(type);
        else
            return ObjectFactory.getInstance().createAndConfigObject(type);
    }
}
