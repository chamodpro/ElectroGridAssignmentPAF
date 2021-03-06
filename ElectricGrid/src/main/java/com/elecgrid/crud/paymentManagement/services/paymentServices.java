package com.elecgrid.crud.paymentManagement.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class paymentServices {
	
	//connection
		private Connection connect() {
			Connection con = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				String url = String.format("jdbc:mysql://localhost:3306/elecgrid");
				String username = "root";
				String password = "";
				
				con = DriverManager.getConnection(url,username, password);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return con;
		}
		
		//insert payment details
		public String insertPayDetails(String pay_type, String amount, String cus_id, String bill_id) {
			
			String output = "";
			
			try {
				
				Connection con = connect();
				
				if(con == null)
				{return "Error while connecting to the database for inserting data";}
				
				String insertQuery = "insert into payment (`pay_id`, `pay_type`,`amount`, `cus_id`, `bill_id`)" + "values(?,?,?,?,?)";
				
				PreparedStatement ps = con.prepareStatement(insertQuery);
				ps.setInt(1, 0);
				ps.setString(2, pay_type);
				ps.setString(3, amount);
				ps.setString(4, cus_id);
				ps.setString(5, bill_id);

				ps.execute();
				con.close();

			} catch(Exception e) {
				output = "Payment does not go through.. somthing went wrong!.";
				System.err.println(e.getMessage());
			}
			
			return output;
		}
		
		//view all payments
		public String viewPayments() {
			
			String output ="";
			
			try {
				
				Connection con = connect();
				
				if (con==null)
				{ return "Error!! While connecting to the database for read the payments";}
				
				// Prepare the html table to be displayed
				output = "<table border='1'><tr><th>id</th><th>Payment Type</th>" +
				"<th>Amount (Rs.)</th>" +
				"<th>Update</th><th>Remove</th></tr>";
				
				String query = "select * from payment";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					
					String pay_id = Integer.toString(rs.getInt("pay_id"));
					String pay_type = rs.getString("pay_type");
					String amount = rs.getString("amount");
					
					// Add into the html table
					output += "<tr><td>" + pay_id + "</td>";
					output += "<td>" + pay_type + "</td>";
					output += "<td>" + amount + "</td>";
					
					// buttons
					output += "<td><input name='btnUpdate' type='button' value='Update'class='btn btn-secondary'></td>"
					+ "<td><form method='post' action='items.jsp'>"
					+ "<input name='btnRemove' type='submit' value='Remove'class='btn btn-danger'>"
					+ "<input name='cus_id' type='hidden' value='" + pay_id
					+ "'>" + "</form></td></tr>";
					
				}
				
				con.close();
				
				output += "</table>";
			} catch (Exception e) {
				output = "Error while reading payments";
				System.err.println(e.getMessage());
			}
			return output;
		}
		
		//update payment --> Payment update is unnecessary <--
		public String updatePayment(String pay_id, String pay_type, String amount) {
			
			String output="";
			
			try {
				
				Connection con = connect();
				
				if (con==null)
				{ return "Error!! While connecting to the database for updating the " + pay_id;}
				
				// create a prepared statement
				String query = "UPDATE payment SET pay_type=?, amount=?, WHERE cus_id=?";
				
				PreparedStatement preparedStmt = con.prepareStatement(query);
				
				// binding values
				preparedStmt.setString(1, pay_type);
				preparedStmt.setString(2, amount);
				preparedStmt.setInt(3,Integer.parseInt(pay_id));
				
				// execute the statement
				preparedStmt.execute();
				
				con.close();
				
				output = "Updated payment successfully";
				
			} catch (Exception e) {
				
				output = "Error while updating the " + pay_id;
				System.err.println(e.getMessage());
			}
			
			return output;
		}
		
		//delete
		public String deleteCustomer(String pay_id)
		{
			String output = "";
			try
			{
			Connection con = connect();
			
			if (con == null)
			{return "Error while connecting to the database for deleting."; }
			
			// create a prepared statement
			String query = "delete from payment WHERE pay_id=?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(pay_id));
			
			// execute the statement
			preparedStmt.execute();
			
			con.close();
			output = "<h1>Deleted paymen detail successfully</h1>";
			}
			catch (Exception e)
			{
				output = "Error while deleting the payment detail.";
				System.err.println(e.getMessage());
			}
		return output;
		}
		
}
