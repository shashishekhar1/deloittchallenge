package com.raml.usecase;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ScheduledCustomersUpdater {

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new CustomerApiExecutor(), 1, 300, TimeUnit.SECONDS);
	}
}