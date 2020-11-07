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

		public int countPersonUsing_PreparedStatement(String cityName, String stateName) {
			String sql = String.format(" select count(id) as count from address where city='%s' or state= '%s';", cityName, stateName);
			try(Connection connection = this.getConnection()) {
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				result.first();
				return result.getInt("count");
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			return 0;
		}

		public AddressBookData addContactToAddressBookDB(int id, String bookName, String firstName, String lastName,
				String city, String state, int zip, String now) {
			int personId = -1;
			AddressBookData contact = null;
			Connection connection = null;
			try {
				connection = this.getConnection();
				connection.setAutoCommit(false);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try (Statement statement = connection.createStatement()) {
				String sql = String.format(
						"insert into addressbook(id,book_name,first_name, last_name, date_added) "
								+ "values (%s, '%s', '%s', '%s', '%s');",
						id, bookName, firstName, lastName, Date.valueOf(now));
				int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
				if (rowAffected == 1) {
					ResultSet resultSet = statement.getGeneratedKeys();
					if (resultSet.next())
						personId = resultSet.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
					return contact;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			try (Statement statement = connection.createStatement()) {
				String sql = String.format("insert into address (id, street, city, state, zip) values (%s, '%s', '%s','%s', %s);",
						id, city, state, zip);
				int rowAffected = statement.executeUpdate(sql);
				if (rowAffected == 1) {
					contact = new AddressBookData(id, bookName, firstName, lastName, city, state, zip, LocalDate.now());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println(contact);
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return contact;
		}
	}

