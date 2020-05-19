import java.sql.*; 


public class connect {    
	public static void main(String args[]) {       
		String username = "abur970"; //"your UPI";       
		String password = "Nili0318!"; //"your password";       
		String url = "jdbc:mysql://127.0.0.1:3306/stu_abur970_SOFTENG_351_C_S1_2020"; //e.g. "jdbc:mysql://127.0.0.1:3306/stu_UPI_COMPSCI_351_C_S1_2020"

		   
	//Loads the JDBC driver  
		try {   
			Class.forName("com.mysql.jdbc.Driver");   
			System.out.println("Driver loaded");       
		 //Establishes a connection          
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected");          
		 //Creates a statement          
			Statement stmt= conn.createStatement(); 
		 //Executes a statement          
			String command = "SELECT * " + "FROM DEPARTMENT";         
		 //Obtains the results as a set of rows    
			System.out.println(command);      
			ResultSet result = stmt.executeQuery(command);      

		  //Obtains the metadata associated with the results 

			ResultSetMetaData  metaData = result.getMetaData(); 
			//Obtains the number of columns         
			int columnCount = metaData.getColumnCount(); 
			System.out.println(columnCount);       
  			//Prints the names of the columns obtained from the metadata        
			for (int i=1; i<=columnCount; i++) { 
				if (i > 1) System.out.print('\t');   
				System.out.print(metaData.getColumnLabel(i));          
			}          
			System.out.println();       
			System.out.println("-----------------------------------------------------");     
            
            //Iterates through the results and prints the tuples (rows)         
			while (result.next()) {          
				for (int i=1; i<= columnCount; i++) {       
					if (i>1) System.out.print('\t');              
					System.out.print(result.getString(i));              
				}             
				System.out.println();        
			}        
                                                         //closes the connection  (optional)      
			conn.close();      
		}    
		catch (Exception e) {   
			e.printStackTrace();
		}  
	}     
}                 