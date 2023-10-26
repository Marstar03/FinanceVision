package ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import core.Account;
import core.Budget;
import core.Expense;
import core.Transaction;
import core.User;
import filesaving.FileHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BudgetTest extends ApplicationTest {

    private BudgetController controller;
    private Parent root;
    private User user;
    

    @Mock
    private FileHandler mockFileHandler;


    @Override
    public void start(Stage stage) throws IOException {
        Account account = new Account(3000);
        account.addTransaction(new Expense("mat", 2000.0, "Food"));
        Transaction tooOld = new Expense("old", 500.0, "Clothes", LocalDateTime.MIN);
        account.addTransaction(tooOld);
        user = new User("testuser", "password", "Test User", "test@user.com", account);
        Budget budget = new Budget();
        budget.addCategory("Food", 500);
        budget.addCategory("Clothes", 1000);
        user.setBudget(budget);

        mockFileHandler = Mockito.mock(FileHandler.class);
        when(mockFileHandler.deserializeUsers(any(File.class))).thenReturn(new ArrayList<>(List.of(user)));
        
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("budget.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setUser(user);
        controller.setModelAccess(new DirectFinanceVisionModelAccess(mockFileHandler));
        controller.init();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.setStage(stage);
        controller.setScene(scene);
        stage.show();

    }

    @Test
    public void testBackButton() {
        clickOn("#backButton");
        Node logOutButton = lookup("#logOutButton").query();
        Assertions.assertTrue(logOutButton.isVisible());
        
    }

    @Test
    public void testEditButton() {
        clickOn("#editBudgetButton");
        Node confirmButton = lookup("#confirmButton").query();
        Assertions.assertTrue(confirmButton.isVisible()); 

        clickOn("#backButton");
        Node editBudgetButton = lookup("#editBudgetButton").query();
        Assertions.assertTrue(editBudgetButton.isVisible());
    }

    @Test
    public void testEditCategories() {
        clickOn("#editBudgetButton");
        clickOn("#limit1");
        write("xy");
        clickOn("#confirmButton");
        Node confirmButton = lookup("#confirmButton").query();
        Assertions.assertTrue(confirmButton.isVisible());

        click("OK");
        clickOn("#limit1");
        type(KeyCode.BACK_SPACE, 10);
        write("1200.0");

        clickOn("#category0");
        type(KeyCode.BACK_SPACE, 10);
        clickOn("#confirmButton");
        confirmButton = lookup("#confirmButton").query();
        Assertions.assertTrue(confirmButton.isVisible());

        click("OK");
        clickOn("#category0");
        write("valid");
        clickOn("#confirmButton");
        Node editBudgetButton = lookup("#editBudgetButton").query();
        Assertions.assertTrue(editBudgetButton.isVisible()); 
    }

    @Test
    public void testRemoveAndAddCategories() {
        clickOn("#editBudgetButton");
        VBox limitBox = (VBox) lookup("#limitBox").query();
        click("+");
        Assertions.assertTrue(limitBox.getChildren().size() == 3);

        clickOn("#remove2");
        Assertions.assertTrue(limitBox.getChildren().size() == 2);

        clickOn("#confirmButton");
        Node editBudgetButton = lookup("#editBudgetButton").query();
        Assertions.assertTrue(editBudgetButton.isVisible());

    }


    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }

}
