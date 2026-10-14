package Commandes;

import java.awt.Point;

import Exception.InvalidCommandArgumentsException;
import exercice6.Command;
import exercice6.Reference;
import graphicLayer.GElement;
import stree.parser.SNode;

public class Translate implements Command {
    
    public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
    	
    	if (method.size() != 4) {
    		throw new InvalidCommandArgumentsException();
    	}
    	
    	int dx = Integer.parseInt(method.get(2).contents());;
        int dy = Integer.parseInt(method.get(3).contents());;
        
        if (reference.getReceiver() instanceof GElement) {
        	GElement target = (GElement) reference.getReceiver();
			target.translate(new Point(dx, dy));
		}
        
        return null;
    }
}
