package at.ac.tuwien.ifs.sos.strategies;

public class RandomStrategy extends DefaultStrategy {

	private static final long serialVersionUID = 1L;

	public void setConfession() {
		prisonerWillConfess = Math.random() < 0.5;
	}

}
