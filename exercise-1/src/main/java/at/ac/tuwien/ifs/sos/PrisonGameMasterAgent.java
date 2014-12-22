package at.ac.tuwien.ifs.sos;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class PrisonGameMasterAgent extends Agent {
	private static final long serialVersionUID = 1L;
	private GameInfo gameInfo;

	private void print(String text) {
		System.out.println("PRISONGAME: " + getAID().getLocalName() + " - "
				+ text);
	}

	@Override
	protected void setup() {

		print("New PrisonGameMasterAgent: " + getAID().getName());

		handleArguments();

		registerService();

		SequentialBehaviour gameRoundBehaviours = new SequentialBehaviour(this);

		for (int i = 0; i < gameInfo.getIterations(); i++) {
			gameRoundBehaviours.addSubBehaviour(new RoundBehaviour(this, null));
		}

		SequentialBehaviour behaviour = new SequentialBehaviour(this);
		behaviour.addSubBehaviour(gameRoundBehaviours);
		behaviour.addSubBehaviour(new EndGameBehaviour(this));

		addBehaviour(behaviour);

		print("setup complete");
	}

	private void handleArguments() {
		Object[] args = getArguments();

		if (args == null || args.length < 3 || args.length > 3) {
			print("Need to supply the names of the two prisoner agents and the number of iterations.");

			doDelete();

			return;
		}

		AID prisoner1 = new AID((String) args[0], AID.ISLOCALNAME);
		AID prisoner2 = new AID((String) args[1], AID.ISLOCALNAME);

		int iterations = Integer.parseInt((String) args[2]);

		gameInfo = new GameInfo(prisoner1, prisoner2, iterations);

		print("gameInfo set to: " + gameInfo);
	}

	private void registerService() {

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName(getLocalName());
		serviceDescription.setType("prisonMaster");
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		agentDescription.addServices(serviceDescription);
			
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown(){
		print("PrisonMaster terminated");
	}
	

	private class RoundBehaviour extends AchieveREInitiator {
		private static final long serialVersionUID = 1L;

		public RoundBehaviour(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected Vector prepareRequests(ACLMessage request) {
			ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
			query.addReceiver(gameInfo.getPrisoner1());
			query.addReceiver(gameInfo.getPrisoner2());

			query.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
			query.setReplyByDate(new Date(System.currentTimeMillis() + 10000));

			try {
				query.setContentObject(gameInfo);

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			print("starting new round...");
			try {
				Vector<ACLMessage> result = new Vector<ACLMessage>(1);
				result.add(query);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			if (failure.getSender().equals(myAgent.getAMS()))
				// FAILURE notification from the JADE runtime: the receiver does
				// not exist
				print("Responder does not exist");
			else
				print("Agent failed to perform the requested action (agent: "
						+ failure.getSender().getName());
		}

		@Override
		protected void handleAllResultNotifications(Vector notifications) {

			try {
				// print("handleAllResultNotifications with notif count: "
				// + notifications.size());
				GameRound currentRound = new GameRound();

				ACLMessage inform1 = (ACLMessage) notifications.get(0);
				Boolean guilty1 = (Boolean) inform1.getContentObject();

				ACLMessage inform2 = (ACLMessage) notifications.get(1);
				Boolean guilty2 = (Boolean) inform2.getContentObject();

				if (inform1.getSender().equals(gameInfo.getPrisoner1())) {
					currentRound.setConfession1(guilty1);
					currentRound.setConfession2(guilty2);
				} else {
					currentRound.setConfession1(guilty2);
					currentRound.setConfession2(guilty1);
				}

				gameInfo.pushRound(currentRound);

				print("ROUND FINISHED: " + currentRound);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class EndGameBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		private EndGameBehaviour(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			print("GAME ENDED; calculating..");

			int years1 = 0;
			int years2 = 0;

			final int BOTH_BETRAYED = 2;
			final int ONE_BETRAYED = 3;
			final int NONE_BETRAYED = 1;

			for (GameRound round : gameInfo.getRounds()) {
				if (round.getConfession1() && round.getConfession2()) {
					years1 += BOTH_BETRAYED;
					years2 += BOTH_BETRAYED;
				} else if (round.getConfession1() && !round.getConfession2()) {
					years1 += ONE_BETRAYED;
				} else if (!round.getConfession1() && round.getConfession2()) {
					years2 += ONE_BETRAYED;
				} else {
					years1 += NONE_BETRAYED;
					years2 += NONE_BETRAYED;
				}
			}

			print("RESULT: ");

			print(gameInfo.getPrisoner1().getLocalName() + " serves " + years1
					+ " years");
			print(gameInfo.getPrisoner2().getLocalName() + " serves " + years2
					+ " years");

			if (years1 > years2) {
				print("The winner is: "
						+ gameInfo.getPrisoner2().getLocalName());

			} else if (years1 < years2) {
				print("The winner is: "
						+ gameInfo.getPrisoner1().getLocalName());
			} else {
				print("The game is a draw!");
			}
			
			doDelete();

		}
	}

}
