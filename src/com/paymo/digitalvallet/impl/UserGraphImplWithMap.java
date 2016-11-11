package com.paymo.digitalvallet.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.paymo.digitalvallet.User;
import com.paymo.digitalvallet.UserGraph;
import com.paymo.digitalvallet.UserTransaction;

/**
 * Implementation of UserGraph using Map. This is not a conventional
 * implementation of graph using edge list, adjacency list or map, or adjacency
 * matrix. It is more adapted for the problem in hand to find whether two users
 * are friend or friend, or 2nd degree or 4th degree friends.
 * 
 * It stores all unique users in a Map. The key being the User object itself,
 * and the value being a map again of all the users this user had transactions
 * with. The second container/map also has key as User object and value as the
 * UserTransaction object.
 * 
 * @author Mrudula
 * @see UserGraph
 * @see User
 * @see UserTransaction
 */
public class UserGraphImplWithMap implements UserGraph {

	private Map<User, Map<User, UserTransaction>> map = new HashMap<User, Map<User, UserTransaction>>();

	@Override
	public boolean addTransaction(UserTransaction t) {

		if (!isTransactionRecorded(t)) {
			if (map.containsKey(t.getId1())) {

				// put id2 in id1's value map, if its already present, then
				// ignore this transaction
				Map<User, UserTransaction> id1Value = map.get(t.getId1());
				if (!id1Value.containsKey(t.getId2())) {

					id1Value.put(t.getId2(), t);
				}

			} else {
				// put id1 key in map , create new value map for id1

				Map<User, UserTransaction> id1Value = new HashMap<User, UserTransaction>();

				id1Value.put(t.getId2(), t);
				map.put(t.getId1(), id1Value);

			}

			if (map.containsKey(t.getId2())) {

				// put id1 in id2's value map, if its already present, then
				// ignore this transaction
				Map<User, UserTransaction> id2Value = map.get(t.getId2());
				if (!id2Value.containsKey(t.getId1())) {

					id2Value.put(t.getId1(), t);
				}
			} else {

				Map<User, UserTransaction> id2Value = new HashMap<User, UserTransaction>();

				id2Value.put(t.getId1(), t);
				map.put(t.getId2(), id2Value);

			}
			// all okay, return true
			return true;

		}

		return false;

	}

	@Override
	public void printGraph() {
		if (map != null) {

			Set<User> keys = map.keySet();
			for (User i : keys) {
				Map<User, UserTransaction> values = map.get(i);
				Set<User> keys1 = values.keySet();
				System.out.println(" User " + i
						+ " had transactions with users " + keys1.toString());
			}
		}

	}

	/**
	 * If the two users had a transaction, the map will have both the users. So
	 * check if any of the users in present in primary map, then check in the
	 * secondary map, if the second user is present.
	 */
	@Override
	public boolean isFirstTransactionForUsers(UserTransaction t) {

		if (map.containsKey(t.getId1())
				&& map.get(t.getId1()).containsKey(t.getId2()))
			return false;
		else
			return true;

	}

	/**
	 * For both the users to have common friends, both of them should exist in
	 * the map. So check for any one of them in the primary map, if present
	 * iterate over the key set which denotes all the friends of this user, he
	 * has had transactions with. For each friend, get his friends from the
	 * primary map.
	 */
	@Override
	public boolean isCommonFriendsTransaction(UserTransaction t) {
		if (map.containsKey(t.getId1())) {
			Set<User> id1Friends = map.get(t.getId1()).keySet();
			for (User i : id1Friends) {
				if (map.get(i).keySet().contains(t.getId2()))
					return true;
			}

		}
		return false;
	}

	/**
	 * Extend the logic from isCommonFriendsTransaction method to check friends till 4th degree.
	 * Not a generic implementation. Can be made to accept n, the degree
	 * 
	 */
	@Override
	public boolean is4thDegreeFriendsTransaction(UserTransaction t) {
		//TODO make generic to check for any given degree of friends.
		//run a counter which increments for every for loop. Check if its value is <= given degree n
		Set<Integer> visited = new HashSet<Integer>();

		if (map.containsKey(t.getId1())) {
			// 1st level

			Set<User> friends1 = map.get(t.getId1()).keySet();

			if (friends1.contains(t.getId2()))
				return true;
			for (User i1 : friends1) {

				if (!visited.contains(i1.getUserId())) {
					visited.add(i1.getUserId());

					if (map.get(i1).keySet().contains(t.getId2())) {
						
						return true;
					} else {
						// 2nd level
						Set<User> friends2 = map.get(i1).keySet();

						for (User i2 : friends2) {

							if (!visited.contains(i2.getUserId())) {
								visited.add(i2.getUserId());

								if (map.get(i2).keySet().contains(t.getId2())) {
									
									return true;
								}
								else {
									// 3rd level
									Set<User> friends3 = map.get(i2).keySet();
									
									for (User i3 : friends3) {

										if (!visited.contains(i3.getUserId())) {
											visited.add(i3.getUserId());

											if (map.get(i3).keySet()
													.contains(t.getId2())) {
												
												return true;
											}
											

										}
									}

								}

							}
						}

					}
				}
			}

		}
		return false;
	}

	private boolean isTransactionRecorded(UserTransaction t) {
		// if map contains keys id1 and id2, and id1 value map contains id2 key,
		// id2 value map contains id1 key

		if (map.containsKey(t.getId1()) && map.containsKey(t.getId2())) {
			if (map.get(t.getId1()).containsKey(t.getId2())
					&& map.get(t.getId2()).containsKey(t.getId1())) {

				return true;
			}
		}

		return false;
	}

}
