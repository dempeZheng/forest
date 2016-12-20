package com.zhizus.forest.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.EvictingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FailoverCheckingStrategy<T> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final int DEFAULT_FAIL_COUNT = 10;

	private static final long DEFAULT_FAIL_DURATION = TimeUnit.MINUTES.toMillis(1);

	private static final long DEFAULT_RECOVERY_DURATION = TimeUnit.MINUTES.toMillis(1);

	private final long failDuration;

	private final Cache<T, Boolean> failedList;

	private final LoadingCache<T, EvictingQueue<Long>> failCountMap;

	public FailoverCheckingStrategy() {
		this(DEFAULT_FAIL_COUNT, DEFAULT_FAIL_DURATION, DEFAULT_RECOVERY_DURATION);
	}

	public FailoverCheckingStrategy(final int failCount, long failDuration, long recoveryDuration) {
		this.failDuration = failDuration;
		this.failedList = CacheBuilder.newBuilder().weakKeys().expireAfterWrite(recoveryDuration, TimeUnit.MILLISECONDS).build();
		this.failCountMap = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<T, EvictingQueue<Long>>() {

			@Override
			public EvictingQueue<Long> load(T key) throws Exception {
				return EvictingQueue.create(failCount);
			}
		});
	}

	public Set<T> getFailed() {
		return failedList.asMap().keySet();
	}

	public void fail(T object) {
		logger.info("server {} failed.", object);
		boolean addToFail = false;
		try {
			EvictingQueue<Long> evictingQueue = failCountMap.get(object);
			synchronized (evictingQueue) {
				evictingQueue.add(System.currentTimeMillis());
				if (evictingQueue.remainingCapacity() == 0 && evictingQueue.element() >= System.currentTimeMillis() - failDuration) {
					addToFail = true;
				}
			}
		} catch (ExecutionException e) {
			logger.error("Ops.", e);
		}
		if (addToFail) {
			failedList.put(object, Boolean.TRUE);
			logger.info("server {} failed. add to fail list.", object);
		}
	}
	
}
