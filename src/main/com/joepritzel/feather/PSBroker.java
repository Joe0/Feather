package com.joepritzel.feather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.joepritzel.feather.internal.Publish;
import com.joepritzel.feather.internal.SubscriberParent;
import com.joepritzel.feather.strategy.publish.PublishStrategy;

/**
 * A message broker that only supports the publish-subscribe pattern.
 * 
 * @author Joe Pritzel
 * 
 */
public class PSBroker {

	/**
	 * A mapping of message types to subscribers.
	 */
	private final ConcurrentMap<Class<?>, List<SubscriberParent>> mapping;

	/**
	 * The strategy used for publishing messages.
	 */
	private final PublishStrategy publishStrategy;

	/**
	 * Creates a new broker with the given executor.
	 * 
	 * @param exec
	 *            - The executor that will be used to publish messages and that
	 *            subscribers will run on.
	 * @param considerClassHierarchy
	 *            - If true, it will allow subscribers to receive messages that
	 *            are subtypes of messages types they've subscribed to. -
	 */
	public PSBroker(ConcurrentMap<Class<?>, List<SubscriberParent>> mapping, PublishStrategy publish) {
		this.mapping = mapping;
		this.publishStrategy = publish;
	}

	/**
	 * Publishes a message to the listening subscribers.
	 * 
	 * @param message
	 *            - The message to publish.
	 */
	public void publish(Object message) {
		publishStrategy.publish(new Publish(this, mapping, message));
	}

	/**
	 * Subscribes the subscriber to the given type of message.
	 * 
	 * @param subscriber
	 *            - The subscriber.
	 * @param messageType
	 *            - The type of message to subscribe to.
	 * @exception SubscriberTypeMismatchException
	 *                the type the subscriber was bound to is incompatible with
	 *                the type that is being received.
	 */
	public <T> void subscribe(Subscriber<?> subscriber, Class<T> messageType) {
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

	/**
	 * Unsubscribes all subscribers listening to the given message type.
	 * 
	 * @param messageType
	 *            - The type to unsubscribe all subscribers from.
	 * @return Returns the list of subscribers that were forced to unsubscribe.
	 */
	public <T> List<Subscriber<T>> unsubscribeAll(Class<T> messageType) {
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

	/**
	 * Forces subscribers to unsubscribe based on the given subscriber type and
	 * message type.
	 * 
	 * @param subscriberType
	 *            - The type of subscriber that will be forced to unsubscribe.
	 * @param messageType
	 *            - The type of message that the subscibers will unsubscribe
	 *            from.
	 * @return Returns true if at least one subscriber was forced to
	 *         unsubscribe. Otherwise, it returns false.
	 */
	public <S extends Subscriber<T>, T> boolean unsubscribeByTypes(
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

	/**
	 * Forces the given subscriber to unsubscribe from the given type of
	 * messages.
	 * 
	 * @param s
	 *            - The subscriber that will be forced to unsubscribe.
	 * @param messageType
	 *            - The type of message.
	 * @return Returns true if the subscriber was forced to unsubscribe from the
	 *         given type pf messages.
	 */
	public <T> boolean unsubscribe(Subscriber<T> s, Class<T> messageType) {
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
