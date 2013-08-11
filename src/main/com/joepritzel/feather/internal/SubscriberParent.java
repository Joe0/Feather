package com.joepritzel.feather.internal;

import com.joepritzel.feather.Subscriber;

/**
 * Used to mask generics.
 */
public class SubscriberParent {
	private final Subscriber<?> s;

	public SubscriberParent(Subscriber<?> subscriber) {
		this.s = subscriber;
	}

	public Subscriber<?> getSubscriber() {
		return s;
	}
}
