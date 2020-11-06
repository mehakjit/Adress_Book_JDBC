package com.capg;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService addBookDB;

	public static AddressBookDBService getInstance() {
		if (addBookDB == null) {
			addBookDB = new AddressBookDBService();
		}
		return addBookDB;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressbookservice?useSSL=false";
		String userName = "root";
		String password = System.getenv("SQL_PASSWORD");
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		return connection;
	}

	public List<AddressBookData> readData() {
		String sql = "SELECT * FROM addressbook a inner join address c on a.id=c.id;";
		List<AddressBookData> addBookList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			addBookList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet result) {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String bookName = result.getString("book_name");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String city = result.getString("city");
				String state = result.getString("state");
				int zip = result.getInt("zip");
				addressBookList.add(new AddressBookData(id, bookName, firstName, lastName, city, state, zip));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
	
	public int updateAddressBookData_Using_PreparedStatement(String firstName, String city) {
		return this.updateAddressBookDataUsingPreparedStatement(firstName, city);
	}

	private int updateAddressBookDataUsingPreparedStatement(String firstName, String city) {
		String sql = String.format("update address set city= '%s' where id in (select id from addressbook where first_name='%s');", city, firstName);
		try(Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			return statement.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<AddressBookData> getAddressBookForDateRange(LocalDate startDate, LocalDate endDate) {
			String sql = String.format("SELECT * FROM addressbook a inner join address b on a.id=b.id WHERE date_added BETWEEN '%s' AND '%s'", Date.valueOf(startDate), Date.valueOf(endDate));
			return this.getAddressBookDataUSingDB(sql);
		}

		private List<AddressBookData> getAddressBookDataUSingDB(String sql) {
			List<AddressBookData> addBookList = new ArrayList<>();
			try(Connection connection = this.getConnection()) {
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				addBookList = this.getAddressBookData(result);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			return addBookList;
		}
}
