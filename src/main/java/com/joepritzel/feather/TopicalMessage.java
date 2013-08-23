package com.joepritzel.feather;

/**
 * A message used to send to TopicSubscibers.
 * 
 * @author Joe Pritzel
 * 
 * @param <T>
 *            - The type of message.
 */
public class TopicalMessage<T> {
	private final String path;
	private final T message;

	/**
	 * Creates a new topical message.
	 * 
	 * @param path
	 *            - The path to be sent to.
	 * @param message
	 *            - The message to be sent.
	 */
	public TopicalMessage(String path, T message) {
		this.path = path;
		this.message = message;
	}

	/**
	 * @return The path that the message was sent to.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return THe message that was sent.
	 */
	public T getMessage() {
		return message;
	}

}
