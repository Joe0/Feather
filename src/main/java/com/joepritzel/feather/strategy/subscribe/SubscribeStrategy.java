package com.joepritzel.feather.strategy.subscribe;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.internal.SubscriberParent;

/**
 * 
 * An interface for strategies for subscribing.
 * 
 * @author Joe Pritzel
 * 
 */
public interface SubscribeStrategy {

	/**
	 * @see PSBroker#subscribe
	 */
	public <T> void subscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<?> subscriber, Class<T> messageType);

	/**
	 * @see PSBroker#unsubscribeAll
	 */
	public <T> List<Subscriber<T>> unsubscribeAll(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Class<T> messageType);

	/**
	 * @see PSBroker#unsubscribeByTypes
	 */
	public <S extends Subscriber<T>, T> boolean unsubscribeByTypes(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			S subscriberType, Class<T> messageType);

	/**
	 * @see PSBroker#unsubscribe
	 */
	public <T> boolean unsubscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<T> s, Class<T> messageType);
}
