package com.capg;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.capg.AddressBookService.IOService;

public class AddressBookTest {
	@Test
	    public void givenEmpPayrollDataInDB_ShouldMatchEmpCount() {
	    	AddressBookService addBookService = new AddressBookService();
	    	List<AddressBookData> addBookData = addBookService.readAddresBookData(IOService.DB_IO);
	    	Assert.assertEquals(6, addBookData.size());
	    }
	
	@Test
	public void givenNewCity_WhenUpdated_shouldMatchWithDB() {
    	AddressBookService service = new AddressBookService();
    	service.readAddresBookData(IOService.DB_IO);
    	service.updateContactsCity("Gurkirat", "Goa");
    	boolean result = service.checkAddressBookDataInSyncWithDB("Gurkirat","Goa");
		Assert.assertTrue(result);
    }

	@Test
	public void givenName_ShouldReturn_DateAdded() {
		AddressBookService service = new AddressBookService();
		service.readAddresBookData(IOService.DB_IO);
    	LocalDate startDate = LocalDate.of(2019, 01, 01);
    	LocalDate endDate = LocalDate.now();
    	List<AddressBookData> addBookData = service.readAddressBookForDateRange(IOService.DB_IO, startDate, endDate);
    	Assert.assertEquals(2, addBookData.size());
	}
}
