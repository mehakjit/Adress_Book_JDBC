package com.capg;

import java.util.List;

public class AddressBookService {
	
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private List<AddressBookData> addBookList;
	private AddressBookDBService addressBookDBService;
	
	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}
	
	public AddressBookService(List<AddressBookData> addBookList) {
		this();
		this.addBookList = addBookList;
	}
	
	public List<AddressBookData> readAddresBookData(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			this.addBookList = addressBookDBService.readData();
		}
		return this.addBookList;
	}
	
	private AddressBookData getAddressBookData(String name) {
		for (AddressBookData data : addBookList) {
			if (data.firstName.equals(name)) {
				return data;
			}
		}
		return null;
	}
	
	public void updateContactsCity(String firstname, String city) {
		int result = addressBookDBService.updateAddressBookData_Using_PreparedStatement(firstname, city);
		if (result == 0)
			return;
		AddressBookData addBookData = this.getAddressBookData(firstname);
		if (addBookData != null)
			addBookData.city = city;
	}
	
	public boolean checkAddressBookDataInSyncWithDB(String fname, String city) {
		for (AddressBookData data : addBookList) {
			if (data.firstName.equals(fname)) {
				if (data.city.equals(city)) {
					return true;
				}
			}
		}
		return false;
	}
}

