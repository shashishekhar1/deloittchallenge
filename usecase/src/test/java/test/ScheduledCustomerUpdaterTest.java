package test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.raml.usecase.CustomerApiExecutor;
import com.raml.usecase.models.Customer;

public class ScheduledCustomerUpdaterTest {

	private CustomerApiExecutor apitest = new CustomerApiExecutor();
	
	@Before
	public void emptyMapBeforeExecution(){
		Set<Integer> allCustomerId = apitest.getAllCustomerdata().keySet();
		for(int id : allCustomerId){
			apitest.getAllCustomerdata().remove(id);
		}
	}
	
	/**
	 * This test case verifies all customers get added in map
	 * when there is no data in map. Simulating 1st web service call.
	 * */
	@Test
	public void testAddCustomerRecords(){
		apitest.addCustomersRecordsToDatabase(getCustomerArray(), true);
		
		assertEquals(3, apitest.getAllCustomerdata().size());
	}
	
	/**
	 * Simulating subsequent web service calls, New data received should be added,
	 * The a customer should be updated if he is present in map.
	 * If an customer data is not received he must be deleted. 
	 * */
	@Test
	public void testAddCustomerDataInSubsequentUpdates(){
		Customer c1 = new Customer(101, "Shashi", "Prashar", "George Street, NSW");
		Customer c2 = new Customer(105, "Rakesh", "Das", "Parramatta, NSW");
		Customer c3 = new Customer(107, "Sas", "Kumar", "Parramatta, NSW");
		
		apitest.getAllCustomerdata().put(101, c1);
		apitest.getAllCustomerdata().put(105, c2);
		apitest.getAllCustomerdata().put(107, c3);
		
		apitest.getCountOfPreviousCustomerId().add(c1.getId());
		apitest.getCountOfPreviousCustomerId().add(c2.getId());
		apitest.getCountOfPreviousCustomerId().add(c3.getId());
		
		apitest.addCustomersRecordsToDatabase(getCustomerArray(), false);
		
		assertEquals(3, apitest.getAllCustomerdata().size());
	}
	
	private JSONObject getArrayOfCustomers(){
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		JSONObject customer1 = new JSONObject();
		customer1.put("id", 101);
		customer1.put("firstname", "Shashi");
		customer1.put("lastname", "Prashar");
		customer1.put("address", "Strathfield, NSW");
		
		JSONObject customer2 = new JSONObject();
		customer2.put("id", 102);
		customer2.put("firstname", "Shuja");
		customer2.put("lastname", "Rehman");
		customer2.put("address", "Parramatta, NSW");

		JSONObject customer3 = new JSONObject();
		customer3.put("id", 103);
		customer3.put("firstname", "Mani");
		customer3.put("lastname", "dev");
		customer3.put("address", "Parramatta, NSW");
		
		array.add(customer1);
		array.add(customer2);
		array.add(customer3);

		json.put("customers", array);
		return json;
	}
	
	private JSONArray getCustomerArray(){
		return (JSONArray) getArrayOfCustomers().get("customers");
	}
}
