package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
   
   @FXML private Text warning;
   @FXML private PasswordField masterPass;
   
   static Database controlData;
   private int selectedAccount = 1;
   
   public LoginController() {
      controlData = new Database();
      if (controlData.get(selectedAccount).getPassword().contentEquals("defaultMasterPasswordNecessary")) {enterMasterPassword();}
   }
   
   
   @FXML private void handleMasterPassword() throws IOException {
      if (masterPass.getText().equals(controlData.get(selectedAccount).getPassword())) {
         warning.setVisible(false);
         // Code to switch scene root to access database.
         switchScenes();
         // displayAccounts
      } else {
         warning.setVisible(true);
      }
   }
   
   private void switchScenes() throws IOException {
      Stage stage = (Stage) masterPass.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("PassKeepScene.fxml"));
      Scene passScene = new Scene(root);
      stage.setScene(passScene);
   }
   
   private void enterMasterPassword() throws NullPointerException {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Set Master Password");
      dialog.setContentText("Please enter your master password: ");
      dialog.setHeaderText("Choose a strong password to safeguard access to all your acccounts . . .");
      dialog.setGraphic(null);
      dialog.getDialogPane().getButtonTypes().removeAll(ButtonType.CANCEL);
      dialog.initStyle(StageStyle.UNDECORATED);
      
      dialog.setOnCloseRequest((event) -> {
         if (dialog.getResult().isEmpty()) {
            event.consume();
         }
      });
      
      dialog.showAndWait()
            .ifPresent(response -> {
               controlData.insert("rootUserAccount", "rootUser",
                     dialog.getResult(), "rootURL", "rootnotes");
            });
      
      controlData.insert("Example Account", "exampleUser",
            "hunter2", "exampleURL", "Notes to remember about this account.");
   }
   
}