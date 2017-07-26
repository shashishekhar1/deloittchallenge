package com.raml.usecase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.raml.usecase.models.Customer;

/**
*This class will call the webservice and update the records in an in Memory Map,
*based on following conditions.
*1) If the customer is present and a record is received, he is updated.
*2) If the customer is not present and a new record is receive he is added.
*3) If a customer record is not received and he is present he is deleted.
*
*/
public class CustomerApiExecutor implements Runnable{
	private URL url = null;
	private HttpURLConnection conn = null;
	private JSONParser parser = new JSONParser();
	private JSONObject obj =null;
	private Set<Integer> countOfPreviousCustomerId = new HashSet<Integer>();
	
	private Map<Integer, Customer> allCustomerdata = new LinkedHashMap<Integer, Customer>();
	
	
	private void makeRestCall(){
		try {
			url = new URL("http://localhost:9090/customers");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			obj = (JSONObject) parser.parse(new InputStreamReader((conn.getInputStream()), "UTF-8"));
			JSONArray allCustomers = (JSONArray) obj.get("customers");
			
			if(allCustomerdata.size() == 0){
				addCustomersRecordsToDatabase(allCustomers, true);
			}else{
				addCustomersRecordsToDatabase(allCustomers, false);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void addCustomersRecordsToDatabase(JSONArray allCustomers, boolean firstCall){
		Iterator allJsonIterator = allCustomers.iterator();
		
		while(allJsonIterator.hasNext()){
			JSONObject tmpObj = (JSONObject) allJsonIterator.next();
			int id = (Integer) tmpObj.get("id");
			String fname = (String) tmpObj.get("firstname");
			String lname = (String) tmpObj.get("lastname");
			String address = (String) tmpObj.get("address");
			
			Customer customer = new Customer(id, fname, lname, address);
			allCustomerdata.put(id, customer);
			
			if(firstCall){
				countOfPreviousCustomerId.add(customer.getId());
			}else{
				countOfPreviousCustomerId.remove(customer.getId());
			}
		}
		
		if(!firstCall){
			removeCustomersNotReceivedInUpdate();
			countOfPreviousCustomerId.addAll(allCustomerdata.keySet());
		}
	}
	
	/**
	*This function removes the customer records not received in new web service call.
	*/
	private void removeCustomersNotReceivedInUpdate(){
		for(int id : countOfPreviousCustomerId){

			allCustomerdata.remove(id);
		}

	}
	
	public Set<Integer> getCountOfPreviousCustomerId() {
		return countOfPreviousCustomerId;
	}

	public Map<Integer, Customer> getAllCustomerdata() {
		return allCustomerdata;
	}
	
	public void run() {
		makeRestCall();
	}
}
