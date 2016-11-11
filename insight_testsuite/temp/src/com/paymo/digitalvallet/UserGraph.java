package com.paymo.digitalvallet;

/**
 * Methods to be supported by any implementation of UserGraph
 * 
 * @author Mrudula
 *
 */
public interface UserGraph {
	/**
	 * Adds a user transaction to the user graph data structure
	 * 
	 * @param t
	 *            UserTransaction
	 * @return boolean true if transaction is added to the user graph
	 *         successfully, otherwise false
	 * @see UserTransaction
	 */
	public boolean addTransaction(UserTransaction t);

	/**
	 * This method determines if this is first transaction between two users.
	 * True if it is, false otherwise.
	 * 
	 * @param t
	 *            UserTransaction
	 * @return boolean
	 * @see UserTransaction
	 */
	public boolean isFirstTransactionForUsers(UserTransaction t); // feature 1

	/**
	 * This method returns true if the transaction is between 2nd degree
	 * friends. That is they are friend of a friend for each other.
	 * 
	 * @param t
	 *            UserTransaction
	 * @return boolean
	 * @see UserTransaction
	 */
	public boolean isCommonFriendsTransaction(UserTransaction t); // feature 2

	/**
	 * This method determines if the users involved in a payment are within the
	 * 4th degree friends network.
	 * 
	 * @param t
	 *            UserTransaction
	 * @return boolean
	 */
	public boolean is4thDegreeFriendsTransaction(UserTransaction t); // feature
																		// 3

	/**
	 * Prints the entire graph, the Users and the users , they have had
	 * transactions with.
	 */
	public void printGraph();
}
