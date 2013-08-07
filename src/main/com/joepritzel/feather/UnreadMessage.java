package com.joepritzel.feather;

/**
 * This is a message that was never received by a subscriber. That is, nothing
 * was subscribed to this message.
 * 
 * @author Joe Pritzel
 * 
 */
public class UnreadMessage {

	private final Object message;

	public UnreadMessage(Object message) {
		this.message = message;
	}

	/**
	 * @return The message that was unread.
	 */
	public Object getMessage() {
		return message;
	}

}
