package server;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import java.sql.Statement;

public class Info {

	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "root";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed
	 * with MySQL)
	 */
	// private final String dbName = "ChatBot";

	/** The name of the table we are testing with */
	private final String tableName1 = "SignIn";
	private String tableName2 = " ";

	// private final String tableName3 = "Course";
	Connection conn = null;

	private Statement stmt;

	public int checkDuplicates(String name, String Username, String email, String pass) {

		String sql = "SELECT * FROM SignIn";
		int k = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String UName = rs.getString("USERNAME");
				String Email = rs.getString("EMAIL");
				if (UName.equals(Username) == true) {
					k = 1;
					System.out.println(k);
					break;
				} else if (Email.equals(email) == true) {
					k = 2;
					System.out.println(k);
					break;
				}
			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return k;
	}

	public void giveFeedback(){

	}
	public void addUser(String name, String Username, String email, String pass, String port, String host) {
		String input1 = "INSERT INTO SignIn" + " VALUES ( "

				+ "'" + name + "'" + "," + "'" + Username + "'" + "," + "'" + email + "'" + "," + "'" + pass + "'" + ","
				+ "'" + host + "'" + "," + "'" + port + "'" + ")";
		try {
			this.executeUpdate(conn, input1);
			System.out.println("Successfully created account ");
			Createfriend(Username);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// controller.setusername(Username);
	}

	public String returnPort(String Username) {
		String sql = "SELECT * FROM SignIn";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String UName = rs.getString("USERNAME");
				String port = rs.getString("PORT");

				if (UName.equals(Username) == true) {
					return port;
				}

			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}

	public ArrayList<String> getFriendsNames(String user) {
		connect();
		ArrayList<String> temp = new ArrayList<String>();
		String sqlStatement = "SELECT * From " + user;

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStatement);

			while (rs.next()) {
				String username = rs.getString("FRIENDS");
				temp.add(username);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
	}

	public String returnHost(String Username) {
		String sql = "SELECT * FROM SignIn";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String UName = rs.getString("USERNAME");
				String host = rs.getString("HOSTNAME");

				if (UName.equals(Username) == true) {
					return host;
				}

			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}

	public void connect() {
		try {
			System.out.println("in connect");
			conn = this.getConnection();

			// System.out.println("connection name is ::
			// "+conn.getClass().getName());
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		// System.out.println("trying to get connection!! ");
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + "DocPort",
				connectionProps);
		System.out.println(" Connection achieved!! ");
		return conn;
	}

	public int validateAccount(String Email, String Pass, String host, String port) {
		String user = " ";
		String sql = "SELECT * FROM SignIn";
		int k = -1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String email = rs.getString("EMAIL");
				String pass = rs.getString("PASSWORD");
				user = rs.getString("USERNAME");
				if (email.equals(Email) == true) {
					if (pass.equals(Pass) == true) {
						k = 1; // validate

						// update port and host
						PreparedStatement updateS = conn.prepareStatement(
								"UPDATE SignIn SET " + "HOSTNAME" + " = ?" + " , PORT = ?" + " WHERE EMAIL LIKE ?");
						updateS.setString(1, host);
						updateS.setString(2, port);
						updateS.setString(3, email);
						System.out.println(updateS);
						updateS.executeUpdate();
						System.out.println("Successfully updated");
						break;
					}

					else {
						k = 0; // Incorrect Password
						break;
					}

				}
			}

			// controller.setusername(user);

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return k;
	}

	public void makeTableUser() {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			System.out.println("Connection Name is :: " + conn1.getClass().getName());
			System.out.println("Connected to database");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("in try");

			String createString = "CREATE TABLE User" + " ( " + "userId INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "
					+ "name varchar(40) NOT NULL, " + "userName varchar(40) NOT NULL UNIQUE, "
					+ "phoneNumber varchar(15), " + "port INTEGER NOT NULL, " + "IP varchar(15) NOT NULL, "
					+ "status BOOL, " + "password varchar(20) NOT NULL) ";

			this.executeUpdate(conn1, createString);

			String input = "INSERT INTO User " + "VALUES (" + null
					+ ", 'Hasan', 'Drhasan', '051-4864428', 1234, 'local host', 0, '1234'  )";
			this.executeUpdate(conn1, input);
			String input1 = "INSERT INTO User " + "VALUES (" + null
					+ ", 'Ali', 'ali', '051-1234567', 1234, 'local host', 0, '1234')";

			this.executeUpdate(conn1, input1);

			String input2 = "INSERT INTO User " + "VALUES (" + null
					+ ", 'LAfarukh ', 'LAfarukh', '051-1234567', 1234, 'local host', 0, '1234')";

			this.executeUpdate(conn1, input2);

			// Query

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// deleteAll(conn1);
	}

	public void makeTableFriend() {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			System.out.println("Connection Name is :: " + conn1.getClass().getName());
			System.out.println("Connected to database");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			String createString = "CREATE TABLE Friend" + " ( " + "userId INTEGER NOT NULL REFERENCES User(userId), "
					+ "friendId INTEGER NOT NULL REFERENCES User(userId)) ";

			this.executeUpdate(conn1, createString);

			String input = "INSERT INTO Friend " + "VALUES (1,2)";

			this.executeUpdate(conn1, input);

			String input1 = "INSERT INTO Friend " + "VALUES (2,1)";

			this.executeUpdate(conn1, input1);

			String input2 = "INSERT INTO Friend " + "VALUES (1,3)";

			this.executeUpdate(conn1, input2);

			String input4 = "INSERT INTO Friend " + "VALUES (3,1)";

			this.executeUpdate(conn1, input4);

			// Query

		} catch (Exception e) {
			System.out.println("Error: Could not create the table");
			return;
		}

		// deleteAll(conn1);
	}

	public void makeUser(String name, String email, String password) {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			System.out.println("Connection Name is :: " + conn1.getClass().getName());
			System.out.println("Connected to database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String input1 = "INSERT INTO USER  VALUES ( " + null + "," + "'" + name + "'" + "," + "'" + name + "'" + ","
				+ "'" + email + "'" + "," + "'" + "1234" + "'" + "," + "'" + "localhost" + "'" + "," + "'" + "1" + "'"
				+ "," + "'" + password + "'" + ")";
		try {
			this.executeUpdate(conn1, input1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void StoreMessage(String from, String to, String msg) {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			System.out.println("Connection Name is :: " + conn1.getClass().getName());
			System.out.println("Connected to database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String input = "INSERT INTO Message " + "VALUES (" + null + ", '" + from + "','" + to + "','" + msg + "')";
		try {
			this.executeUpdate(conn1, input);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void makeTableMessage() {
		Connection conn1 = null;
		try {
			conn1 = this.getConnection();
			System.out.println("Connection Name is :: " + conn1.getClass().getName());
			System.out.println("Connected to database");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			String createString = "CREATE TABLE Message" + " ( "
					+ "messageId INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " + "Sender varchar(10) , "
					+ "Receiver varchar(10) , " + "text varchar(1000) NOT NULL )";

			this.executeUpdate(conn1, createString);

			// Query

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: Could not create the table");
			return;
		}

		// deleteAll(conn1);
	}

	public String GetUSername(String Email) {
		String sql = "SELECT * FROM SignIn";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String UName = rs.getString("USERNAME");
				String email = rs.getString("EMAIL");

				if (email.equals(Email) == true) {
					return UName;
				}

			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}

	public void Createfriend(String Username) {
		tableName2 = Username;

		String createString1 = "CREATE TABLE " + this.tableName2 + " ( " + "FRIENDS VARCHAR(70) NOT NULL PRIMARY KEY, "
				+ "FRIENDSHIPDATE VARCHAR(80) NOT NULL) ";
		try {

			this.executeUpdate(conn, createString1);
			System.out.println("Successfully Created Friends table ");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void AddFriend(String name, String user) {
		int t = CheckFriend(name, user);

		if (t == 1) {
			Date date = new Date();
			String input1 = "INSERT INTO " + name + " " + " VALUES ( " + "'" + user + "'" + "," + "'" + date + "'"
					+ ")";
			try {

				this.executeUpdate(conn, input1);
				System.out.println("Successfully Friend added ");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		try {
			Statement stmt = null;
			stmt = (Statement) conn.createStatement();
			stmt.executeUpdate(command);
			return true;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public int CheckFriend(String table, String Username) {

		String sql = "SELECT * FROM " + table;
		int k = 1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String UName = rs.getString("FRIENDS");

				if (UName.equals(Username) == true) {
					k = -1;
					// System.out.println(UName);
					break;
				}

			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return k;
	}

	public ArrayList<String> getUserNames() {
		connect();
		ArrayList<String> temp = new ArrayList<String>();
		String sqlStatement = "SELECT * From SignIn";

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStatement);
			while (rs.next()) {
				String username = rs.getString("USERNAME");
				temp.add(username);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
	}

	public String GetUsername(String host, String port) {
		String sql = "SELECT * FROM SignIn";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String h = rs.getString("HOSTNAME");
				String p = rs.getString("PORT");
				String user = rs.getString("USERNAME");

				if (h.equals(host) == true && p.equals(port) == true) {
					return user;
				}

			}
			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}

	public void run() {
		System.out.println("In run ");
		// Connect to MySQL
		connect();
		// Create a table
		System.out.println("Creating table");
		try {
			String createString = "CREATE TABLE " + this.tableName1 + " ( " + "NAME VARCHAR(30) NOT NULL, "
					+ "USERNAME varchar(50) NOT NULL UNIQUE, " + "EMAIL VARCHAR(70) NOT NULL PRIMARY KEY, "
					+ "PASSWORD VARCHAR(80) NOT NULL , " + "HOSTNAME VARCHAR(70) NOT NULL, "
					+ "PORT VARCHAR(80) NOT NULL)";

			// Creating Sign in table
			this.executeUpdate(conn, createString);

			System.out.println("Created  table");
		} catch (Exception e) {
			System.out.println("ERROR: Could not create the table");
			e.printStackTrace();
			return;
		}
	}
}
