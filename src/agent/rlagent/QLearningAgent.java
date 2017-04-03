package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour les clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
	}


	
	
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}else{
			//*** VOTRE CODE
			double valeurAction = 0;
			for (Action a : this.getActionsLegales(e)) {
				if(this.getQValeur(e,a)>valeurAction){
					returnactions.clear();
					returnactions.add(a);
					valeurAction=this.getQValeur(e,a);
				}else if(this.getQValeur(e,a)==valeurAction){
					returnactions.add(a);
				}
			}
		}

		return returnactions;
	}
	
	@Override
	public double getValeur(Etat e) {
		//*** VOTRE CODE
		double valeurAction = 0.0;
		for (Action a : this.getActionsLegales(e)) {
			if(this.getQValeur(e,a)>valeurAction){
				valeurAction=this.getQValeur(e,a);
			}
		}
		return valeurAction;
		
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
		if(qvaleurs.containsKey(e)){
			if(qvaleurs.get(e).containsKey(a)){
				return qvaleurs.get(e).get(a);
			}
		}
		return 0;
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE

		if(qvaleurs.containsKey(e)){
			if(qvaleurs.get(e).containsKey(a)){
				qvaleurs.get(e).replace(a,d);
			}else{
				qvaleurs.get(e).put(a,d);
			}
		}else{
			HashMap<Action,Double> aValeur = new HashMap<>();
			aValeur.put(a,d);
			qvaleurs.put(e, aValeur);
		}
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		//*** VOTRE CODE
		double valeurAction = reward+gamma*(getValeur(esuivant));
		setQValeur(e,a,valeurAction);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		qvaleurs.clear();
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
