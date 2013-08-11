package com.joepritzel.feather;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.internal.MutablePSBroker;
import com.joepritzel.feather.internal.SubscriberParent;
import com.joepritzel.feather.strategy.publish.PublishStrategy;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;

/**
 * A builder for the PSBroker class.<br>
 * <br>
 * By default, it uses the Sequential publish strategy and the
 * FastSubscribeUnsubscribe subscribe strategy.
 * 
 * @author Joe Pritzel
 * 
 */
public class PSBrokerBuilder {

	/**
	 * A mutable representation of a PSBroker.
	 */
	private MutablePSBroker broker;

	/**
	 * Creates a new PSBrokerBuilder.
	 */
	public PSBrokerBuilder() {
		broker = new MutablePSBroker();
	}

	/**
	 * Used to provide a strategy for publishing to the broker.
	 * 
	 * @param strategy
	 *            - The strategy that will be used to publish messages to
	 *            subscribers.
	 */
	public PSBrokerBuilder publishStrategy(PublishStrategy strategy) {
		broker.publishStrategy = strategy;
		return this;
	}

	/**
	 * Used to provide a strategy for subscribing to messages.
	 * 
	 * @param strategy
	 *            - The strategy that will be used to subscribe to message
	 *            types.
	 */
	public PSBrokerBuilder subscribeStrategy(SubscribeStrategy strategy) {
		broker.subscribeStrategy = strategy;
		return this;
	}

	/**
	 * The map that should be used internally, that maps types to a list of
	 * subscribers.
	 * 
	 * @param mapping
	 *            - The map to be used.
	 */
	public PSBrokerBuilder typeToSubscriberMapping(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping) {
		broker.mapping = mapping;
		return this;
	}

	/**
	 * Builds the PSBroker.
	 * 
	 * @return The PSBroker that has been built.
	 */
	public PSBroker build() {
		return new PSBroker(broker.mapping, broker.publishStrategy,
				broker.subscribeStrategy);
	}
}
