
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import JSON.JSON;
import exercice6.Exercice6;
import exercice6.Exercice6.Interpreter;
import graphicLayer.GSpace;
import stree.parser.SNode;



public class ServeurRobi {

	ServerSocket serverSocket;

	

	public ServeurRobi(int port){

		try {
			//lancement du server sur le port passe en parametre 
			serverSocket = new ServerSocket(port);

			while (true) {
				//on accepte un client
				Socket socket = serverSocket.accept();
	
				System.out.println("Client accepte.");
				//creation d'un thread avec le client qu'on vient d'accepte
				ClientServ clientThread = new ClientServ(socket);
				//lancement de ce thread
				clientThread.start();
			}

		} catch (IOException e) {

            System.err.println("Impossible d'accepter la connexion, fermeture du serveur, erreur: " + e.getMessage());

        } finally {

            try {
            	//si il y a un problème alors on ferme la socket du serveur
                serverSocket.close();

            } catch (IOException e) {

                System.err.println("Fermeture du socket serveur impossible: " + e.getMessage());

            }

        }

	}

	
	 public class ClientServ extends Thread {
		 BufferedWriter output;
		 BufferedReader input;

	        public ClientServ(Socket socket) {
	            try {
	            	output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	    			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            } catch (IOException e ) {
	                System.err.println("Probleme de communication avec le client, fermeture de la connexion, erreur: " + e.getMessage());
	            }
	        }
	        //le node.contents() ne fonctionne pas alors on a fait cette methode
	        public String getScriptFromNode(SNode node) {
	        	String res = "(";
	        	
	        	for (SNode fils : node.children()) {
	        		if (fils.contents() == null) {
	        			res += getScriptFromNode(fils);
	        		} else {
	        			res += " " + fils.contents();
	        		}
	        	}
	        	res += " )";
	        	return res;
	        }

	        public void run() {
	            try {

	            	 String message;
	            	 //initialisation de l'exercie 6
	            	 Exercice6 exo6 = new Exercice6();
	            	 DataToSend executable = null;
	            	 List<SNode> compiledScript = new ArrayList<>();
	            	 
	            	 //tant qu'il y a des choses a lire venant du client
	                 while ((message = input.readLine()) != null) {
	                		 
	                	//transformation du script en JSON
						executable = (DataToSend) JSON.Json2Java(message, DataToSend.class);
	                	System.out.println("Message recu : " + executable.toString());
	                	
	                	//si le type de message recu est Clear : effacer le contenu du GSpace
	                	if(executable.getType() == TypeMessage.CLEAR) {
	                		System.out.println("clear space");
	                		exo6.getSpace().clear();
	                		GSpace result = exo6.getSpace();
	                		BufferedImage imageResult = ImageFunctions.panelToImage(result);
                			// Convertir BufferedImage en tableau d'octets
                			String imageString = ImageFunctions.bufferedImageToBase64(imageResult, "png");
                			System.out.println(imageString);
                            output.write(imageString);
                			output.newLine();
                			output.flush();
	                	}
	                	
	                	//si le message recu est SEND ca veut dire que c'est le boutton Envoyer 
	                	if(executable.getType() == TypeMessage.SEND) {
	                		System.out.println("send");
	                		//on stock le script recu pour l'executer plus tard
	                		String script = executable.getData();
	                		//on compile ce script
	                		compiledScript = exo6.parseScript(script);
	                	}
	                	
	                	//si le message recu contient un mode d'execution (pas ou bloc) alors c'est qu'il faut executer le script
	                	else if(executable.getType() == TypeMessage.PAS) {
	                		System.out.println("pas");
	                		//si le script compile n'est pas vide
	                		//verification de si le script a ete envoye 
	                		if(!compiledScript.isEmpty()) {
	                			
	                			// execution des s-expressions compilees
	                			Iterator<SNode> itor = compiledScript.iterator();
	                			if (itor.hasNext()) {
	                				
	                				SNode node = itor.next();
	                				exo6.executePas(node);
	                				GSpace result = exo6.getSpace();
	                				
	                				//envoie de l'image
	                    			BufferedImage imageResult = ImageFunctions.panelToImage(result);
	                    			// Convertir BufferedImage en tableau d'octets
	                    			String imageString = ImageFunctions.bufferedImageToBase64(imageResult, "png");
	                    			System.out.println(imageString);
	                                output.write(imageString);
	                    			output.newLine();
	                    			//envoi du script execute
	                    			output.flush();
	                    			//utilisation de cette methode qui remplace le .contents() qui ne fonctionne pas
	                    			String scriptExecuted = getScriptFromNode(node);
	                    			output.write(scriptExecuted);
	                    			output.newLine();
	                    			output.flush();
	                    			//on enleve le script qu'on vient d'execute
	                				compiledScript.remove(node);
	                			}
	                		} else {
	                			System.out.println("Envoi fini");
	                			output.write("fini");
	                			output.newLine();
                    			output.flush();
	                		}
	                	}
	                	else if(executable.getType() == TypeMessage.BLOC) {
	                		System.out.println("bloc");
	                		
	                		//si le script compile n'est pas vide
	                		//verification de si le script a ete envoye 
	                		if(!compiledScript.isEmpty()) {
	                			//execution en bloc du script compile 
	                			exo6.executeBloc(compiledScript);
	                			GSpace result = exo6.getSpace();
	                			BufferedImage imageResult = ImageFunctions.panelToImage(result);
	                			//ImageFunctions.displayImage(imageResult, "test");
	                			// Convertir BufferedImage en tableau d'octets
	                			String imageString = ImageFunctions.bufferedImageToBase64(imageResult, "png");
	                            output.write(imageString);
	                			output.newLine();
	                			output.flush();
	                			//mise a zero de tout le script compile 
	                			compiledScript.clear();
	                		} else {
	                			System.out.println("Envoi fini");
	                			output.write("fini");
	                			output.newLine();
                    			output.flush();
	                		}
	                	}
	              
	                 }  
	                 
	            } catch (IOException e ) {
	                System.err.println("Probleme de lecture du message du client: " + e.getMessage());
	            } finally {
	                try {
	                	
	                    if (input != null) input.close();
	                    if (output != null) output.close();
	                    
	                } catch (IOException e) {
	                    System.err.println("Fermeture de la socket client impossible: " + e.getMessage());
	                }
	            }
	        }
	    }

	

	public static void main(String args[]) {

		new ServeurRobi(Integer.parseInt(args[0]));

	}

}



