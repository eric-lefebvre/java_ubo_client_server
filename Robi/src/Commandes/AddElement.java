package Commandes;

import Exception.CommandNotFoundException;
import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import Exception.UnknownColorException;
import exercice6.Command;
import exercice6.Environment;
import exercice6.Reference;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class AddElement implements Command {
	
	Environment environment;

	public AddElement(Environment environment) {
		this.environment = environment;
	}
	
	//AJoute le GElement specifie a l'environment 
	@Override
	public Reference run(Reference reference, SNode method) throws CommandNotFoundException, ReferenceNotFoundException, InvalidCommandArgumentsException, UnknownColorException {
		
		if (method.size() != 4) {
    		throw new InvalidCommandArgumentsException();
    	}
    	
        if (reference.getReceiver() instanceof GContainer) {
        	SNode subNode = (SNode)method.children().toArray()[3];
    		String newObjectName = method.get(0).contents() + "." + method.get(2).contents();
    		
    		String receiverName = subNode.get(0).contents();
    		Reference receiver = environment.getReferenceByName(receiverName);
    		
    		environment.addReference(newObjectName, receiver.run(subNode));
    		
        	GContainer target = (GContainer) reference.getReceiver();
            
            Reference objectToAdd = environment.getReferenceByName(newObjectName);
            GElement elementToAdd = (GElement) objectToAdd.getReceiver();
            target.addElement(elementToAdd);
            target.repaint();
        }

        return reference;
	}

}
