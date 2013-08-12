package com.joepritzel.feather.internal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.strategy.publish.PublishStrategy;
import com.joepritzel.feather.strategy.publish.Sequential;
import com.joepritzel.feather.strategy.subscribe.FastSubscribeUnsubscribe;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;

/**
 * A mutable, dummy version of PSBroker, used by the PSBrokerBuilder.
 * 
 * @author Joe Pritzel
 * 
 */
public class MutablePSBroker {
	public PublishStrategy publishStrategy = new Sequential(false);
	public ConcurrentMap<Class<?>, List<SubscriberParent>> mapping = new ConcurrentHashMap<Class<?>, List<SubscriberParent>>();
	public SubscribeStrategy subscribeStrategy = new FastSubscribeUnsubscribe();
}
