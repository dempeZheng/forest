package com.dempe.forest;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/2
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
public class ExecutorGroup {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecutorGroup.class);

    private static Map<String, Executor> executorsMap = Maps.newConcurrentMap();

    private ServerConfig config;

    public ExecutorGroup(ServerConfig config, Set<String> groupSet) {
        this.config = config;
        for (String group : groupSet) {
            executorsMap.put(group, new StandardThreadExecutor(config.coreThread(), config.maxThreads()));
        }
    }


    public void execute(String group, Runnable command) {
        Executor executor = executorsMap.get(group);
        if (executor == null) {
            LOGGER.warn("group:{} is null, use {} as default.", group, Constants.DEF_GROUP);
            executor = executorsMap.get(Constants.DEF_GROUP);
        }
        executor.execute(command);
    }

}
