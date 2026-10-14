package exercice6;

import java.util.HashMap;

import Exception.ReferenceNotFoundException;

public class Environment {
	
	HashMap<String, Reference> variables;
	
	public Environment() {
		variables = new HashMap<String, Reference>();
	}

	public Reference getReferenceByName(String receiverName) throws ReferenceNotFoundException {
		Reference r = variables.get(receiverName);
		
		if (r == null) throw new ReferenceNotFoundException();
		
		return r;
	}

	public void addReference(String string, Reference spaceRef) {
		
		variables.put(string, spaceRef);
		
	}
}
