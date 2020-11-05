package com.capg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService addBookDB;

	public AddressBookDBService() {
	}

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
				String city = result.getString("book_name");
				String state = result.getString("first_name");
				int zip = result.getInt("zip");
				addressBookList.add(new AddressBookData(id, bookName, firstName, lastName, city, state, zip));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
}
