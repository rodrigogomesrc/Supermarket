package Supermarket.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

	static HashMap<String, ArrayList<String>> users = new HashMap<String, ArrayList<String>>();
	//Username, Subscriptions
	
	static HashMap<String, ArrayList<String>> updatesList = new HashMap<String, ArrayList<String>>();
	//Username, updates

	public static HashMap<String, ArrayList<String>> getUsers() {
		return users;
	}

	public static void setUsers(HashMap<String, ArrayList<String>> users) {
		UserManager.users = users;
	}
	
	public static boolean checkIfUserExists(String username) {
		return UserManager.getUsers().containsKey(username);
	}
	
	public static ArrayList<String> getUserSubscriptions(String username){
		return users.get(username);
	}
	
	public static void addUser(String username) {
		users.put(username, new ArrayList<String>());
	}
	
	public static void addSubscriptionToUser(String username, String product) {
		users.get(username).add(product);
	}
	
	public static void removeSubscriptionFromUser(String username, String product) {
		users.get(username).remove(product);
	}
	
	public static boolean chechIfProductHasSubscriptions(String product) {
		boolean productAlreadyWatched = false;
		
		for(ArrayList<String> productList : UserManager.getUsers().values()) {
			for(String userProduct : productList) {
				if(userProduct.toLowerCase().equals(product.toLowerCase())) {
					productAlreadyWatched = true;
					break;
				}
			}
		}
		
		return productAlreadyWatched;
	}

	public static HashMap<String, ArrayList<String>> getUpdatesList() {
		return updatesList;
	}

	public static void setUpdatesList(HashMap<String, ArrayList<String>> updatesList) {
		UserManager.updatesList = updatesList;
	}
	
	public static ArrayList<String> getUserUpdates(String username) {
		return updatesList.get(username);
	}
	
	public static void addUpdatesList(String product, String price) {
		ArrayList<String> usernames = getUsersInterestedInProduct(product);
		String updateText = "Product: " + product + " / Price: " + price + "\n";
		
		for(String username : usernames) {
			boolean contains = false;
			
			if(updatesList.containsKey(username)) {
				for(String localUpdateText : getUserUpdates(username)) {
					if(localUpdateText.toLowerCase().equals(updateText.toLowerCase())) {
						contains = true;
						break;	
					}
				}
				
				if(!contains) {
					getUserUpdates(username).add(updateText);
				}
			}
			else {
				ArrayList<String> s = new ArrayList<String>();
				s.add(updateText);
				updatesList.put(username, s);	
			}
		}
	}	
	
	public static void removeUpdatesList(String username) {
		updatesList.remove(username);
	}
	
	public static ArrayList<String> getUsersInterestedInProduct(String product){
		ArrayList<String> usersFound = new ArrayList<String>();
		
		for(String user : UserManager.getUsers().keySet()) {
			for(String userProduct : users.get(user)) {
				if(userProduct.toLowerCase().equals(product.toLowerCase())) {
					usersFound.add(user);
				}
			}

		}
		
		return usersFound;
	}
}









