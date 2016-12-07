package com.dempe.forest.client.proxy.processor;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Dempe on 2016/12/7.
 */
public abstract class AnnotationProcessorsProvider {

    public static final AnnotationProcessorsProvider DEFAULT = new DefaultAnnotationProcessorsProvider();

    private final List<AnnotationProcessor> processors = new CopyOnWriteArrayList<AnnotationProcessor>();

    public static class DefaultAnnotationProcessorsProvider extends AnnotationProcessorsProvider {
        protected DefaultAnnotationProcessorsProvider() {
            ServiceLoader<AnnotationProcessor> loader = ServiceLoader.load(AnnotationProcessor.class);
            Iterator<AnnotationProcessor> iterator = loader.iterator();
            while (iterator.hasNext()) {
                register(iterator.next());
            }
        }
    }

    public void register(AnnotationProcessor processor) {
        processors.add(processor);
    }

    public List<AnnotationProcessor> getProcessors() {
        return processors;
    }
}
