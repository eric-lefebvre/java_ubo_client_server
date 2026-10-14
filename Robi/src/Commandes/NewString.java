package Commandes;

import java.lang.reflect.InvocationTargetException;

import Exception.InvalidCommandArgumentsException;
import exercice6.Command;
import exercice6.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewString implements Command {

	//Cree une nouvelle reference avec des commandes elementaires et le retourne
	public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
		try {
			if (method.size() != 3) {
	    		throw new InvalidCommandArgumentsException();
	    	}
			
			String stringValue = method.get(2).contents();
			//Remove double quotes
			stringValue = stringValue.substring(1, stringValue.length() - 1);
			
			@SuppressWarnings("unchecked")
			//Instancie l'element e à partir du receiver de type exemple.class
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor(String.class).newInstance(stringValue);
			//Referencie le nouvel objet et ajoute les fonctions de base
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
