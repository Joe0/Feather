package com.joepritzel.feather.strategy.publish;

import com.joepritzel.feather.internal.Publish;

/**
 * 
 * An interface for strategies for publishing messages.
 * 
 * @author Joe Pritzel
 * 
 */
public interface PublishStrategy {

	/**
	 * Invoked when PSBroker's publish method is called.
	 * 
	 * @param message
	 *            - The message to be published.
	 */
	public void publish(Publish message);
}
