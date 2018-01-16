package sample;

class PassKeepAccount {
   
   private int id;
   private String accountName;
   private String userName;
   private String password = "defaultMasterPasswordNecessary";
   private String URL;
   private String notes;
   
   void setID(int id) {
     this.id = id;
   }
   
   void setAccountName(String newAccount) {
        accountName = newAccount;
     }
     
   void setUserName(String newUser) {
        userName = newUser;
     }
     
   void setPassword(String newPassword) {
        password = newPassword;
     }
     
   void setURL(String URL) {
      this.URL = URL;
   }
   
   void setNotes(String newNotes) {
        notes = newNotes;
     }
     
   int getID() {
     return id;
   }
   
   String getAccountName() {
      return accountName;
   }
   
   String getUserName() {
      return userName;
   }
   
   String getNotes() {
      return notes;
   }
   
   String getPassword() {
      return password;
   }
   
   String getURL() {
      return URL;
   }

}
