
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IHMClient extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			final URL url = getClass().getResource("chat.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(url);
			final VBox root = (VBox) fxmlLoader.load();
			final Scene scene = new Scene(root, 851, 573);
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
		} catch (IOException ex) {
			System.err.println("Erreur au chargement: " + ex);
		}
		primaryStage.setTitle("Projet Robi");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}