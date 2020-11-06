package com.capg;

public class AddressBookData {
	public int id;
	public String bookName;
	public String firstName;
	public String lastName;
	public String city;
	public String state;
	public int zip;
	
	public AddressBookData() {}
	
	public AddressBookData(int id, String bookName, String firstName, String lastName, String city, String state, int zip) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.city = city;
		this.bookName = bookName;
		this.state = state;
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "AddressBookData [firstName=" + firstName + ", city=" + city + "]";
	}
	
}
