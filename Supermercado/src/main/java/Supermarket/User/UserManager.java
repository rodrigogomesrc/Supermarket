package Supermarket.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

	//@ public invariant users != null;
	//@ public invariant updatesList != null;

	//@ spec_public
	static HashMap<String, ArrayList<String>> users = new HashMap<String, ArrayList<String>>();
	//@ spec_public
	static HashMap<String, ArrayList<String>> updatesList = new HashMap<String, ArrayList<String>>();

	//@ ensures \result == users;
	//@ pure
	public static HashMap<String, ArrayList<String>> getUsers() {
		return users;
	}

	//@ requires users != null;
	//@ ensures UserManager.users == users;
	public static void setUsers(HashMap<String, ArrayList<String>> users) {
		UserManager.users = users;
	}

	//@ requires username != null;
	//@ ensures \result == true || \result == false;
	//@ pure
	public static boolean checkIfUserExists(String username) {
		return UserManager.getUsers().containsKey(username);
	}

	//@ requires username != null;
	//@ ensures \result != null;
	//@ pure
	public static ArrayList<String> getUserSubscriptions(String username) {
		return users.get(username);
	}

	//@ requires username != null;
	public static void addUser(String username) {
		users.put(username, new ArrayList<String>());
	}

	//@ requires username != null && product != null;
	public static void addSubscriptionToUser(String username, String product) {
		users.get(username).add(product);
	}

	//@ requires username != null && product != null;
	public static void removeSubscriptionFromUser(String username, String product) {
		users.get(username).remove(product);
	}

	//@ requires product != null;
	//@ ensures \result == true || \result == false;
	public static boolean chechIfProductHasSubscriptions(String product) {
		boolean productAlreadyWatched = false;

		for (ArrayList<String> productList : UserManager.getUsers().values()) {
			for (String userProduct : productList) {
				/*if (userProduct.toLowerCase().equals(product.toLowerCase())) {
					productAlreadyWatched = true;
					break;
				}*/
			}
		}

		return productAlreadyWatched;
	}

	//@ ensures \result == updatesList;
	//@ pure
	public static HashMap<String, ArrayList<String>> getUpdatesList() {
		return updatesList;
	}

	//@ requires updatesList != null;
	public static void setUpdatesList(HashMap<String, ArrayList<String>> updatesList) {
		UserManager.updatesList = updatesList;
	}

	//@ requires username != null;
	//@ ensures \result != null;
	//@ pure
	public static ArrayList<String> getUserUpdates(String username) {
		return updatesList.get(username);
	}

	//@ requires product != null && price != null;
	//@ requires updatesList != null;
	public static void addUpdatesList(String product, String price) {
		ArrayList<String> usernames = getUsersInterestedInProduct(product);
		String updateText = "Product: " + product + " / Price: " + price + "\n";

		for (String username : usernames) {
			boolean contains = false;

			/*if (updatesList.containsKey(username)) {
				for (String localUpdateText : getUserUpdates(username)) {
					if (localUpdateText.toLowerCase().equals(updateText.toLowerCase())) {
						contains = true;
						break;
					}
				}

				if (!contains) {
					//getUserUpdates(username).add(updateText);
				}
			} else {
				ArrayList<String> s = new ArrayList<>();
				s.add(updateText);
				updatesList.put(username, s);
			}*/
		}
	}

	//@ requires username != null;
	public static void removeUpdatesList(String username) {
		updatesList.remove(username);
	}

	//@ requires product != null;
	//@ ensures \result != null;
	//@ pure
	public static ArrayList<String> getUsersInterestedInProduct(String product) {
		ArrayList<String> usersFound = new ArrayList<>();

		for (String user : UserManager.getUsers().keySet()) {
			/*for (String userProduct : users.get(user)) {
				/*if (userProduct.toLowerCase().equals(product.toLowerCase())) {
					usersFound.add(user);
				}
			}*/
		}

		return usersFound;
	}
}










