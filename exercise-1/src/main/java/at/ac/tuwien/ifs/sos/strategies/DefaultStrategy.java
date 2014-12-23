package at.ac.tuwien.ifs.sos.strategies;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import at.ac.tuwien.ifs.sos.entities.GameInfo;
import at.ac.tuwien.ifs.sos.entities.GameRound;

public class DefaultStrategy extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	public GameInfo gameInfo;
	public GameRound lastRound;
	public boolean prisonerWillConfess;

	public void setConfession() {
		prisonerWillConfess = true;
	}

	protected void print(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t");
		sb.append(myAgent.getAID().getLocalName());
		sb.append(": ");
		sb.append(text);
		sb.append(" ");
		sb.append("(" + this.getClass().getSimpleName() + ")");

		System.out.println(sb.toString());
	}

	@Override
	public void action() {

		AchieveREResponder p = (AchieveREResponder) parent;
		ACLMessage query = (ACLMessage) getDataStore().get(p.REQUEST_KEY);

		try {

			// Set gameInfo and lastRound variables, which are used by some strategies
			gameInfo = (GameInfo) query.getContentObject();
			if (gameInfo != null) {
				lastRound = gameInfo.getLastRound();
			}

			setConfession();

			ACLMessage inform = query.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			inform.setContentObject(prisonerWillConfess);

			getDataStore().put(p.RESULT_NOTIFICATION_KEY, inform);

			print(prisonerWillConfess ? "guity" : "not guilty");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
