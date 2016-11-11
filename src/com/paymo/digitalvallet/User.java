package com.paymo.digitalvallet;

/**
 * User class. Encapsulates a user entity in a transaction.
 * 
 * @author Mrudula
 *
 */
public class User {
	/**
	 * User id , which identifies a user in a transaction
	 */
	private Integer userId;
	/**
	 * Indicator to set visited property true for this user in the graph. Not
	 * used at present.
	 */
	private boolean visited = false;

	public User(Integer id) {
		this.userId = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		hashcode = userId * 20;
		hashcode += userId.hashCode();
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u2 = (User) obj;
			return this.getUserId().equals(u2.getUserId());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", visited=" + visited + "]";
	}

}
