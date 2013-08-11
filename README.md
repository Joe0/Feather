Feather
=========

Feather is an extremely lightweight publish-subscribe message broker (event bus). It supports both topic (through types) and content (through a predicate) based filtering. The goal of Feater is to maximize message throughput.

Table of contents:
+ [Features](#features)
+ [Usage](#usage)
+ [License](#license)

<h2 name="features">Features</h2>

+ <strong>Strategy based</strong> Allows you to provide strategies to specify how the broker does things. This allows you to optimize for different things by simply changing how you create the broker.
+ <strong>Asynchronous</strong> Everything can run asynchronously, as well as synchronously. Just provide a strategy that does what you want.
+ <strong>Filters</strong> A simple yet flexible filter system. Every subscriber is fitered by types, and it is optionally filtered by a predicate. All you need to do is implement the PredicatedSubscriber class and the applies method.
+ <strong>Unread messages</strong> Catch any message that was not received by a subscriber just by creating a subscriber that listens for UnreadMessage.
+ <strong>Lightweight</strong> Feather is extremely lightweight and doesn't bring in any bloat, redundant concepts or confusing features.
+ <strong>Fast</strong> Faster than other publish-subscribe frameworks by orders of magnitude. Check out the <a href="http://www.joepritzel.com/blog/publish-subscribe" target="_blank">case study</a>.

<h2 name="usage">Usage</h2>
How to create the publish-subscribe message broker and set up for use:
```Java
// Use a builder to create a new PSBroker.
// The build allows you to provide customizable strategies for the broker to use.
PSBroker broker = new PSBrokerBuilder().build();
```  
Example of how to create a subscriber:
```Java
public class UnreadMessageReader extends Subscriber<UnreadMessage> {
	@Override
	public void receive(UnreadMessage message) {
		// insert logic here
	}
}
```
Example of how to let the broker know what messages a subscriber is listening to:
```Java
broker.subscribe(new UnreadMessageReader(), UnreadMessage.class);
```
Example of how to send a message that subscribers can receive:
```Java
broker.publish("Hello world!");
```
For complete examples, check out the examples in the src/example directory.
<h2 name="license">License</h2>
This project is distributed under the terms of the MIT License. See file "LICENSE" for further information.
