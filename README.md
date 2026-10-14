# Projet Robi — projet de synthèse L3 (Java)

Interpréteur de scripts graphiques en **S-expressions**, utilisable à distance via une architecture **client / serveur**.
Le client (IHM JavaFX) envoie un script au serveur, qui l'interprète, dessine le résultat dans un espace graphique 2D et renvoie une capture de l'image au client.

## Exemple de script

```lisp
( space add robi ( Rect new ) )
( space.robi setColor yellow )
( space.robi translate 50 50 )

( space addScript addRect ( ( self name w c ) ( self add name ( Rect new ) ) ( self.name setColor c ) ( self.name setDim w w ) ) )
( space addRect mySquare 30 black )

( space addImage alien.gif )
( space clear )
```

## Structure

| Module | Rôle |
|---|---|
| [2DGraphicCore/](2DGraphicCore/src/graphicLayer) | Couche graphique Swing : `GSpace` (fenêtre/espace), `GRect`, `GOval`, `GImage`, `GString`, `GLine`… |
| [SParser/](SParser/src/stree/parser) | Parseur de S-expressions → arbre de `SNode` (+ tests JUnit) |
| [Robi/src/exercice6/](Robi/src/exercice6) | Cœur de l'interpréteur : `Environment` (références nommées), `Reference` (objet + commandes), `Script` (commandes définies par l'utilisateur via `addScript`) |
| [Robi/src/Commandes/](Robi/src/Commandes) | Commandes du langage : `new`, `add`, `del`, `clear`, `setColor`, `setDim`, `translate`, `addScript`… |
| [Robi/src/](Robi/src) | Réseau et IHM : `ServeurRobi`, `ClientRobi`, `IHMClient` + `Controller` + `chat.fxml` (JavaFX) |
| [Robi/src/JSON/](Robi/src/JSON) | Sérialisation des messages client ↔ serveur avec Jackson 2.10 (jars fournis) |

## Fonctionnement

1. Le client se connecte au serveur (adresse IP + port) par socket TCP.
2. Les messages sont des objets `DataToSend` sérialisés en JSON, typés par `TypeMessage` :
   - `SEND` : envoi du script, que le serveur parse et stocke ;
   - `PAS` : exécution pas à pas (une S-expression à la fois, le serveur renvoie l'image et l'instruction exécutée) ;
   - `BLOC` : exécution de tout le script d'un coup ;
   - `CLEAR` : effacement de l'espace graphique.
3. Le serveur renvoie l'image du `GSpace` encodée en PNG/Base64, affichée dans l'IHM.
4. Le serveur est multi-clients (un thread `ClientServ` par connexion).

## Lancement

Prérequis : JDK 11+, **JavaFX** (pour le client), les jars Jackson présents dans `Robi/src/JSON/`.
Le projet n'a pas de build tool : les trois dossiers `src` (2DGraphicCore, SParser, Robi) doivent être sur le même classpath (projet Eclipse/IntelliJ d'origine).

```bash
# Serveur
java ServeurRobi 8000

# Client (avec JavaFX sur le module path)
java --module-path <javafx/lib> --add-modules javafx.controls,javafx.fxml IHMClient
```

Dans l'IHM : saisir l'adresse (`localhost`) et le port, se connecter, écrire ou charger un script, l'envoyer, puis choisir le mode d'exécution (pas à pas ou bloc).

## Remarques

- `Exercice6.java` dépend de `exercice5.Exercice5` et `tools.Tools`, absents de ce dépôt. L'import de `exercice5` peut être supprimé, car `Exercice6` définit sa propre classe interne `Interpreter`. En revanche, `Tools.readKeyboard()` sert à la boucle en mode console (`mainLoop`) : il faut ajouter cette classe ou retirer cette boucle pour compiler.
