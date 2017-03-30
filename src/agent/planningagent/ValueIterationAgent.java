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
		this.delta=0.0;
		this.vmax = 0;
		this.vmin = 0;
		for (Etat etat : this.mdp.getEtatsAccessibles()) {
			vtemp = -Double.MAX_VALUE;
			for(Action action : this.mdp.getActionsPossibles(etat)) {
				vsum = getPsum(etat, vsum, action);
				vtemp = Math.max(vtemp, vsum);
			}
			if (this.mdp.estAbsorbant(etat)){
                V.put(etat, 0.0);
				this.delta = Math.max(this.delta, Math.abs(0.0 - this.V.get(etat)));
			} else {
                V.put(etat, vtemp);
				this.delta = Math.max(this.delta, Math.abs(vtemp - this.V.get(etat)));
			}

			// màj vmin et vmax pour le dégradé :
			this.vmax = Math.max(this.vmax, this.V.get(etat));
			this.vmin = Math.min(this.vmin, this.V.get(etat));
		}

		this.notifyObs();
	}

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

	public List<Action> getPolitique(Etat etat) {
        List<Action> returnactions = new ArrayList<Action>();
        Double max = -Double.MAX_VALUE;

        for (Action action : mdp.getActionsPossibles(etat)){
            try {
                Double sum = 0.0;
                sum = getPsum(etat, sum, action);
                if(sum >= max){
                    if(sum.equals(max)){
                        returnactions.add(action);
                    }
                    else {
                        max = sum;
                        returnactions.clear();
                        returnactions.add(action);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnactions;

	}

	private double getPsum(Etat etat, double psum, Action action) {
		try {
            Map<Etat, Double> transitionProba = mdp.getEtatTransitionProba(etat, action);
            psum = 0.0;
            for (Map.Entry<Etat, Double> etatPossible : transitionProba.entrySet()) {
                psum += etatPossible.getValue() * (this.mdp.getRecompense(etat, action, etatPossible.getKey()) + this.gamma * this.V.get(etatPossible.getKey()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return psum;
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
