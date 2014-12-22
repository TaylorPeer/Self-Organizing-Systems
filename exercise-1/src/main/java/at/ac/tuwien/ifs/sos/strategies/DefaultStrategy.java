package at.ac.tuwien.ifs.sos.strategies;

import java.io.IOException;

import at.ac.tuwien.ifs.sos.GameInfo;
import at.ac.tuwien.ifs.sos.GameRound;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class DefaultStrategy extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public DefaultStrategy() {
	}

	private void print(String text) {
		System.out.println(myAgent.getAID().getLocalName()
				+ " [DefaultStrategy] - " + text);
	}

	@Override
	public void action() {

		AchieveREResponder p = (AchieveREResponder) parent;

		ACLMessage query = (ACLMessage) getDataStore().get(p.REQUEST_KEY);

		try {
			GameInfo gameInfo = (GameInfo) query.getContentObject();

			if (gameInfo == null) {
				print("ERROR: gameInfo = null");
				return;
			}

			GameRound lastRound = gameInfo.getLastRound();
			print("received lastRound: " + lastRound);

			ACLMessage inform = query.createReply();

			inform.setPerformative(ACLMessage.INFORM);
			inform.setContentObject(new Boolean(true));

			getDataStore().put(p.RESULT_NOTIFICATION_KEY, inform);

			print("response: " + true);

		} catch (UnreadableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
