package Commandes;

import java.lang.reflect.InvocationTargetException;

import Exception.InvalidCommandArgumentsException;
import exercice6.Command;
import exercice6.Environment;
import exercice6.Reference;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewElement implements Command {

	Environment environment;

	public NewElement(Environment environment) {
		this.environment = environment;
	}

	//Cree une nouvelle reference avec des commandes elementaires et le retourne
	public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
		try {
			if (method.size() != 2) {
	    		throw new InvalidCommandArgumentsException();
	    	}
			
			@SuppressWarnings("unchecked")
			//Instancie l'element e à partir du receiver de type exemple.class
			GElement e = ((Class<GElement>) reference.getReceiver()).getDeclaredConstructor().newInstance();
			//Referencie le nouvel objet et ajoute les fonctions de base
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new SetColor());
			ref.addCommand("translate", new Translate());
			ref.addCommand("setDim", new SetDim());
			ref.addCommand("del", new DelElement(environment));
			ref.addCommand("addScript", new AddScript(environment));
			if (ref.getReceiver() instanceof GContainer) {
				ref.addCommand("add", new AddElement(environment));
			}
			return ref;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}	
}
