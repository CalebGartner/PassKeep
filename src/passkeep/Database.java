package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


class Database {
   
   Database() {
      // First time connect() is called, it will create a new database if one does not already exist.
      connect();
      createNewTable();
   }
   
   // Connects to database if already created and automatically creates one if not.
   private Connection connect() {
      // SQLite connection from driver to database's relative path
      String path = "jdbc:sqlite:passKeep.db";
      Connection conn = null;
      try {
         conn = DriverManager.getConnection(path);
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
      return conn;
   }
   
   // Creates a new table in the database if one does not already exist.
   private static void createNewTable() {
      // SQLite connection from driver to database's relative path
      String path = "jdbc:sqlite:passKeep.db";
      
      // SQL statement for creating a new table
      String sqlAccounts = "CREATE TABLE IF NOT EXISTS accounts (\n"
                              + "id integer PRIMARY KEY ,\n"
                              + "accountName text NOT NULL ,\n"
                              + "userName text NOT NULL,\n"
                              + "password text NOT NULL,\n"
                              + "URL text NOT NULL,\n"
                              + "notes text NOT NULL\n"
                              + ");";
      
      try (Connection newConnect = DriverManager.getConnection(path);
           Statement newConnectStatement = newConnect.createStatement()) {
         // create a new table
         newConnectStatement.execute(sqlAccounts);
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
   }
   
   // Creates and returns a List of all accounts in the database using the PassKeepAccount class. With the exception of the root user account at rowid 1.
   List<PassKeepAccount> getList() {
      String sql = "SELECT id, accountName, userName, password, URL, notes FROM accounts";
      List<PassKeepAccount> accountList = new ArrayList<PassKeepAccount>();
      
      try (Connection conn = this.connect();
           Statement stmt  = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql)
      ) {
         while (rs.next()) {
            if (rs.getInt("id") > 1) {
               PassKeepAccount newAccount = new PassKeepAccount();
               newAccount.setID(rs.getInt("id"));
               newAccount.setAccountName(rs.getString("accountName"));
               newAccount.setUserName(rs.getString("userName"));
               newAccount.setPassword(rs.getString("password"));
               newAccount.setURL(rs.getString("URL"));
               newAccount.setNotes(rs.getString("notes"));
               accountList.add(newAccount);
            }
         }
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
      return accountList;
   }
   
   // Returns PassKeepAccount object from database at specified index.
   PassKeepAccount get(int id){
      String sql = "SELECT accountName, userName, password, URL, notes FROM accounts WHERE id = ?";
      PassKeepAccount newAccount = new PassKeepAccount();
      
      try (Connection conn = this.connect();
           PreparedStatement pstmt  = conn.prepareStatement(sql)){
         
         // set the value
         pstmt.setInt(1,id);
         //
         ResultSet rs  = pstmt.executeQuery();
         
         // loop through the result set
         while(rs.next()) {
            newAccount.setAccountName(rs.getString("accountName"));
            newAccount.setUserName(rs.getString("userName"));
            newAccount.setPassword(rs.getString("password"));
            newAccount.setURL(rs.getString("URL"));
            newAccount.setNotes(rs.getString("notes"));
         }
         return newAccount;
      } catch (Exception e) {
         // System.out.println(e.getMessage());
      }
      return newAccount;
   }
   
   // Inserts new account data into the database, taking Strings from the GUI text fields/area.
   void insert(String accountName, String userName, String password, String URL, String notes) {
      String sql = "INSERT INTO accounts(accountName,userName,password,URL,notes) VALUES(?,?,?,?,?)";
      
      try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, accountName);
         pstmt.setString(2, userName);
         pstmt.setString(3, password);
         pstmt.setString(4, URL);
         pstmt.setString(5, notes);
         pstmt.executeUpdate();
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
   }
   
   // When the 'Save Changes' button is pressed, all fields from the currently selected account are updated in the database.
   void update(int id, String newAccountName, String newUserName, String newPassword, String newURL, String newNotes) {
      String sql = "UPDATE accounts SET "
                         + "accountName = ? , "
                         + "userName = ? , "
                         + "password = ? , "
                         + "URL = ? , "
                         + "notes = ? "
                         + "WHERE id = ?";
      
      try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
         // set the corresponding param
         pstmt.setString(1, newAccountName);
         pstmt.setString(2, newUserName);
         pstmt.setString(3, newPassword);
         pstmt.setString(4, newURL);
         pstmt.setString(5, newNotes);
         pstmt.setInt(6, id);
         // update
         pstmt.executeUpdate();
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
   }
   
   // When the 'Delete Account' button is pressed, the selected account is deleted from the database.
   void delete(int deleteid) {
      String sql = "DELETE FROM accounts WHERE id = ?";
      
      try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
         // set the corresponding param
         pstmt.setInt(1, deleteid);
         // execute the delete statement
         pstmt.executeUpdate();
         
      } catch (SQLException e) {
         // System.out.println(e.getMessage());
      }
   }
   
}
