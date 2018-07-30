/**
 * 
 */
package cmt.elk.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author c.tripathi
 *
 */
public class ElkDemoLogProvider {

	private static Logger logger = Logger.getLogger(ElkDemoLogProvider.class);

	public static void main(String[] args) throws IOException {

		try {

			logger.info("STARTING DATA COLLECTION");

			Customer customer = new Customer();
			customer.setName("Will Smith");
			customer.setAge(45);
			customer.setSex('M');
			customer.setIdentification("1434554567");

			List<Order> orders = new ArrayList<Order>();

			for (int counter = 1; counter < 100; counter++) {

				Order order = new Order();

				order.setOrderId(counter);
				order.setProductId(counter);
				order.setCustomerId(customer.getIdentification());
				order.setQuantity(counter);

				orders.add(order);

			}

			logger.info("FETCHING RESULTS INTO DESTINATION");

			PrintWriter file = new PrintWriter(
					new FileWriter("/Users/c.tripathi/gr/logs/" + new Date().getTime() + ".txt"));

			file.println(
					"1" + customer.getName() + customer.getSex() + customer.getAge() + customer.getIdentification());

			for (Order order : orders) {
				file.println(
						"2" + order.getOrderId() + order.getCustomerId() + order.getProductId() + order.getQuantity());
			}

			logger.info("CLEANING UP!");

			file.flush();
			file.close();

			// forcing a error to simulate stack traces
			PrintWriter fileError = new PrintWriter(new FileWriter("/etc/nopermission.txt"));

		} catch (Exception e) {

			logger.error("Error Occurred :: ", e);
		}

	}

}
