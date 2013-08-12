package com.joepritzel.feather;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.internal.Publish;
import com.joepritzel.feather.internal.SubscriberParent;
import com.joepritzel.feather.strategy.publish.PublishStrategy;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;

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
	 * The strategy used for subscribing.
	 */
	private final SubscribeStrategy subscribeStrategy;

	PSBroker(ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			PublishStrategy publish, SubscribeStrategy subscribe) {
		this.mapping = mapping;
		this.publishStrategy = publish;
		this.subscribeStrategy = subscribe;
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
		subscribeStrategy.subscribe(mapping, subscriber, messageType);
	}

	/**
	 * Unsubscribes all subscribers listening to the given message type.
	 * 
	 * @param messageType
	 *            - The type to unsubscribe all subscribers from.
	 * @return Returns the list of subscribers that were forced to unsubscribe.
	 */
	public <T> List<Subscriber<T>> unsubscribeAll(Class<T> messageType) {
		return subscribeStrategy.unsubscribeAll(mapping, messageType);
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
		return subscribeStrategy.unsubscribeByTypes(mapping, subscriberType, messageType);
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
		return subscribeStrategy.unsubscribe(mapping, s, messageType);
	}
}
