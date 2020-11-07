package com.capg;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capg.AddressBookService.IOService;

public class AddressBookTest {
	
	private AddressBookService addressBookService;

	@Before
	public void initialise() {
		addressBookService = new AddressBookService();
	}

	@Test
	    public void givenEmpPayrollDataInDB_ShouldMatchEmpCount() {
	      	List<AddressBookData> addBookData = addressBookService.readAddresBookData(IOService.DB_IO);
	    	Assert.assertEquals(6, addBookData.size());
	    }
	
	@Test
	public void givenNewCity_WhenUpdated_shouldMatchWithDB() {
    	addressBookService.readAddresBookData(IOService.DB_IO);
    	addressBookService.updateContactsCity("Gurkirat", "Goa");
    	boolean result = addressBookService.checkAddressBookDataInSyncWithDB("Gurkirat","Goa");
		Assert.assertTrue(result);
    }

	@Test
	public void givenName_ShouldReturn_DateAdded() {
		addressBookService.readAddresBookData(IOService.DB_IO);
    	LocalDate startDate = LocalDate.of(2019, 01, 01);
    	LocalDate endDate = LocalDate.now();
    	List<AddressBookData> addBookData = addressBookService.readAddressBookForDateRange(IOService.DB_IO, startDate, endDate);
    	Assert.assertEquals(2, addBookData.size());
	}
	
	@Test
	public void givenCityOrState_ShouldReturn_TotalContacts() {
		addressBookService.readAddresBookData(IOService.DB_IO);
    	int no_Of_Contacts = addressBookService.getCountOfPerson("Rohtak","Punjab");
    	Assert.assertEquals(3, no_Of_Contacts);
	}
	
	@Test
	public void givenAddressBookDBWhenAddedContactToDB_shouldMatchCount() {
		/*List<AddressBookData> personsData = */addressBookService.readAddresBookData(IOService.DB_IO);
		addressBookService.addContactToAddressBookDB(7, "book1", "Lamar", "Antony", "Baranala", "Punjab", 123456, "2020-05-06");
		boolean result = addressBookService.checkAddressBookInSyncWithDB(7);
		Assert.assertTrue(result);
	}
}
