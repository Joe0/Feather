package com.joepritzel.feather;

/**
 * A topic based subscriber, where topics are paths.<br>
 * <br>
 * Subscribers will receive all messages to children of the paths specified.
 * 
 * @author Joe Pritzel
 * 
 */
public abstract class TopicSubscriber<T> extends
		PredicatedSubscriber<TopicalMessage<T>> {

	/**
	 * The path that the subscriber is subscribed to.
	 */
	private final String path;

	/**
	 * Creates a new topical subscriber.
	 * 
	 * @param path
	 *            - The path to listen on.
	 */
	public TopicSubscriber(String path) {
		this.path = path;
	}

	/**
	 * Invoked when a message is received by a path that this is subscribed to.
	 * 
	 * @param path
	 *            - The path that the message was sent to.
	 * @param message
	 *            - The message that was received.
	 */
	public abstract void received(String path, T message);

	public String getPath() {
		return path;
	}

	@Override
	public final boolean applies(TopicalMessage<T> message) {
		if (message.getPath().startsWith(path)) {
			return true;
		}
		return false;
	}

	@Override
	public final void receive(TopicalMessage<T> message) {
		received(message.getPath(), message.getMessage());
	}

}
