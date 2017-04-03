package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{


	public int[] pacmanCoord = new int[2];
	public List<Integer> ghostX = new ArrayList<>();
	public List<Integer> ghostY = new ArrayList<>();
	public int closestDot;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		this.pacmanCoord[0]=_stategamepacman.getPacmanState(0).getX();
		this.pacmanCoord[1]=_stategamepacman.getPacmanState(0).getY();
		for (StateAgentPacman ghost: _stategamepacman.getGhosts_states()) {
			ghostX.add(ghost.getX());
			ghostY.add(ghost.getY());
		}
		this.closestDot=-_stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
	}
	
	@Override
	public String toString() {
		
		return "";
	}


	@Override
	public int hashCode() {
		int result = Arrays.hashCode(pacmanCoord);
		result = 31 * result + (ghostX != null ? ghostX.hashCode() : 0);
		result = 31 * result + (ghostY != null ? ghostY.hashCode() : 0);
		result = 31 * result + closestDot;
		return result;
	}

	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}



	

}
