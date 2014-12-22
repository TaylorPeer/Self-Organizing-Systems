package at.ac.tuwien.ifs.sos.strategies;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;

import at.ac.tuwien.ifs.sos.GameInfo;
import at.ac.tuwien.ifs.sos.GameRound;

public class RandomStrategy extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private void print(String text) {
		System.out.println(myAgent.getAID().getLocalName()
				+ " [RandomStrategy] - " + text);
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

			Boolean confess = Math.random() < 0.5;

			print("response: confess=" + confess);

			ACLMessage inform = query.createReply();

			inform.setPerformative(ACLMessage.INFORM);
			inform.setContentObject(confess);

			getDataStore().put(p.RESULT_NOTIFICATION_KEY, inform);

		} catch (UnreadableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
