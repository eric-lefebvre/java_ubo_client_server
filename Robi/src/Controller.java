import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import Exception.ConnexionException;
import Exception.EnvoyerScriptException;
import Exception.ExecuterScriptException;
import Exception.GetImageException;
import Exception.GetScriptException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Controller {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField entreeAdresse;

	@FXML
	private TextField entreeScript;

	@FXML
	private TextField entreePort;

	@FXML
	private Label labelEtatConnexion;

	@FXML
	private MenuButton MenuButton;

	@FXML
	private MenuItem option1;

	@FXML
	private MenuItem option2;

	@FXML
	private TextArea renvoiCode;

	private ClientRobi client;

	boolean connecte = false;

	TypeMessage mode = TypeMessage.NULL;

	@FXML
	ImageView imageView;

	@FXML
	void actionBoutonConnexion(ActionEvent event) {
		try {
			//si il n'y a pas de connexion alors connexion possible
			if (!connecte) {
				//si les champs ne sont pas vides
				if (!entreePort.getText().isEmpty() && !entreeAdresse.getText().isEmpty()) {

					System.out.println(entreePort.getText() + " " + entreeAdresse.getText());
					//nouveau client avec les infos des champs de connexion 
					client = new ClientRobi(entreeAdresse.getText(), entreePort.getText());

					labelEtatConnexion.setText("Connecté");
					labelEtatConnexion.setStyle("-fx-text-fill : green;");
					connecte = true;
				} else {
					afficherPopupErreur("Vous devez renseigner un port et une adresse IP.");
				}

			} else {
				//deja connecte
				afficherPopupInformation("Vous êtes deja connecté.");

			}

		} catch (ConnexionException e) {
			afficherPopupInformation("Le port renseigné et/ou l'adresse IP sont invalides");
			labelEtatConnexion.setText("Connexion echoué");
			labelEtatConnexion.setStyle("-fx-text-fill : red;");
		}
	}

	@FXML
	void actionBoutonDeconnexion(ActionEvent event) {
		//on ne peut pas se deconnecter si on est deja deconnecte
		if (!connecte) {
			afficherPopupInformation("Vous êtes deja deconnecté");
		} else {
			//appelle de la methode de deconnexion de client
			client.Deconnexion();
			labelEtatConnexion.setText("Non connecté");
			labelEtatConnexion.setStyle("-fx-text-fill : red;");
			this.entreeScript.clear();
			imageView.setImage(null); 
			this.mode = TypeMessage.NULL;
			this.renvoiCode.setText("");
			connecte = false;
		}

	}

	@FXML
	void actionBoutonEnvoyer(ActionEvent event) {
		//si le client est connecte
		if (connecte) {
			//si le champ du script n'est pas vide
			if (!entreeScript.getText().isEmpty()) {

				try {
					//envoi du script au serveur
					client.EnvoyerScript(entreeScript.getText());
				} catch (EnvoyerScriptException e) {
					afficherPopupErreur("Impossible d'envoyer le script au serveur.");
				}

				entreeScript.setText("");
			} else {
				afficherPopupInformation("Vous ne pouvez pas envoyer de script vide.");
			}
		} else {
			afficherPopupInformation("Vous ne pouvez pas envoyer de script car vous n'êtes pas connecté.");
		}

	}
	
	@FXML
	void actionBoutonEffacer(ActionEvent event) {
		try {
			//appelle de la methode qui clear l'affichage de l'image 
			client.clearSpace(TypeMessage.CLEAR);
			BufferedImage img = client.getImage();
			Image afficheImg = ImageFunctions.bufferedImageToImage(img);
			imageView.setImage(afficheImg);
			this.renvoiCode.setText("");
		} catch (EnvoyerScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			afficherPopupErreur("Impossible d'effacer le contenu du l'image.");
		} catch (GetImageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			afficherPopupErreur("Le résultat de l'execution n'est pas disponible.");
		}
	}

	@FXML
	void actionBoutonExecuter(ActionEvent event) {

		if (connecte) {
			if (mode != TypeMessage.NULL) {

				try {
					//si le mode d'execution est pas � pas
					if (mode == TypeMessage.PAS) {
						client.ExecuteScript(TypeMessage.PAS);
						BufferedImage img = client.getImage();
						if (img == null) {
							afficherPopupErreur("Vous ne pouvez pas exécuter sans script");
							return;
						}
						Image afficheImg = ImageFunctions.bufferedImageToImage(img);
						imageView.setImage(afficheImg);
						String script = client.getScript();
						renvoiCode.setText(script);
					} 
					//si le mode d'execution est bloc
					else if (mode == TypeMessage.BLOC) {
						client.ExecuteScript(TypeMessage.BLOC);
						BufferedImage img = client.getImage();
						if (img == null) {
							afficherPopupErreur("Vous ne pouvez pas exécuter sans script");
							return;
						}
						Image afficheImg = ImageFunctions.bufferedImageToImage(img);
						imageView.setImage(afficheImg);
					}
					
				} catch (ExecuterScriptException e) {
					afficherPopupErreur("Le script n'a pas pu être exécuté.");
				} catch (GetImageException e) {
					afficherPopupErreur("Le résultat de l'execution n'est pas disponible.");
				} catch (GetScriptException e) {
					afficherPopupErreur("Le script n'est pas disponible.");
				}
				
			} else {
				afficherPopupInformation("Vous devez sélectionner un mode d'execution.");
			}
		} else {
			afficherPopupInformation("Vous ne pouvez pas executer le script car vous n'etes pas connecté.");
		}

	}

	@FXML
	void initialize() {
		entreeAdresse.setText("localhost");
		entreePort.setText("8888");
		option1.setOnAction(event -> {
			mode = TypeMessage.PAS;
			MenuButton.setText("Pas à pas");
		});

		option2.setOnAction(event -> {
			mode = TypeMessage.BLOC;
			MenuButton.setText("Bloc");
		});
	}

	private void afficherPopup(String message, AlertType type) {

		Alert alert = new Alert(type);

		if (type == AlertType.ERROR) {
			alert.setTitle("Erreur");
		} else {
			alert.setTitle("Information");
		}

		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.setResizable(true);
		alert.showAndWait();

	}

	@FXML
	private void OpenFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrir un fichier");

		// Définir les filtres si vous souhaitez limiter les types de fichiers
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"),
				new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"));

		// Afficher la boîte de dialogue pour sélectionner un fichier
		File selectedFile = fileChooser.showOpenDialog(null);

		// Vérifier si un fichier a été sélectionné
		if (selectedFile != null) {
			try {
				// Lire le contenu du fichier sélectionné et l'afficher dans la Zone de script
				String content = new String(Files.readAllBytes(selectedFile.toPath()));
				entreeScript.setText(content);
			} catch (IOException e) {
				e.printStackTrace();
				// Gérer l'erreur de lecture du fichier
			}
		}
	}

	private void afficherPopupErreur(String message) {
		this.afficherPopup(message, AlertType.ERROR);
	}

	private void afficherPopupInformation(String message) {
		this.afficherPopup(message, AlertType.INFORMATION);
	}
}
