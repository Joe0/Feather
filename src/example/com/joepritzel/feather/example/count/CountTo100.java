package com.joepritzel.feather.example.count;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.PSBrokerBuilder;
import com.joepritzel.feather.Subscriber;

/**
 * Shows how to count to 100 using messages using the default configuration.
 * 
 * @author Joe Pritzel
 * 
 */
public class CountTo100 {

	private static PSBroker broker;

	public static void main(String[] args) throws InterruptedException {
		int countTo = 100;
		broker = new PSBrokerBuilder().build();
		broker.subscribe(new FinishedReader(), Finished.class);
		broker.subscribe(new Counter(countTo), Integer.class);
		broker.publish(0);
	}

	private static class Finished {
		public final int count;

		public Finished(int count) {
			this.count = count;
		}
	}

	private static class Counter extends Subscriber<Integer> {
		private final int max;

		public Counter(int max) {
			this.max = max;
		}

		@Override
		public void receive(Integer i) {
			if (i.intValue() == max) {
				broker.publish(new Finished(i.intValue()));
			} else {
				broker.publish(i + 1);
			}
		}
	}

	private static class FinishedReader extends Subscriber<Finished> {
		@Override
		public void receive(Finished f) {
			System.out.println("We finished at " + f.count);
		}
	}
}
