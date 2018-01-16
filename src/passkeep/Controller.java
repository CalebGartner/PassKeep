package sample;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;


public class Controller {
   
   // All GUI elements located in FXML document
   @FXML private TextField accountTitle;
   @FXML private TextField userName;
   @FXML private TextField password;
   @FXML private TextField url;
   @FXML private TextArea notes;
   
   @FXML private Button saveChanges;
   @FXML private Button deleteAccount;
   @FXML private Button addAccount;
   @FXML private Button addToDatabase;
   @FXML private Button addCancel;
   
   @FXML private GridPane gridPane;
   @FXML private VBox accountBox;
   
   // Same database instance as in the LoginController class.
   private static Database controlData = LoginController.controlData;
   // Button to add when the 'New Account' button is pressed.
   private Button newBtn;
   // Field to automatically keep track of the selected account's id in the database. Set in populateAccountButtons when the account is selected.
   private int selectedAccount = 1;
   
   // TODO implement search bar
   // TODO implement settings: change root password, ask for old and new
   // TODO implement password generation
   // TODO implement two-factor authentication
   // TODO implement SQLite DB encryption
   // TODO implement folders/indexed tables for organizing passwords
   // TODO implement option for different sorting of accounts
   // TODO implement export of DB to file (csv?)
   
   public Controller() {
      newBtn = new Button("New Account . . .");
      newBtn.setVisible(false);
      newBtn.setMaxHeight(Double.MAX_VALUE);
      newBtn.setMaxWidth(Double.MAX_VALUE);
      newBtn.setFont(Font.font("DejaVu Sans Light", FontWeight.EXTRA_BOLD, 15.0));
      newBtn.setEffect(new DropShadow());
   }
   
   private void populateAccountButtons(boolean addAccount) {
      // Get list of accounts from the database.
      List<PassKeepAccount> accountList = controlData.getList();
      
      // Sort buttons added to VBox from the database
      accountList.sort((a, b) -> (a.getAccountName().compareToIgnoreCase(b.getAccountName())));
      
      // UI control logic depending on whether there are any accounts in the database and if the user chose to add an account.
      if (accountList.size() == 0) {
         gridPane.setVisible(false);
         deleteAccount.setDisable(true);
         if (addAccount) {
            deleteAccount.setDisable(true);
            gridPane.setVisible(true);
         }
      }
      
      // Removes all current account buttons in the scrollpane to start with a fresh slate
      accountBox.getChildren().clear();
      
      // Create, set values, and add buttons to GUI
      for (PassKeepAccount populateFields : accountList) {
         
         Button accountBtn = new Button(populateFields.getAccountName());
         accountBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         accountBtn.setTextFill(Color.BLACK);
         accountBtn.setFont(Font.font("DejaVu Sans Light", FontWeight.EXTRA_BOLD, 15.0));
         accountBtn.setEffect(new DropShadow());
         
         accountBtn.setOnAction(event -> {
            
            accountTitle.setText(populateFields.getAccountName());
            userName.setText(populateFields.getUserName());
            password.setText(populateFields.getPassword());
            url.setText(populateFields.getURL());
            notes.setText(populateFields.getNotes());
   
            gridPane.setVisible(true);
            deleteAccount.setVisible(true);
            this.addAccount.setVisible(true);
            deleteAccount.setDisable(false);
            this.addAccount.setDisable(false);
            addToDatabase.setVisible(false);
            addCancel.setVisible(false);
            saveChanges.setVisible(true);
   
            accountBox.getChildren().remove(newBtn);
            
            // Change selectedAccount to match database index
            selectedAccount = populateFields.getID();
         });
         accountBox.getChildren().add(accountBtn);
      }
      
      // If the 'Add Account' button was pressed, show the 'New Account' button and add it to the VBox
      if (addAccount) {
         newBtn.setVisible(true);
         accountBox.getChildren().add(newBtn);
         newBtn.requestFocus();
         
         deleteAccount.setDisable(true);
      } else {
         accountBox.getChildren().remove(newBtn);
      }
   }
   
   
   @FXML private void handleSaveChangesBtn() {
      controlData.update(selectedAccount, accountTitle.getText(), userName.getText(), password.getText(), url.getText(), notes.getText());
      populateAccountButtons(false);
   }
   
