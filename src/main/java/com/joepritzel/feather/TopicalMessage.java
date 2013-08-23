package com.joepritzel.feather;

public class TopicalMessage<T> {
	private final String path;
	private final T message;

	public TopicalMessage(String path, T message) {
		this.path = path;
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public T getMessage() {
		return message;
	}

}
