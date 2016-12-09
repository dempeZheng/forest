package com.zhizus.forest.client.proxy.processor;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Dempe on 2016/12/7.
 */
public abstract class AnnotationProcessorsProvider {

    public static final AnnotationProcessorsProvider DEFAULT = new DefaultAnnotationProcessorsProvider();

    private final List<IAnnotationProcessor> processors = new CopyOnWriteArrayList<IAnnotationProcessor>();

    public static class DefaultAnnotationProcessorsProvider extends AnnotationProcessorsProvider {
        protected DefaultAnnotationProcessorsProvider() {
            ServiceLoader<IAnnotationProcessor> loader = ServiceLoader.load(IAnnotationProcessor.class);
            Iterator<IAnnotationProcessor> iterator = loader.iterator();
            while (iterator.hasNext()) {
                register(iterator.next());
            }
        }
    }

    public void register(IAnnotationProcessor processor) {
        processors.add(processor);
    }

    public List<IAnnotationProcessor> getProcessors() {
        return processors;
    }
}
