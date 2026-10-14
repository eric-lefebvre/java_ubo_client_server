package exercice6;

import Exception.InvalidCommandArgumentsException;
import stree.parser.SNode;

public class Sleep implements Command {

	public Reference run(Reference reference, SNode method) throws InvalidCommandArgumentsException {
		
		if (method.size() != 3) {
    		throw new InvalidCommandArgumentsException();
    	}
		
		int sleepTime = Integer.parseInt(method.get(2).contents());
		
		try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		return null;
	}
}
