package com.joepritzel.feather.internal;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.PSBroker;

/**
 * A wrapper for a PSBroker and a message.
 */
public class Publish {

	/**
	 * The message to publish.
	 */
	public final Object message;

	/**
	 * The broker
	 */
	public final PSBroker broker;

	/**
	 * The mapping of types to subscriberparents.
	 */
	public final ConcurrentMap<Class<?>, List<SubscriberParent>> mapping;

	public Publish(PSBroker broker,
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Object message) {
		this.broker = broker;
		this.mapping = mapping;
		this.message = message;
	}
}