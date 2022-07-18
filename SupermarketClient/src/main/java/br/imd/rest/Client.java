package br.imd.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.imd.rest.expections.RestRequestException;

public class Client {

	static Map<String, String> headerParams;
	static String username;
	static int option = -1;
	
	public Client() {
		headerParams = new HashMap<String, String>();
		headerParams.put("accept", "application/json");
	}	

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
			System.out.println("2 - Subscribe to recieve updates on a product.");
			System.out.println("3 - Unsubscribe from updates on product.");
			System.out.println("==========================================");
			option = sc.nextInt();
			sc.nextLine();
			
			if(option == 1) {
				listSales();
			}
			else if(option == 2) {
				String product;
				System.out.println("Inform the name of the product you want to recieve updates for: ");
				product = sc.nextLine();
				subscribeProduct(product);
			}
			else if(option == 3) {
				String product;
				System.out.println("Inform the name of the product you want to stop recieving updates for: ");
				product = sc.nextLine();
				unsubscribeProduct(product);
			}				
		}		
	}
	
	public void setUsername() throws RestRequestException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String _username = "";
		
		System.out.println("Please, inform your username: ");
		_username = sc.nextLine();
		
		String uri = "http://localhost:8080/Supermarket/user/add/"+_username;
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);
		
		while(response.startsWith("ERROR")) {
			_username = sc.nextLine();
			uri = "http://localhost:8080/Supermarket/user/add/"+_username;
			response = HttpUtils.httpGetRequest(uri, headerParams);
			
			System.out.println(response);
		};
		
		username = _username;
	}	
	
	public static void listSales() throws RestRequestException {
		String uri = "http://localhost:8080/Supermarket/sale/listSales";
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);
	}
	
	public static void subscribeProduct(String product) throws RestRequestException {	
		String uri = "http://localhost:8080/Supermarket/sale/subscribe/"
				+ username + "/"
				+ product;
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);		
	}
	
	public static void unsubscribeProduct(String groupname) throws RestRequestException {		
		String uri = "http://localhost:8080/RestServer/restapi/groupServer/joinGroup"
						+ "?groupname="+groupname+"&username="+username;
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);
	}	
	
	public static void sendMessage(String groupname, String message) throws RestRequestException {	
		message = message.replaceAll(" ", "+");
		
		String uri = "http://localhost:8080/RestServer/restapi/groupServer/sendMessage"
				+ "?groupname="+groupname+"&username="+username+"&message="+message;
		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		System.out.println(response);		
	}
	
	private class MessageListener extends Thread{
		
		String uri = "http://localhost:8080/Supermarket/getUpdates/"
				+username;
		
		public void run() {
			while(option != 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				String response = null;
				try {
					response = HttpUtils.httpGetRequest(uri, headerParams);
				} catch (RestRequestException e) {
					e.printStackTrace();
				}
				
				if(response != null && response != "" && response != "\n" && response.length() != 0) System.out.println(response);
			}
		}
	}
}







