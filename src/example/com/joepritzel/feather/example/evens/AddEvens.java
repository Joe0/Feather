package com.joepritzel.feather.example.evens;

import java.util.ArrayList;
import java.util.List;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.PSBrokerBuilder;
import com.joepritzel.feather.PredicatedSubscriber;
import com.joepritzel.feather.Subscriber;

/**
 * Shows how to add all even numbers between 1 and 100 using messages and the
 * default configuration for PSBroker.<br>
 * <br>
 * This expands on the CountTo100 example and shows how to use predicated subscribers.
 * 
 * @author Joe Pritzel
 * 
 */
public class AddEvens {

	private static PSBroker broker;

	public static void main(String[] args) throws InterruptedException {
		int countTo = 99; // We don't want 100.
		Collector evens = new Collector();
		broker = new PSBrokerBuilder().build();

		// We want the collector to be called first, because the default broker
		// is sequential, and would end up making out list be reversed.
		broker.subscribe(evens, Integer.class);
		broker.subscribe(new Counter(countTo), Integer.class);

		broker.publish(1);

		evens.print();
	}

	private static class Counter extends Subscriber<Integer> {
		private final int max;

		public Counter(int max) {
			this.max = max;
		}

		@Override
		public void receive(Integer i) {
			if (i.intValue() != max) {
				broker.publish(i + 1);
			}
		}
	}

	private static class Collector extends PredicatedSubscriber<Integer> {

		private final List<Integer> evens = new ArrayList<>();

		@Override
		public boolean applies(Integer message) {
			return message % 2 == 0;
		}

		@Override
		public void receive(Integer f) {
			evens.add(f);
		}

		public void print() {
			for (int i : evens) {
				System.out.println(i);
			}
		}
	}
}
