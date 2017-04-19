package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();

    private List<Action> actions;
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat e) {//renvoi null si e absorbant
        actions = this.agent.getActionsLegales(e);
		if (actions.isEmpty()){
			return null;
		}

		if (rand.nextDouble() <= epsilon) {
			return actions.get(rand.nextInt(actions.size()));
		} else {
			List<Action> politique = this.agent.getPolitique(e);
			if (politique.isEmpty())
				return null;
			else
				return politique.get(rand.nextInt(politique.size()));
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}
