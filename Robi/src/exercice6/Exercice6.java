package exercice6;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import Commandes.AddElement;
import Commandes.AddScript;
import Commandes.ClearElement;
import Commandes.DelElement;
import Commandes.NewElement;
import Commandes.NewImage;
import Commandes.SetColor;
import Commandes.SetDim;
import Exception.CommandNotFoundException;
import Exception.InvalidCommandArgumentsException;
import Exception.ReferenceNotFoundException;
import Exception.UnknownColorException;
import exercice5.Exercice5.Interpreter;
import graphicLayer.GImage;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/* 
( space add robi (Rect new ) )

( space addScript addRect (( self name w c ) ( self add name ( Rect new ) ) ( self.name setColor c ) ( self.name setDim w w ) ) )

( space addScript addImage (( self filename )(self add im ( Image new filename ) ) ) )
( space addImage alien.gif )

( space addRect mySquare 30 yellow )
( space.mySquare translate 50 50 )
( space addRect mySquare2 20 black )

( space addScript empty ( ( self ) ( self clear ) ) )
( space empty )

( space add robi (Rect new ) )
( space add bla (Rect new ) )
( space.bla translate 40 40 )
( space clear )

 */

public class Exercice6 {
	Environment environment = new Environment();
	private GSpace space;

	public Exercice6() {
		
		this.space = new GSpace("Exercice 6", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference imageClassRef = new Reference(GImage.class);
		
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("setDim", new SetDim());
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("addScript", new AddScript(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		spaceRef.addCommand("clear", new ClearElement(environment));
	
		rectClassRef.addCommand("new", new NewElement(environment));
		imageClassRef.addCommand("new", new NewImage());
		
		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Image", imageClassRef);
	}
	
	private void mainLoop() {
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();

			List<SNode> compiled = parseScript(input);
			
			// execution des s-expressions compilees
			executeBloc(compiled);
		}
	}
	
	public List<SNode> parseScript(String script) {
		
		SParser<SNode> parser = new SParser<>();
		// compilation
		List<SNode> compiled = null;
		try {
			compiled = parser.parse(script);
			System.out.println(compiled.toString());
		} catch (IOException e) {
			System.err.println("Erreur parseur");
			e.printStackTrace();
		}
		
		return compiled;
	
	}
	
	public void executeBloc(List<SNode> compiledScript) {
		
		// execution des s-expressions compilees
		Iterator<SNode> itor = compiledScript.iterator();
		while (itor.hasNext()) {
			new Interpreter().compute(environment, itor.next());
		}
	}
	
	public void executePas(SNode node) {
		
		new Interpreter().compute(environment, node);
	}
	
	public class Interpreter {
		
		public void compute(Environment environment, SNode expr) {
			
			String receiverName;
			Reference receiver;
			
			receiverName = expr.get(0).contents();
			
			try {
				receiver = environment.getReferenceByName(receiverName);
				receiver.run(expr);
			} catch (ReferenceNotFoundException e) {
				System.out.println("La référence n'existe pas.");
			} catch (CommandNotFoundException e) {
				System.out.println("La commande n'existe pas pour cette référence.");
			} catch (InvalidCommandArgumentsException e) {
				System.out.println("Pas assez d'arguments pour exécuter la commande.");
			} catch (UnknownColorException e) {
				System.out.println("La couleur n'existe pas.");
			}
		}
		
	}
	
	public static void main(String[] args) {
		Exercice6 exo6 = new Exercice6();
		exo6.mainLoop();
	}

	public Environment getEnvironment() {
		return environment;
	}

	public GSpace getSpace() {
		return space;
	}

}
