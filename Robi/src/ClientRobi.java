import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

import Exception.ConnexionException;
import Exception.EnvoyerScriptException;
import Exception.ExecuterScriptException;
import Exception.GetImageException;
import Exception.GetScriptException;
import JSON.*;

public class ClientRobi {
	
	boolean running = false;
	BufferedWriter output;
	BufferedReader input;
	Socket socket;
	int bytesRead;

	public ClientRobi (String serverAdress, String port) throws ConnexionException {
		try {
			InetAddress adr = InetAddress.getByName(serverAdress);
			
			//socket avec les infos du serveur
			socket = new Socket(adr, Integer.parseInt(port));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			System.err.println("Impossible de se connecter au serveur, erreur: " + e.getMessage());
            throw new ConnexionException("Erreur connexion");
		}

	}
	
	//methode qui permet d'envoyer le script au format JSON au serveur
	public void EnvoyerScript(String script) throws EnvoyerScriptException {
		try {
			
			DataToSend toSend = new DataToSend(TypeMessage.SEND, script);
			//passage de script au format JSON
			String ToSendJson = JSON.Java2Json(toSend);
			//envoi du script JSON
			output.write(ToSendJson);
			System.out.println(ToSendJson);
			output.newLine();
			output.flush();
			 
		} catch(IOException e ) {
			
			System.err.println("Impossible d'envoyer le script au serveur: " + e.getMessage());
            throw new EnvoyerScriptException("Erreur envoi script");
			
		}
	}
	//methode qui execute avec un mode le script pr�c�dement envoye au serveur
	public void ExecuteScript(TypeMessage mode) throws ExecuterScriptException {
		try {
			//on envoie au serveur une donn�e contenant uniquement le mode d'execution choisi
			DataToSend toSend = new DataToSend(mode, "");
			//au format JSON
			String ToSendJson = JSON.Java2Json(toSend);
			output.write(ToSendJson);
			output.newLine();
			output.flush();
			 
		} catch(IOException e ) {
			
			System.err.println("Impossible d'ex�cuter le script : " + e.getMessage());
            throw new ExecuterScriptException("Erreur exec script");
			
		}
	}
	
	//methode de recuperation de l'image 
	public BufferedImage getImage() throws GetImageException {
		BufferedImage image = null;
		try {
			System.out.println("avant if");
			String imageResult;
			//si on recoit un message alors on entre dans la boucle
			while((imageResult =input.readLine())!=null) {
				//si le message recu est fini alors on quitte
				if (imageResult.equals("fini")) {
					return null;
				}
				System.out.println("avant read");
				System.out.println("aprèsss read");
				System.out.println(imageResult);
				//transformation du string en image 
				image = ImageFunctions.base64ToBufferedImage(imageResult);
				break;
			}
		
		} catch (IOException e) {
			System.err.println("Impossible de lire l'image du serveur : " + e.getMessage());
            throw new GetImageException("Erreur récupération image");
		}
		return image;
	}
	//methode de recuperation du script pour l'afficher sur la console IHM
	public String getScript() throws GetScriptException {
		String script = null;
		try {
			System.out.println("avant if");
			//si on recoit un message alors entre dans la boucle 
			while((script =input.readLine())!= null) {
				
				if (script.equals("fini")) {
					return null;
				}
				System.out.println("avant read");
				System.out.println(script);
				break;
			}
		
		} catch (IOException e) {
			System.err.println("Impossible de lire l'image du serveur : " + e.getMessage());
            throw new GetScriptException("Erreur récupération script");
		}
		return script;
	}
	
	//methode qui supprime l'affichage de l'image dans l'IHM 
	public void clearSpace(TypeMessage mode) throws EnvoyerScriptException {
		try {
			//envoi d'un message contenant la demande de clear l'affichage 
			DataToSend toSend = new DataToSend(TypeMessage.CLEAR);
			String ToSendJson = JSON.Java2Json(toSend);
			output.write(ToSendJson);
			System.out.println(ToSendJson);
			output.newLine();
			output.flush();
			 
		} catch(IOException e ) {
			System.err.println("Impossible d'effacer l'espace : " + e.getMessage());
			throw new EnvoyerScriptException("Erreur ré-initiallisatio  de l'espace");
		}
	}
	//methode de deconnexion 
	public void Deconnexion() {
    	try {
    		running = false;
            // Fermer les flux de sortie et d'entree
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            
            // Fermer la socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            
            System.out.println("Deconnexion reussie.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la dconnexion : " + e.getMessage());
        }
    }
	
	
	
	public static void main(String args[]) throws ConnexionException {
		ClientRobi robi = new ClientRobi(args[1], args[0]);
	}

}

