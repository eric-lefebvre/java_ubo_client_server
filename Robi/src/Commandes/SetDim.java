package Commandes;

import java.awt.Dimension;

import Exception.InvalidCommandArgumentsException;
import exercice6.Command;
import exercice6.Reference;
import graphicLayer.GRect;
import stree.parser.SNode;

public class SetDim implements Command {
    
    public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
    	
    	if (method.size() != 4) {
    		throw new InvalidCommandArgumentsException();
    	}
    	
    	int dx = Integer.parseInt(method.get(2).contents());;
        int dy = Integer.parseInt(method.get(3).contents());;
        
        if (reference.getReceiver() instanceof GRect) {
			GRect target = (GRect) reference.getReceiver();
			target.setDimension(new Dimension(dx, dy));
		}
        
        return null;
    }
}
