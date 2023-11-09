package ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import filesaving.JsonFileSaving;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadLoginScreenTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loadLoginScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        AbstractController controller = loader.getController();
        controller.setModelAccess(new DirectFinanceVisionModelAccess(new JsonFileSaving()));
        controller.setStage(stage);
        controller.setScene(scene);
        controller.init();
    }

    @BeforeAll
    public static void setupHeadless() {
        App.supportHeadless();
    }

    @Test
    public void testLoadLoginScreen() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Node loginButton = lookup("#loginButton").query();
        assertTrue(loginButton.isVisible(), "Login side ikke synlig etter animasjon");
    }
}
