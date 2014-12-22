package at.ac.tuwien.ifs.sos.strategies;

public class RetaliationStrategy extends DefaultStrategy {

	private static final long serialVersionUID = 1L;

	public void setConfession() {
		prisonerWillConfess = true;
		if (lastRound != null) {
			if (gameInfo.getPrisoner1().equals(myAgent.getAID())) {
				prisonerWillConfess = lastRound.getConfession2();
			} else {
				prisonerWillConfess = lastRound.getConfession1();
			}
		}
	}
}
