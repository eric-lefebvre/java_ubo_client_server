package exercice6;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Exception.CommandNotFoundException;
import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import Exception.UnknownColorException;
import stree.parser.SDefaultNode;
import stree.parser.SNode;
import stree.parser.STreeBuilder;

public class Script implements Command {
	Environment environment;

	SNode scriptMethod;
	
	//reference du script 
	Reference self;
	
	int nbargs;
	
	//conserver les paramètre du script
	SNode[] paramScript;
	
	Map<Command, SNode> commands;
	
	Map<SNode, SNode> replaceNode;
	
	public Script(SNode method, Reference self,Environment environment) throws CommandNotFoundException {
		this.environment = environment;
		this.scriptMethod = method;
		this.self = self;		
		
		this.nbargs = method.children().get(0).children().size() - 1;
		this.paramScript = new SNode[this.nbargs];
		
		this.commands = new LinkedHashMap<>();
		this.replaceNode = new LinkedHashMap<>();
		
		int i = 0;

        for(SNode node : method.children()) {
        	
        	//Remplissage de paramScript avec les parametres du script (1e noeud)
        	if (i == 0) {
        		i++;
        		List<SNode> makeParam = node.children();
        		this.paramScript = makeParam.toArray(new SNode[makeParam.size()]);
        	}
        	//Remplissage de la liste des commandes executees par le script
        	else {
      
	            Command c = self.getCommandByName(node.get(1).contents());
	            
	            this.commands.put(c, node);
        	}
        }

		
	}
	
	@Override
	public Reference run(Reference reference, SNode method) throws CommandNotFoundException, ReferenceNotFoundException, InvalidCommandArgumentsException, UnknownColorException {
    	
		if (method.size() != nbargs + 2) {
    		throw new InvalidCommandArgumentsException();
    	}
		
		this.replaceNode.clear();
		
		System.out.println("ReplaceNodeSize " + replaceNode.size());
		
		getReplacementMap(this.paramScript, method);

		for (Command key : commands.keySet()) {
			
			SNode modifiedNode = replaceMatchingChildrenInNode(commands.get(key));
			
			for(SNode n : modifiedNode.children()) {
				System.out.println(n.contents());
			}
			
			key.run(this.environment.getReferenceByName(modifiedNode.get(0).contents()), modifiedNode);
		}

        return reference;
	}
	
	//lier les paramètres du script avec les valeurs de remplacement 
	private void getReplacementMap(SNode[] nodeToMatchArray, SNode nodeReplacing) {
		
		List<SNode> nodeReplacingContents = nodeReplacing.children();
		SNode[] nodeReplacingArray = nodeReplacingContents.toArray(new SNode[nodeReplacingContents.size()]);
		
		for (int i = 0; i < nodeToMatchArray.length; i++) {

			if (i == 0)
				this.replaceNode.put(nodeToMatchArray[i], nodeReplacingArray[i]);
			if (i > 0)
				this.replaceNode.put(nodeToMatchArray[i], nodeReplacingArray[i + 1]);
		}
		
	}
	
	/* nodeToReplace -> les commandes du script
	 * nodeToMatchArray -> le tout/le script (toutes les commandes du script)
	 * nodeReplacing -> une exécution d'un script ( space addRect mySquare 30 yellow )
	 */
	private SNode replaceMatchingChildrenInNode(SNode nodeToReplace) {
		
		List<SNode> nodeToReplaceContents = nodeToReplace.children();
		
		SNode[] nodeToReplaceArray = new SDefaultNode[nodeToReplaceContents.size()];
		for (int i = 0; i < nodeToReplaceArray.length; i++) {
		    nodeToReplaceArray[i] = new SDefaultNode();
		}
		int n = 0;
		for (SNode child : nodeToReplaceContents) {
			if (child.contents() != null)
				nodeToReplaceArray[n].setContents(new String(child.contents()));
			else {
				nodeToReplaceArray[n] = child;
			}
			n++;
		}	
		
		//constrcution du nouveau node modifié avec les valeurs des variables 
				
		for (Map.Entry<SNode, SNode> entry : this.replaceNode.entrySet()) {
			
			for(int i = 0; i < nodeToReplaceArray.length; i++) {
				
				if (nodeToReplaceArray[i].contents() == null) {
					nodeToReplaceArray[i] = replaceMatchingChildrenInNode(nodeToReplaceArray[i]);
					continue;
				}
				
		        String key = entry.getKey().contents();
		        String value = entry.getValue().contents();
		        
		        //modifer la valeur du contenu et re-set le contenu 
		        String[] contentToReplaceSplit = nodeToReplaceArray[i].contents().split("\\.");
				
				for (String s : contentToReplaceSplit) {
					if (s.equals(key))
						nodeToReplaceArray[i].setContents(nodeToReplaceArray[i].contents().replace(s, value));
				}
	        
			}
		}
		
		SNode executableNode = constructNodeFromArray(nodeToReplaceArray);
		
		return executableNode;
	}
	
	
	//methode pour reconstruire un SNode à partir de l'array crée
	private SNode constructNodeFromArray(SNode[] nodeArray) {

		//Création d'un nouvel instance de STreeBuilder
		STreeBuilder<SNode> treeBuilder = new STreeBuilder<>();

		//Début de la construction du nouveau SNode
		treeBuilder.startNode();

		//Ajout des nœuds du tableau commandMethodArray comme enfants du nouveau SNode
		for (SNode node : nodeArray) {
		 // Ajout du contenu du nœud
		 if (node.contents() != null) {
		     treeBuilder.atom(node.contents());
		 }
		 // Si le nœud a des enfants, créer un nouveau nœud et ajoutez les enfants récursivement
		 if (!node.children().isEmpty()) {
		     treeBuilder.startNode();
		     // Appel récursif pour ajouter les enfants du nœud
		     addChildNodes(treeBuilder, node.children());
		     treeBuilder.endNode();
		 }
		}

		//Fin de la construction du nouveau SNode
		treeBuilder.endNode();

		//Récupération du SNode reconstruit
		SNode reconstructedNode = treeBuilder.result().get(0);
		
		return reconstructedNode;
	}
	
	private void addChildNodes(STreeBuilder<SNode> treeBuilder, List<SNode> nodes) {
	    for (SNode node : nodes) {
	        // Ajout du contenu du nœud
	        if (node.contents() != null) {
	            treeBuilder.atom(node.contents());
	        }
	        // Si le nœud a des enfants, créer un nouveau nœud et ajoutez les enfants récursivement
	        if (!node.children().isEmpty()) {
	            treeBuilder.startNode();
	            addChildNodes(treeBuilder, node.children());
	            treeBuilder.endNode();
	        }
	    }
	}

}
	


