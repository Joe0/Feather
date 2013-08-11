package com.joepritzel.feather.strategy.subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.SubscriberTypeMismatchException;
import com.joepritzel.feather.internal.SubscriberParent;

/**
 * A pretty fast and general purpose SubscribeStrategy.
 * 
 * @author Joe Pritzel
 * 
 */
public class FastSubscribeUnsubscribe implements SubscribeStrategy {

	@Override
	public <T> void subscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<?> subscriber, Class<T> messageType) {
		try {
			subscriber.getClass().getDeclaredMethod("receive", messageType);
		} catch (NoSuchMethodException e) {
			throw new SubscriberTypeMismatchException();
		} catch (SecurityException e) {
			// Do nothing.
			// If the issue is present, then the type mismatch will occur
			// naturally when it's trying to cast it.
		}
		List<SubscriberParent> list = new CopyOnWriteArrayList<>();
		SubscriberParent p = getSubParent(subscriber);
		list.add(p);
		if (mapping.putIfAbsent(messageType, list) != null) {
			mapping.get(messageType).add(p);
		}
	}

	@Override
	public <T> List<Subscriber<T>> unsubscribeAll(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Class<T> messageType) {
		List<SubscriberParent> list = mapping.remove(messageType);
		List<Subscriber<T>> realList = new ArrayList<>(list.size());
		for (SubscriberParent sp : list) {
			@SuppressWarnings("unchecked")
			Subscriber<T> s = (Subscriber<T>) sp.getSubscriber();
			if (s != null) {
				realList.add(s);
			}
		}
		return realList;
	}

	@Override
	public <S extends Subscriber<T>, T> boolean unsubscribeByTypes(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			S subscriberType, Class<T> messageType) {
		boolean ret = false;
		List<SubscriberParent> list = mapping.get(messageType);
		if (list != null) {
			for (SubscriberParent p : list) {
				Subscriber<?> s = p.getSubscriber();
				if (s != null
						&& subscriberType.getClass().isAssignableFrom(
								s.getClass())) {
					list.remove(p);
					ret = true;
				}
			}
		}
		return ret;
	}

	@Override
	public <T> boolean unsubscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<T> s, Class<T> messageType) {
		List<SubscriberParent> list = mapping.get(messageType);
		if (list != null) {
			return list.remove(getSubParent(s));
		}
		return false;
	}

	/**
	 * A map of subscribers to their subscriber parents.
	 */
	private final Map<Subscriber<?>, SubscriberParent> subscriberParents = Collections
			.synchronizedMap(new WeakHashMap<Subscriber<?>, SubscriberParent>());
	/**
	 * A lock for the subscriberParents field.
	 */
	private final ReentrantLock parentLock = new ReentrantLock();

	/**
	 * @param subscriber
	 *            - The subscriber to wrap.
	 * @return Returns a {@linkplain SubscriberParent} to be used in the
	 *         mapping.
	 */
	private SubscriberParent getSubParent(Subscriber<?> subscriber) {
		try {
			parentLock.lock();
			SubscriberParent p = subscriberParents.get(subscriber);
			if (p == null) {
				p = new SubscriberParent(subscriber);
				subscriberParents.put(subscriber, p);
			}
			return p;
		} finally {
			parentLock.unlock();
		}
	}

}
