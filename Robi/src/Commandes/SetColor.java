package Commandes;

import java.awt.Color;

import Exception.InvalidCommandArgumentsException;
import Exception.UnknownColorException;
import exercice6.Command;
import exercice6.Reference;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetColor implements Command {

	public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException, UnknownColorException {
		
		if (method.size() != 3) {
    		throw new InvalidCommandArgumentsException();
    	}
		
		Color c = this.getColorFromName(method.get(2).contents());
		
		if (c == null) {
			throw new UnknownColorException();
		}
		
		if (reference.getReceiver() instanceof GElement) {
			GElement target = (GElement) reference.getReceiver();
			target.setColor(c);
		}
		if (reference.getReceiver() instanceof GSpace) {
			GSpace target = (GSpace) reference.getReceiver();
			target.setColor(c);
		}

		return null;
	}
	
	private Color getColorFromName(String colorName) {
        switch (colorName) {
            case "black":
                return Color.BLACK;
            case "yellow":
                return Color.YELLOW;
            case "white":
                return Color.WHITE;
            case "red":
                return Color.RED;
            default:
                return null;
        }
    }

}
