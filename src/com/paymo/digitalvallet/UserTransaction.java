package com.paymo.digitalvallet;

import java.util.Date;

/**
 * UserTransaction class for a transaction between two users.
 * e.g. 2016-11-02 09:46:59, 9167, 8658, 11.88, Caaaaaaab 
 * @author Mrudula
 *
 */
public class UserTransaction {
	
	Date date ;
	
	User id1;
	
	User id2;
	
	float amount;
	

	public UserTransaction(Date date, User id1, User id2, float amount) {

		this.date = date;
		this.id1 = id1;
		this.id2 = id2;
		this.amount = amount;

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getId1() {
		return id1;
	}

	public void setId1(User id1) {
		this.id1 = id1;
	}

	public User getId2() {
		return id2;
	}

	public void setId2(User id2) {
		this.id2 = id2;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "UserTransaction [date=" + date + ", id1=" + id1.getUserId() + ", id2="
				+ id2.getUserId() + ", amount=" + amount + "]";
	}
}
