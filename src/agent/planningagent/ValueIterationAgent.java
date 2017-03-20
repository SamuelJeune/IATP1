package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param nbIterations
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		double vtemp = 0.0;
		double vsum = 0.0;
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon
		this.delta=0.0;
		//*** VOTRE CODE
		this.vmax = 0;
		this.vmin = 0;
		for (Etat etat : this.mdp.getEtatsAccessibles()) {
			vtemp = 0.0;
			for(Action action : this.mdp.getActionsPossibles(etat)) {
				try {
					Map<Etat, Double> transitionProba = mdp.getEtatTransitionProba(etat, action);
					vsum = 0.0;
					for (Map.Entry<Etat, Double> etatPossible : transitionProba.entrySet()) {
						vsum += etatPossible.getValue() * (this.mdp.getRecompense(etat, action, etatPossible.getKey()) + this.gamma * this.V.get(etatPossible.getKey()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				vtemp = Math.max(vtemp, vsum);
			}
			this.delta = Math.max(this.delta, Math.abs(vtemp - this.V.get(etat)));
			V.put(etat, vtemp);


			// mise a jour vmax et vmin pour affichage du gradient de couleur:
			//vmax est la valeur  max de V  pour tout s
			//vmin est la valeur min de V  pour tout s
			this.vmax = Math.max(this.vmax, this.V.get(etat));
			this.vmin = Math.min(this.vmin, this.V.get(etat));
		}


		//******************* laisser la notification a la fin de la methode
		this.notifyObs();
	}


//	public void updateV(){
//		//delta est utilise pour detecter la convergence de l'algorithme
//		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
//		//delta < epsilon
//		this.delta=0.0;
//		//*** VOTRE CODE
//
//
//
//		// mise a jour vmax et vmin pour affichage du gradient de couleur:
//		//vmax est la valeur  max de V  pour tout s
//		//vmin est la valeur min de V  pour tout s
//		// ...
//
//		//******************* laisser la notification a la fin de la methode
//
//        HashMap<Etat, Double> Vk = V;
//        List<Action> actions = null;
//        Map<Etat,Double> transition = null;
//        double maxval = 0;
//		int sum = 0;
//        for (Etat etat:this.mdp.getEtatsAccessibles()){
//            actions = mdp.getActionsPossibles(etat);
//			sum = 0;
//            for (Action action:actions){
//                try{
//                    transition = mdp.getEtatTransitionProba(etat, action);
//					for (Map.Entry<Etat, Double> etatPossible : transition.entrySet()) {
//						sum += etatPossible.getValue() * (this.mdp.getRecompense(etat, action, etatPossible.getKey()) + this.gamma * this.V.get(etatPossible.getKey()));
//						System.out.println(gamma*(V.get(etatPossible.getKey())));
//                    }
//					if (sum>maxval){
//						maxval = sum;
//					}
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//			this.delta = Math.max(this.delta, Math.abs(maxval - this.V.get(etat)));
//			V.put(etat,maxval);
//			}
//
//		this.notifyObs();
//	}

    /**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0) {
			System.out.println("Action2D.NONE");
			return Action2D.NONE;
		}
		else{//choix aleatoire
			return actions.get(rand.nextInt(actions.size()));
		}

		
	}
	@Override
	public double getValeur(Etat _e) {
		return  V.get(_e);
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override

	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		// retourne action de meilleure valeur dans _e selon V,
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();
		double psum = 0.0;
		double ptemp = 0.0;
		Map<Action, Double> ptempList = new HashMap<>();

		for(Action action : this.mdp.getActionsPossibles(_e)) {
			try {
				Map<Etat, Double> transitionProba = mdp.getEtatTransitionProba(_e, action);
				psum = 0.0;
				for (Map.Entry<Etat, Double> etatPossible : transitionProba.entrySet()) {
					psum += etatPossible.getValue() * (this.mdp.getRecompense(_e, action, etatPossible.getKey()) + this.gamma * this.V.get(etatPossible.getKey()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ptempList.put(action, psum);
			ptemp = Math.max(ptemp, psum);
		}

		for (Map.Entry<Action, Double> ptempListPossible : ptempList.entrySet()) {
			if(ptempListPossible.getValue() == ptemp)
				returnactions.add(ptempListPossible.getKey());
		}

		return returnactions;

	}



	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}
