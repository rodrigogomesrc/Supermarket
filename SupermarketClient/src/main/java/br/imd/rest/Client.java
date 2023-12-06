package br.imd.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.imd.rest.expections.RestRequestException;

public class Client {

	//@ spec_public
	static Map<String, String> headerParams;

	//@ spec_public
	static String username;

	//@ spec_public
	static int option = -1;

	//@ spec_public
	static HttpUtils httpUtils = new HttpUtils();

	//@ ensures headerParams != null && headerParams.containsKey("accept") && headerParams.containsValue("application/json");
	public Client() {
		headerParams = new HashMap<String, String>();
		headerParams.put("accept", "application/json");
	}

	//@ requires args != null;
	//@ requires true;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public static void main(String[] args) throws RestRequestException {
		Client restClient = new Client();

		restClient.setUsername();

		restClient.new MessageListener().start();

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		while(option != 0) {
			System.out.println("==========================================");
			System.out.println("USER: " + username);
			System.out.println("0 - Quit Application.");
			System.out.println("1 - List Sales.");
			System.out.println("2 - List Subscriptions.");
			System.out.println("3 - Subscribe to receive updates on a product.");
			System.out.println("4 - Unsubscribe from updates on a product.");
			System.out.println("==========================================");
			option = sc.nextInt();
			sc.nextLine();

			if(option == 1) {
				listSales();
			}
			else if(option == 2) {
				listSubscriptions();
			}
			else if(option == 3) {
				String product;
				System.out.println("Inform the name of the product you want to receive updates for: ");
				product = sc.nextLine();
				subscribeProduct(product);
			}
			else if(option == 4) {
				String product;
				System.out.println("Inform the name of the product you want to stop receiving updates for: ");
				product = sc.nextLine();
				unsubscribeProduct(product);
			}
		}
	}

	//@ requires true;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public void setUsername() throws RestRequestException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String _username = "";

		System.out.println("Please, inform your username: ");
		_username = sc.nextLine();

		String uri = "http://localhost:8080/Supermarket/user/add/"+_username;
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);

		while(response.startsWith("ERROR")) {
			_username = sc.nextLine();
			uri = "http://localhost:8080/Supermarket/user/add/"+_username;
			response = httpUtils.httpGetRequest(uri, headerParams);

			System.out.println(response);
		};

		username = _username;
	}

	//@ requires true;
	//@ ensures true;
	//@ signals_only RestRequestException;
	// @ signals (RestRequestException) false;
	public static void listSales() throws RestRequestException {
		String uri = "http://localhost:8080/Supermarket/sale/listSales";
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);
	}

	//@ requires true;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public static void listSubscriptions() throws RestRequestException {
		String uri = "http://localhost:8080/Supermarket/sale/listSubscriptions/"+username;
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);
	}

	//@ requires product != null;
	//@ requires product.length() >= 2;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public static void subscribeProduct(String product) throws RestRequestException {
		String uri = "http://localhost:8080/Supermarket/sale/subscribe/"
				+ username + "/"
				+ product;
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);
	}

	//@ requires product != null && product.length() >= 2;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public static void unsubscribeProduct(String product) throws RestRequestException {
		String uri = "http://localhost:8080/Supermarket/sale/unsubscribe/"
				+ username + "/"
				+ product;
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);
	}

	//@ requires groupname != null && message != null;
	//@ requires groupname.length() >= 2;
	//@ ensures true;
	//@ signals_only RestRequestException;
	public static void sendMessage(String groupname, String message) throws RestRequestException {
		message = message.replaceAll(" ", "+");

		String uri = "http://localhost:8080/RestServer/restapi/groupServer/sendMessage"
				+ "?groupname="+groupname+"&username="+username+"&message="+message;
		String response = httpUtils.httpGetRequest(uri, headerParams);

		System.out.println(response);
	}

	private class MessageListener extends Thread{

		String uri = "http://localhost:8080/Supermarket/getUpdates/"
				+username;

		//@ also requires true;
		//@ ensures true;
		//@ signals_only InterruptedException;
		public void run() {
			while(option != 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				String response = null;
				try {
					response = httpUtils.httpGetRequest(uri, headerParams);
				} catch (RestRequestException e) {
					e.printStackTrace();
				}

				if(response != null && !response.isEmpty()) System.out.println(response);
			}
		}
	}
}
