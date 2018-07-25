package annotation;

import thread.ThreadPool;

import java.lang.reflect.Field;

public class AnnotationAnalysis {

    public void parse(ThreadPool threadPool) {
        Field field;
        try {
            field = threadPool.getClass().getDeclaredField("numberThread");
            field.setAccessible(true);
            if (field.isAnnotationPresent(ThreadAnnotation.class)) {
                field.setInt(threadPool, field.getAnnotation(ThreadAnnotation.class).numberThread());
            }
        } catch (NoSuchFieldException e) {
            System.exit(-1);
        } catch (IllegalAccessException e) {
            System.exit(-1);
        }

    }
}
