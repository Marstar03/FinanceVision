package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import filesaving.FileHandler;


public class RegisterUserTest extends ApplicationTest {

    private AbstractController abstractController;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        FileHandler mock = Mockito.mock(FileHandler.class);
        when(mock.deserializeUsers(any(File.class))).thenReturn(new ArrayList<>());


        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("registerNewUser.fxml"));
        root = fxmlLoader.load();
        abstractController = fxmlLoader.getController();
        abstractController.setStage(stage);
        abstractController.setFileHandler(Mockito.mock(FileHandler.class));
        abstractController.init();
        stage.setScene(new Scene(root));
        stage.show();

    }

       
    private void setUp() {
        clickOn("#fullNameField");
        write("Test User");
        clickOn("#usernameField");
        write("testuser");
        clickOn("#emailField");
        write("valid@gmail.com");
        clickOn("#passwordField");
        write("password");
        clickOn("#balanceField");
        write("1000");
    }



    


    @Test
    public void testRegisterUser() {
        setUp();
        click("Register user");
        Assertions.assertNotNull(lookup("#loginButton").query());
    }

    @Test
    public void testInvalidUsername() {
        setUp();
        clickOn("#usernameField");
        write(" invalid");
        click("Register user");
        Assertions.assertTrue(getRootNode().lookup("#loginButton") == null);
    }

    @Test
    public void testInvalidBalance() {
        setUp();
        clickOn("#balanceField");
        write("....");
        click("Register user");
        Assertions.assertTrue(getRootNode().lookup("#loginButton") == null);
    }

    @Test
    public void testBack() {
        clickOn("#backButton");
        Assertions.assertNotNull(lookup("#loginButton").query());
    }


    public Parent getRootNode() {
        return root;
    }

    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }


}



