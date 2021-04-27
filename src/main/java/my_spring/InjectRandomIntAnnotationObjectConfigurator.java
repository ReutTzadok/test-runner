package my_spring;

import my_spring.RandomUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class InjectRandomIntAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object t) {
        Class<?> type = t.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                int value = RandomUtil.getNumberBetween(min, max);
                field.setAccessible(true);
                field.set(t,value);
            }
        }
    }
}
