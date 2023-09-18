package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import core.Account;
import core.Expense;
import core.Income;
import core.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AppController {

    @FXML
    private TextField balanceField, transactionDescriptionField, transactionAmountField;
    @FXML
    private ListView<String> incomeView, expenseView, categoryView;
    @FXML
    private RadioButton incomeRadioButton, expenseRadioButton;
    @FXML
    private Button addTransactionButton;
    @FXML
    private ChoiceBox<String> categoryList;

    //private Account account = new Account(0.0);

    //Skal egentlig hente bruker fra innloggingsside, så følgende bruker fjernes når vi får det til.
    //private User user = new User("Markus", "passordet", "Markus Klund", "markus.klund@hotmail.com", account);
    private User user;
    private HashMap<String, Double> categoryTransactions = new HashMap<>();
    private ArrayList<String> categories = new ArrayList<>(Arrays.asList("Food", "Clothes", "Housing", "Other"));

    public void setUser(User user) {
        this.user = user;
        this.balanceField.setText(String.valueOf(getAccount().getBalance()));
        initialize();
    }

    @FXML
    private void initialize() {
        this.categoryList.getItems().add("Select category");
        this.categoryList.getItems().addAll(this.categories);
        this.categoryList.setValue("Select category");

        for (String category : this.categories) {
            this.categoryTransactions.put(category, 0.0);
        }
        updateCategoryView();

    }

    public User getUser() {
        return this.user;
    }

    public Account getAccount() {
        return this.user.getAccount();
    }

    @FXML
    private void updateCategoryView() {
        ArrayList<String> categories = new ArrayList<>(this.categoryTransactions.keySet());
        this.categoryView.getItems().clear();
        for (String category : categories) {
            this.categoryView.getItems().add(category + ": " + this.categoryTransactions.get(category));
        }
    }

    @FXML
    private void updateBalanceView() {
        balanceField.setText(String.valueOf(this.user.getAccount().getBalance()));
    }

    @FXML
    private void clearAmountAndDescriptionFields() {
        this.transactionAmountField.clear();
        this.transactionDescriptionField.clear();
    }

    @FXML
    void enableButton() {
        if (! this.categoryList.getValue().equals("Select category")) {
            this.addTransactionButton.setDisable(false);
        }
    }

    private boolean isNumeric(String string) {
        return string.matches("[0-9]+");
    }

    @FXML
    void handleAddTransactionButton() {
        String description = this.transactionDescriptionField.getText();
        String amountString = this.transactionAmountField.getText();
        if (! isNumeric(amountString)) {
            //throw new IllegalArgumentException("The given amount should only contain digits");
            //notify("The given amount should only contain digits");
            ExternalMethods.notify("The given amount should only contain digits");
        }

        else {
            double amount = Double.parseDouble(amountString);
            String category = this.categoryList.getValue();
            if (incomeRadioButton.isSelected()) {
                handleIncome(description, amount, category);
            }
            else if (expenseRadioButton.isSelected()) {
                handleExpense(description, amount, category);
            }
            updateBalanceView();
            updateCategoryView();
        }
        clearAmountAndDescriptionFields();
    }

    @FXML
    void handleIncome(String description, double amount, String category) {
        this.getAccount().addTransaction(new Income(description, amount));
        this.incomeView.getItems().add(0, "+ " + amount + "    " + description);
        this.categoryTransactions.put(category, amount);
    }

    @FXML
    void handleExpense(String description, double amount, String category) {
        this.getAccount().addTransaction(new Expense(description, amount));
        this.expenseView.getItems().add(0, "- " + amount + "    " + description);
        this.categoryTransactions.put(category, -amount);
    }

}
