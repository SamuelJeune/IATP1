package pacman.environnementRL;

import java.util.ArrayList;

import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */


public class EtatPacmanMDPClassic implements Etat , Cloneable{
    private final static int PRIME_NUMBER = 31;
    private class Position {

        private int x;
        private int y;

        Position(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position that = (Position) o;

            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return PRIME_NUMBER * x + y;
        }
    }
	private ArrayList<Position> ghosts;
	private ArrayList<Position> dots;
	private ArrayList<Position> pacmen;

	EtatPacmanMDPClassic(StateGamePacman stateGamePacman){

		//Pacmen
		this.pacmen = new ArrayList<>();
		for (int i = 0; i < stateGamePacman.getNumberOfPacmans(); i++) {
			if(!stateGamePacman.getPacmanState(i).isDead()){
				this.pacmen.add(new Position(stateGamePacman.getPacmanState(i).getX(), stateGamePacman.getPacmanState(i).getY()));
			}
		}

		//Ghosts
		this.ghosts = new ArrayList<>();
		for (int i = 0; i < stateGamePacman.getNumberOfGhosts();i++) {
			if(!stateGamePacman.getGhostState(i).isDead()){
				this.ghosts.add(new Position(stateGamePacman.getGhostState(i).getX(), stateGamePacman.getGhostState(i).getY()));
			}
		}

		//Dots
		this.dots = new ArrayList<>();
		for(int i = 0; i < stateGamePacman.getMaze().getSizeX(); i++){
			for(int j = 0; j < stateGamePacman.getMaze().getSizeY(); j++){
				if(stateGamePacman.getMaze().isFood(i, j)){
					this.dots.add(new Position(i, j));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "";
	}


	@Override
	public int hashCode() {
		int result = ghosts != null ? ghosts.hashCode() : 0;
		result = PRIME_NUMBER * result + (dots != null ? dots.hashCode() : 0);
		result = PRIME_NUMBER * result + (pacmen != null ? pacmen.hashCode() : 0);
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

	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        return (ghosts != null ? ghosts.equals(that.ghosts) : that.ghosts == null) && (dots != null ? dots.equals(that.dots) : that.dots == null) && (pacmen != null ? pacmen.equals(that.pacmen) : that.pacmen == null);
    }

	

}
