package com.raml.usecase;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
*This calls will call CustomerApiExecutor every 5 minutes and update an
*in memory record of customers.
*/
public class ScheduledCustomersUpdater {

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new CustomerApiExecutor(), 1, 300, TimeUnit.SECONDS);
	}
}
