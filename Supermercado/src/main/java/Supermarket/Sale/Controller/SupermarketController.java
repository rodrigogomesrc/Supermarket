package Supermarket.Sale.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import Rest.HttpUtils;
import Rest.Exceptions.RestRequestException;
import Supermarket.User.UserManager;

@RestController
public class SupermarketController {
	
	static Map<String, String> headerParams = new HashMap<String, String>();

	//@ spec_public
	static HttpUtils httpUtils;
	
	public SupermarketController() {
		headerParams.put("accept", "application/json");
		httpUtils = new HttpUtils();
	}
	
	@RequestMapping(value = "user/add/{username}", method = RequestMethod.GET)
	public String addUser(@PathVariable("username") String username) {
		if(UserManager.checkIfUserExists(username)) {
			return "ERROR: THIS USER ALREADY EXISTS";
		}
		
		UserManager.addUser(username);
		
		return "Welcome, " + username;
	}
	
	@RequestMapping(value = "sale/listSales", method = RequestMethod.GET)
	public String listSales() throws RestRequestException {
		String uri = "http://127.0.0.1:1026/v2/entities?type=Sale";
		String response = httpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);
		
		return response;
	}
	
	@RequestMapping(value = "sale/listSubscriptions/{username}", method = RequestMethod.GET)
	public String listSubscriptions(@PathVariable("username") String username) throws RestRequestException {
		String returnText = "=====> Your Subscriptions: \n";
		
		ArrayList<String> userSubscriptions = UserManager.getUserSubscriptions(username);
		
		if(userSubscriptions == null || userSubscriptions.size() == 0) {
			return "You have no subscriptions.";
		}
		
		for(String s : userSubscriptions) {
			returnText += "==> " + s + "\n";
		}
		
		
		return returnText;
	}
	
	@RequestMapping(value = "sale/subscribe/{username}/{product}", method = RequestMethod.GET)
	public String subscribeProduct(@PathVariable("username") String username, @PathVariable("product") String product) throws RestRequestException {
		if(!UserManager.checkIfUserExists(username)) {
			return "This user does not exists.";
		}
		
		if(UserManager.getUserSubscriptions(username).contains(product)) {
			return "You are already subscribed to this product.";
		}
		
		if(!UserManager.chechIfProductHasSubscriptions(product)) {			
			String uri = "http://127.0.0.1:1026/v1/subscribeContext";
			String body = "{"
					+ "    \"entities\": ["
					+ "        {"
					+ "            \"isPattern\": \"false\","
					+ "            \"id\": \"" + product + "\","
					+ "            \"type\": \"Sale\""
					+ "        }"
					+ "    ],"
					+ "    \"attributes\": ["
					+ "        \"Name\","
					+ "        \"Price\""
					+ "    ],\r\n"
					+ "    \"reference\": \"http://192.168.1.71:8080/Supermarket/updateProduct\","
					+ "    \"duration\": \"P1M\","
					+ "    \"notifyConditions\": ["
					+ "        {"
					+ "            \"type\": \"ONCHANGE\""
					+ "        }"
					+ "    ],"
					+ "    \"throttling\": \"PT5S\""
					+ "}";
			
			Map<String, String> localHeaderParams = new HashMap<String, String>();
			localHeaderParams.put("accept", "application/json");
			localHeaderParams.put("Content-Type", "application/json");
			String response = httpUtils.httpPostRequest(uri, localHeaderParams, body, 200);
			System.out.println(response);
		}
		
		UserManager.getUserSubscriptions(username).add(product);
		
		return "You have subscribed to recieve news about " + product + ".";
	}
	
	
	@RequestMapping(value = "sale/unsubscribe/{username}/{product}", method = RequestMethod.GET)
	public String unsubscribeProduct(@PathVariable("username") String username, @PathVariable("product") String product) throws RestRequestException {		
		if(!UserManager.checkIfUserExists(username)) {
			return "This user does not exists.";
		}
		
		if(!UserManager.getUserSubscriptions(username).contains(product)) {
			return "You are not subscribed to this product.";
		}

		UserManager.removeSubscriptionFromUser(username, product);

		return "You won't recieve news about " + product + ".";
	}	
	
	@RequestMapping(value = "updateProduct", method = RequestMethod.POST)
	public void newSale(@RequestBody JsonNode body) {
		String product = body.get("contextResponses").get(0).get("contextElement").get("attributes").get(0).get("value").toString();
		String price = body.get("contextResponses").get(0).get("contextElement").get("attributes").get(1).get("value").toString();
	
		UserManager.addUpdatesList(product.substring(1, product.length()-1), price.substring(1, price.length()-1));
	}
	
	@RequestMapping(value = "getUpdates/{username}", method = RequestMethod.GET)
	public String getUpdates(@PathVariable("username") String username) {
		String returnText = "[NEW UPDATE]: ";
		
		if(!UserManager.checkIfUserExists(username)) {
			return "This user does not exists.";
		};
		
		if(UserManager.getUserUpdates(username) == null) {
			return "";
		}
		
		for(String update : UserManager.getUserUpdates(username)) {
			returnText += update;
		}
		
		if(returnText != "") UserManager.removeUpdatesList(username);
		
		return returnText;
	}
}









