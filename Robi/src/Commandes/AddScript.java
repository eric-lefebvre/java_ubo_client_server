package Commandes;

import Exception.CommandNotFoundException;
import exercice6.Command;
import exercice6.Environment;
import exercice6.Reference;
import exercice6.Script;
import stree.parser.SNode;

public class AddScript implements Command {

	Environment environment;

	public AddScript(Environment environment) {
		this.environment = environment;
	}

	public Reference run(Reference reference, SNode method) throws CommandNotFoundException {
		
		SNode scriptNode = (SNode)method.children().toArray()[3];
		
		
		reference.addCommand(method.get(2).contents(), new Script(scriptNode, reference, environment));
		
		return null;
	}	
}
