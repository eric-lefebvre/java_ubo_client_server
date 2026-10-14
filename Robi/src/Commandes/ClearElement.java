package Commandes;

import Exception.CommandNotFoundException;
import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import exercice6.Command;
import exercice6.Environment;
import exercice6.Reference;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import stree.parser.SNode;

public class ClearElement implements Command {

	Environment environment;

	
	public ClearElement(Environment environment) {
		this.environment = environment;
	}

	@Override
	public Reference run(Reference receiver, SNode method) throws CommandNotFoundException, ReferenceNotFoundException,
			InvalidCommandArgumentsException {
		
		if (method.size() != 2) {
			throw new InvalidCommandArgumentsException();
    	}
		

		if (receiver.getReceiver() instanceof GContainer) {
			GContainer target = (GContainer) receiver.getReceiver();
			GElement[] contentArray = target.getRawContents();
			
			for(int i =0; i<contentArray.length; i++) {
				target.removeElement(contentArray[i]);
			}
			
			target.repaint();
        }
		
		return receiver;
	}

}
