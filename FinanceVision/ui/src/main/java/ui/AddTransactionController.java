package ui;

import java.io.IOException;
import java.time.LocalDateTime;

import core.Expense;
import core.Income;
import core.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AddTransactionController extends AbstractController {
    
    @FXML
    private TextField descriptionField, amountField;
    @FXML
    private RadioButton incomeRadioButton, expenseRadioButton;
    @FXML
    private Button addTransactionButton, backButton;
    @FXML
    private ChoiceBox<String> categoryList;
    @FXML
    private DatePicker datePicker;

    
    @FXML
    void handleRbtnClicked(){

        if (incomeRadioButton.isSelected()){
            categoryList.getItems().clear();
            categoryList.getItems().addAll(core.User.defaultIncomeCategories);
        }
        else if(expenseRadioButton.isSelected()){
            categoryList.getItems().clear();
            categoryList.getItems().addAll(core.User.defaultExpenseCategories);

            //add the additional categories for this user
        }

    }

    @FXML
    void handleAddTransactionButton() throws IOException {
        try{
            String description = descriptionField.getText();
            String amountString = amountField.getText();
            double amount = Double.parseDouble(amountString);
            String category = this.categoryList.getValue();
            LocalDateTime time = datePicker.getValue().atStartOfDay();

            Transaction t;
            if (incomeRadioButton.isSelected()) {
                t = new Income(description, amount, category, time);
            }
            else {
                t = new Expense(description, amount, category, time);
            }
            user.getAccount().addTransaction(t);
            saveToFile();

        }
        catch(Exception e){
            //TODO: tell the user which field is invalid or empty
            notify("One or more fields are empty or contains invalid data", AlertType.WARNING);
            return;
        }

        switchScene("App.fxml", user);
    }

    @FXML
    void handleBack() throws IOException{
        switchScene("App.fxml", user);
    }
}
