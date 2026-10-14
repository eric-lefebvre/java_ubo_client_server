package Commandes;

import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import exercice6.Command;
import exercice6.Environment;
import exercice6.Reference;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class DelElement implements Command {

	Environment environment;

	public DelElement(Environment environment) {
		this.environment = environment;
	}
	
	//Supprime le GElement specifie de l'environment 
	@Override
	public Reference run(Reference reference, SNode method) throws ReferenceNotFoundException, InvalidCommandArgumentsException {
		
		if (method.size() != 3) {
    		throw new InvalidCommandArgumentsException();
    	}
    	
        if (reference.getReceiver() instanceof GSpace) {
        	GSpace target = (GSpace) reference.getReceiver();
            
            Reference objectToRemove = environment.getReferenceByName(method.get(2).contents());
            GElement elementToRemove = (GElement) objectToRemove.getReceiver();
            target.removeElement(elementToRemove);
            target.repaint();
        }

        return null;
	}

}
