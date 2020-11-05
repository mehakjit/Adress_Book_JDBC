package com.capg;

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
}
