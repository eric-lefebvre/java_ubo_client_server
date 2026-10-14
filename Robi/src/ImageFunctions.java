import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import graphicLayer.GSpace;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageFunctions {

	 // Méthode pour créer une BufferedImage à partir d'un JPanel
    public static BufferedImage panelToImage(GSpace result) {
        int width = result.getWidth();
        int height = result.getHeight();

        // Créer une BufferedImage avec le type d'image RGB
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Obtenir le Graphics de l'image pour dessiner le contenu du JPanel
        Graphics2D g2d = image.createGraphics();

        // Dessiner le contenu du JPanel sur l'image
        result.paint(g2d);

        // Libérer les ressources Graphics
        g2d.dispose();

        return image;
    }
    
    public static String bufferedImageToBase64(BufferedImage image, String formatName) throws IOException {
        // Créer un flux de sortie pour écrire l'image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Écrire l'image dans le flux de sortie avec le format spécifié
        ImageIO.write(image, formatName, outputStream);

        // Obtenir le tableau d'octets représentant l'image
        byte[] imageBytes = outputStream.toByteArray();

        // Encoder le tableau d'octets en Base64
        String base64String = Base64.getEncoder().encodeToString(imageBytes); 

        // Fermer le flux de sortie
        outputStream.close();

        return base64String;
    }
    


	public static BufferedImage base64ToBufferedImage(String base64String) throws IOException {
	    // Décoder la chaîne base64 en tableau d'octets
	    byte[] imageBytes = Base64.getDecoder().decode(base64String);
	
	    // Créer un flux d'entrée à partir du tableau d'octets
	    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
	
	    // Lire l'image à partir du flux d'entrée
	    BufferedImage image = ImageIO.read(inputStream);
	
	    // Fermer le flux d'entrée
	    inputStream.close();
	
	    return image;
	}
	
	public static Image bufferedImageToImage(BufferedImage bufferedImage) {
	    // Convertir BufferedImage en Image
	    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
	    return img;
	}
	
	 public static void displayImage(BufferedImage image, String title) {
		 // Créer une icône à partir de l'image
	        ImageIcon icon = new ImageIcon(image);

	        // Créer un label pour afficher l'icône
	        JLabel label = new JLabel();
	        label.setIcon(icon);

	        // Créer une fenêtre JFrame pour afficher l'image
	        JFrame frame = new JFrame(title);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().add(label, BorderLayout.CENTER); // Ajouter le label au centre du contenu
	        frame.pack();
	        frame.setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
	        frame.setVisible(true);
	    }
	    
}
