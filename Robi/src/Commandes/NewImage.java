package Commandes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

import Exception.InvalidCommandArgumentsException;
import exercice6.Command;
import exercice6.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewImage implements Command {
	
	//Cree une nouvelle reference avec des commandes elementaires et le retourne
	public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
		try {
			if (method.size() != 3) {
	    		throw new InvalidCommandArgumentsException();
	    	}
			
			String imageName = method.get(2).contents();
			System.out.println(imageName);
			BufferedImage image = ImageIO.read(new File(imageName));
			
			//Instancie l'element e à partir du receiver de type exemple.class
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor(Image.class).newInstance((Image) image);
			//Referencie le nouvel objet et ajoute les fonctions de base
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | IOException e) {
			System.out.println("Problème lors de l'ajout de l'image.");
			e.printStackTrace();
		}
		return null;
	}

}
