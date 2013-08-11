package com.joepritzel.feather.internal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;

import com.joepritzel.feather.strategy.publish.FewQuickListeners;
import com.joepritzel.feather.strategy.publish.PublishStrategy;
import com.joepritzel.feather.strategy.subscribe.FastSubscribeUnsubscribe;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;

/**
 * A mutable, dummy version of PSBroker, used by the PSBrokerBuilder.
 * 
 * @author Joe Pritzel
 * 
 */
public class MutablePSBroker {
	public boolean considerHierarchy = false;
	public PublishStrategy publishStrategy = new FewQuickListeners(
			new ForkJoinPool(), considerHierarchy);
	public ConcurrentMap<Class<?>, List<SubscriberParent>> mapping = new ConcurrentHashMap<Class<?>, List<SubscriberParent>>();
	public SubscribeStrategy subscribeStrategy = new FastSubscribeUnsubscribe();
}
