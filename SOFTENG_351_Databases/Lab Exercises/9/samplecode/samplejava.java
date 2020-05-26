import java.sql.*;

public class samplejava {
	public static void main(String args[]) {
		String username = ""; // "your UPI";
		String password = ""; // "your password";
		String url = "jdbc:mysql://127.0.0.1:3306/teaching_mqia842_351_C_S1_2020"; // your database

		// Loads the JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			// Establishes a connection
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected");
			// Creates a statement
			Statement stmt = conn.createStatement();

			/************************PLease adapt the following code to complete Lab 9**********************/ 
			// Executes a statement
			String command = "SELECT * " + "FROM DEPARTMENT";
			// Obtains the results as a set of rows
			System.out.println(command);
			ResultSet result = stmt.executeQuery(command);
			while (result.next()) {
				for (int i = 1; i <= 4; i++) {
					if (i > 1)
						System.out.print('\t');
					System.out.print(result.getString(i));
				}
				System.out.println();
			}
			/************************PLease adapt the aboce code to complete Lab 9**********************/
			// closes the connection (optional)
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}