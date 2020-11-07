package com.capg;

import java.time.LocalDate;

public class AddressBookData {
	public int id;
	public String bookName;
	public String firstName;
	public String lastName;
	public String city;
	public String state;
	public int zip;
	LocalDate date;
	
	public AddressBookData(int id2, String bookName2, String firstName2, String lastName2, String city2, String state2, int zip2, LocalDate now) {
		this.firstName = firstName2;
		this.lastName = lastName2;
		this.id = id2;
		this.city = city2;
		this.bookName = bookName2;
		this.state = state2;
		this.zip = zip2;
		this.date = now;
	}
	
	public AddressBookData(int id, String bookName, String firstName, String lastName, String city, String state, int zip) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.city = city;
		this.bookName = bookName;
		this.state = state;
		this.zip = zip;
	}
	
}