   @FXML private void handleDeleteBtn(ActionEvent event) {
      boolean delete = displayDeleteDialog();
      if (!delete) {
         event.consume();
      } else {
         controlData.delete(selectedAccount);
         populateAccountButtons(false);
         
         // Automatically select previous account button
         int accounts = accountBox.getChildren().size();
         if (accounts > 0) {
            accountBox.getChildren().get(accounts - 1).fireEvent(new ActionEvent());
            accountBox.getChildren().get(accounts - 1).requestFocus();
   
         }
      }
   }
   
   @FXML private void handleAddBtn() {
      gridPane.setVisible(true);
      addToDatabase.setVisible(true);
      addCancel.setVisible(true);
      saveChanges.setVisible(false);
      deleteAccount.setDisable(true);
      addAccount.setDisable(true);
   
      accountTitle.setText("");
      userName.setText("");
      password.setText("");
      url.setText("");
      notes.setText("");
   
      populateAccountButtons(true);
   }
   
   @FXML private void handleAddToDatabaseBtn(ActionEvent event) {
      if (!(accountTitle.getText().equals("") || userName.getText().equals("") || password.getText().equals(""))) {
         
         controlData.insert(accountTitle.getText(), userName.getText(), password.getText(), url.getText(), notes.getText());
         addToDatabase.setVisible(false);
         addCancel.setVisible(false);
         deleteAccount.setDisable(false);
         addAccount.setDisable(false);
         
         populateAccountButtons(false);
      } else {
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setContentText("Uh-oh! You're missing necessary field(s) for this account.");
         alert.show();
         event.consume();
      }
   
   }
   
   @FXML private void handleAddCancelBtn() {
      addToDatabase.setVisible(false);
      addCancel.setVisible(false);
      deleteAccount.setDisable(false);
      addAccount.setDisable(false);
      
      accountBox.getChildren().remove(newBtn);
      
      // Automatically select previous account button
      int accounts = accountBox.getChildren().size();
   
      if (accounts == 0) {
         gridPane.setVisible(false);
         deleteAccount.setDisable(true);
      }
      if (accounts > 0) {
         accountBox.getChildren().get(accounts - 1).fireEvent(new ActionEvent());
         accountBox.getChildren().get(accounts - 1).requestFocus();
      }
   }
   
   private boolean displayDeleteDialog() {
      Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
      dialog.setTitle("Delete account information?");
      dialog.setContentText("If you are certain, press OK to delete account:");
      dialog.setHeaderText(null);
      dialog.setGraphic(null);
      dialog.initStyle(StageStyle.UNDECORATED);
   
      Optional<ButtonType> result = dialog.showAndWait();
      if (result.isPresent()) {
         if (result.get().equals(ButtonType.CANCEL)) { return false; }
         else if (result.get().equals(ButtonType.OK)) { return true; }
      }
      return false;
   }
   
   public void initialize() {
      // Display all accounts in scrollpane on entering scene.
      populateAccountButtons(false);
      // Ensure that the textArea 'notes' can be traversed via tab and shift-tab
      notes.addEventFilter(KeyEvent.KEY_PRESSED, (event -> {
         if (event.getCode() == KeyCode.TAB) {
            TextAreaSkin skin = (TextAreaSkin) notes.getSkin();
            if (skin.getBehavior() != null) {
               if (event.isControlDown()) {
                  skin.getBehavior().callAction("InsertTab");
               } else if (event.isShiftDown()) {
                  skin.getBehavior().callAction("TraversePrevious");
               } else {
                  skin.getBehavior().callAction("TraverseNext");
               }
               event.consume();
            }
         }
      }));
   }
}
