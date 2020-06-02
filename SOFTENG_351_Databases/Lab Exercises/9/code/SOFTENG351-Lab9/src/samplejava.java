import java.sql.*;

public class samplejava {
	public static void main(String args[]) {
		String username = "abur970"; //"your UPI";       
		String password = "Nili0318!"; //"your password";       
		String url = "jdbc:mysql://127.0.0.1:3306/stu_abur970_SOFTENG_351_C_S1_2020"; //e.g. "jdbc:mysql://127.0.0.1:3306/stu_UPI_COMPSCI_351_C_S1_2020"

		// Loads the JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			// Establishes a connection
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected");
			// Creates a statement
			Statement stmt = conn.createStatement();

			/************************Please adapt the following code to complete Lab 9**********************/ 
			// Executes a statement
			String command = "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE "
					+ "TABLE_SCHEMA='stu_abur970_SOFTENG_351_C_S1_2020_A2_Q1' AND "
					+ "TABLE_NAME='PROJECT'";
			// Obtains the results as a set of rows
			System.out.println(command);
			ResultSet result = stmt.executeQuery(command);
			boolean found = false;
			while (result.next()) {
				if (result.getString(1).equals("Hours")) {
					found = true;
				}
			}
			if (!found) {
				System.out.println("Not found");
				conn.prepareStatement("ALTER TABLE PROJECT ADD COLUMN Hours FLOAT").executeUpdate();
				conn.prepareStatement("UPDATE PROJECT SET Hours=(SELECT IFNULL(SUM(Hours), 0) "
						+ "FROM WORKS_ON WHERE Pno=Pnumber)").executeUpdate();
			}
			result = conn.prepareStatement("SELECT Pname, Hours FROM PROJECT").executeQuery();
			while (result.next()) {
				String name = result.getString(1);
				float hours = result.getFloat(2);
				if (hours < 100) {
					System.out.println(name + " Short");
				} else {
					System.out.println(name + " Long");
				}
			}
			/************************PLease adapt the above code to complete Lab 9**********************/
			// closes the connection (optional)
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}