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
	 * 
	 * @param mapping
	 *            - The mapping to use.
	 * @param subscriber
	 *            - The subscriber to subscribe.
	 */
	public void subscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<?> subscriber);

	/**
	 * 
	 * @param mapping
	 *            - The mapping to use.
	 * @param subscriber
	 *            - The subscriber to subscribe.
	 * @param messageType
	 *            - The type to subscribe to.
	 */
	public <T> void subscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<?> subscriber, Class<T> messageType);

	/**
	 * 
	 * @param mapping
	 *            - The mapping to use.
	 * @param messageType
	 *            - The type to unsubscribe from.
	 */
	public <T> List<Subscriber<T>> unsubscribeAll(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Class<T> messageType);

	/**
	 * 
	 * @param mapping
	 *            - The mapping to use.
	 * @param subscriberType
	 *            - The type of the subscriber.
	 * @param messageType
	 *            - The type to unsubscribe from.
	 */
	public <S extends Subscriber<T>, T> boolean unsubscribeByTypes(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			S subscriberType, Class<T> messageType);

	/**
	 * 
	 * @param mapping
	 *            - The mapping to use.
	 * @param s
	 *            - The subscriber to unsubscribe.
	 * @param messageType
	 *            - The type to unsubscribe from.
	 * @return
	 */
	public <T> boolean unsubscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<T> s, Class<T> messageType);
}
