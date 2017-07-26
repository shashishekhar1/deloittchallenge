# deloittchallenge

1. Commentary on usecase - 1
I have made an implementation of ScheduledThreadPoolExecutor which after every 5 minutes calls the web service to get all customer data.
I have used a Map<ID, Customer> data structure which will get updated with all the Customer data from first web service call.
I also have a list of all customer id's which will get updated in 1st web service call.

In subsequent call of web service which happens after every 5 minutes, the customer data which is received is put into the map and the customer id's are removed from the list one by one, after updating all the records in map, the id's left in the list are the customers which got deleted.

Code removes the deleted customers from the map and the list is populated with the id which are present as the keyset of map.

2. Commentary on usecase - 2
I have used html5, css from bootstrap and Angular 1.x as front end tools for creating a web application which can also work on mobile platform.
Executed web service calls from angular to create a customer, update and delete a customer from interface.

3. Commentary on usecase - 3
We can extend the order and product api in following ways as per my understanding.

1 Customer can have multiple orders [Customer 1 * Order] one to many relationship and customer is identified by his unique id.
Customer class can be implemented on the lines given below.

Path("/customers")
class Customer{
      @Path("/{customerId}/orders")
	public Orders getOrders(){
		return new Orders();
	}
}

In similar way 1 order can have 1 to may products (1..*) and order can be identified uniquely via orderId.
web service call url should be something like [/customers/{customerId}/orders/{orderId}/products]

Path("/")
class Order{
  
@Path("/{orderId}/products")
  get Products getProducts(){
     return new Products();
  }
}

4. I have gone with the ScheduledThreadPool executor service but an Observer design pattern would have been a better implementation.

The API is pulling the records from the main application but there can also be an implementation where the main application sends the updated customer data to downstream systems, this is better approach as the main application could be busy in other tasks and an update API request could put extra load on server. 

Pushing the updated data when main application is idle reduces the load on main web service.
