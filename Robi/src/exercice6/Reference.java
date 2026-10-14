package exercice6;

import java.util.HashMap;
import java.util.Map;

import Exception.CommandNotFoundException;
import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import Exception.UnknownColorException;
import stree.parser.SNode;

public class Reference {
	Object receiver;
	
	Map<String, Command> primitives;
	
	public Reference(Object receiver) {
		this.receiver = receiver;
		primitives = new HashMap<String, Command>();
	}

	public Reference run(SNode method) throws CommandNotFoundException, ReferenceNotFoundException, InvalidCommandArgumentsException, UnknownColorException {
		Command cmd = this.getCommandByName(method.get(1).contents());
		return (cmd.run(this, method));
	}
	
	public Command getCommandByName(String selector) throws CommandNotFoundException {
		Command c = primitives.get(selector);
		
		if (c == null) throw new CommandNotFoundException();
		
		return c;
	}
	
	public void addCommand(String selector, Command primitive) {
		primitives.put(selector, primitive);
	}

	public Object getReceiver() {
		
		return receiver;
	}
}
