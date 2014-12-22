package at.ac.tuwien.ifs.sos.strategies;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;

import at.ac.tuwien.ifs.sos.GameInfo;
import at.ac.tuwien.ifs.sos.GameRound;

public class TittyForTattyStrategy extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public TittyForTattyStrategy() {
	}

	private void print(String text) {
		System.out.println(myAgent.getAID().getLocalName()
				+ " [TittyForTattyStrategy] - " + text);
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

			Boolean comply;

			if (lastRound != null) {
				if (gameInfo.getPrisoner1().equals(myAgent.getAID()))
					comply = lastRound.getConfession2();
				else
					comply = lastRound.getConfession1();
			} else {
				comply = true;
			}
			
			print("response: comply=" + comply);
			
			ACLMessage inform = query.createReply();

			inform.setPerformative(ACLMessage.INFORM);
			inform.setContentObject(comply);

			getDataStore().put(p.RESULT_NOTIFICATION_KEY, inform);

			

		} catch (UnreadableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
