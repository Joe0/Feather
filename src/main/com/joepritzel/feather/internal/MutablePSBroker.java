package com.joepritzel.feather.internal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.strategy.publish.FewQuickListeners;
import com.joepritzel.feather.strategy.publish.PublishStrategy;

public class MutablePSBroker {
	public Executor exec = new ForkJoinPool();
	public boolean considerHierarchy = false;
	public PublishStrategy publishStrategy = new FewQuickListeners(exec,
			considerHierarchy);
	public ConcurrentMap<Class<?>, List<SubscriberParent>> mapping = new ConcurrentHashMap<Class<?>, List<SubscriberParent>>();

	public PSBroker toPSBroker() {
		return new PSBroker(mapping, publishStrategy);
	}
}
