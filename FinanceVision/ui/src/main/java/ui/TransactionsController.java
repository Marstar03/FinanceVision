package ui;

import core.Account;
import core.Income;
import core.Transaction;
import core.User;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

/**
 * Controller for the main page of the application.
 */
public class TransactionsController extends AbstractSubController {
    
    @FXML
    private ListView<Transaction> incomeView;
    @FXML
    private ListView<Transaction> expenseView;
    @FXML
    private Button addTransactionButton;
    @FXML
    private Button editTransactionButton;
    @FXML
    private Button deleteTransactionButton;
    @FXML
    private ChoiceBox<String> transactionFilterList;



    /**
     * Shows total bank balance and list of transactions.
     */
    @Override
    public void init() {
        final List<String> timeList = new ArrayList<>(List.of(
            "All", "Today", "This week", "This month", "This year"));
        // updateBalanceView();
        incomeView.getItems().clear();
        expenseView.getItems().clear();
        loadTransactionsFromFile();

        transactionFilterList.getItems().addAll(timeList);
        transactionFilterList.getSelectionModel().selectFirst();
        transactionFilterList.setOnAction(this::handleFilterTransactionsList);

       
    }
    
    private void loadTransactionsFromFile() {
        List<Transaction> fileTransactions = getAccount().getTransactions();
        fileTransactions.stream().sorted((t1, t2) -> t1.getTime().compareTo(t2.getTime()))
        .forEach(t -> addTransactionToView(t));
    }
    
    
    public User getUser() {
        return parentController.getUser();
    }
    
    public Account getAccount() {
        return parentController.getUser().getAccount();
    }

    @FXML
    private void addTransactionToView(Transaction transaction) {
        if (transaction instanceof Income) {
            this.incomeView.getItems().add(0, transaction);
        } else {
            this.expenseView.getItems().add(0, transaction);
        }
    }

    @FXML
    private void handleAddTransactionButton() throws IOException {
        parentController.switchBorderPane("addTransaction.fxml");
    }

    @FXML
    private void incomeViewClicked() {
        expenseView.getSelectionModel().clearSelection();
    }

    @FXML
    private void expenseViewClicked() {
        incomeView.getSelectionModel().clearSelection();
    }

    private Transaction getSelectedTransaction() {
        if (!incomeView.getSelectionModel().isEmpty()) {
            return incomeView.getSelectionModel().getSelectedItem();
        } else if (!expenseView.getSelectionModel().isEmpty()) {
            return expenseView.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }
    } 

    @FXML
    private void handleEditTransaction() throws IOException {
        Transaction selectedTransaction = getSelectedTransaction();
        if (selectedTransaction == null) {
            parentController.notify("No transaction selected", AlertType.ERROR);
            return;
        }
        parentController.setTransaction(selectedTransaction);
        parentController.switchBorderPane("editTransaction.fxml");
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = getSelectedTransaction();
        if (selectedTransaction == null) {
            parentController.notify("No transaction selected", AlertType.ERROR);
            return;
        }
        getAccount().removeTransaction(selectedTransaction);
        parentController.saveToFile();
        init();
    }

    /**
     * Method for filtering the transactions by current day, month or year.
     */
    @FXML
    private void handleFilterTransactionsList(ActionEvent event) {
        List<Transaction> allTransactions = getAccount().getTransactions();
        String selectedTime = transactionFilterList.getValue();
        incomeView.getItems().clear();
        expenseView.getItems().clear();
        Stream<Transaction> timeFilterStream = allTransactions.stream();

        if (selectedTime.equals("Today")) {
            timeFilterStream = timeFilterStream
                .filter(t -> t.getTime().toLocalDate().equals(LocalDate.now()));
        } else if (selectedTime.equals("This week")) {
            timeFilterStream = timeFilterStream
                .filter(new Predicate<Transaction>() {
                    @Override
                    public boolean test(Transaction t) {
                        LocalDate transactionDate = t.getTime().toLocalDate();
                        LocalDate nowDate = LocalDate.now();
                        LocalDate transactionDateStartOfWeek = transactionDate
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                        LocalDate nowDateStartOfWeek = nowDate
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                        return transactionDateStartOfWeek.equals(nowDateStartOfWeek);
                    }
                }
                );
        } else if (selectedTime.equals("This month")) {
            timeFilterStream = timeFilterStream
                .filter(t -> t.getTime().getMonth().equals(LocalDate.now().getMonth()) 
                && t.getTime().getYear() == LocalDate.now().getYear());
        } else {
            timeFilterStream = timeFilterStream
                .filter(t -> t.getTime().getYear() == LocalDate.now().getYear());
        }
        timeFilterStream
            .sorted((t1, t2) -> t1.getTime().compareTo(t2.getTime()))
            .forEach(t -> addTransactionToView(t));
    }
}