package at.ac.tuwien.ifs.sos;

import java.io.IOException;
import java.security.acl.NotOwnerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

public class PrisonMasterAgent extends Agent {
	private static final long serialVersionUID = 1L;
	private GameInfo gameInfo;

	private void print(String text) {
		System.out.println("GM " + getAID().getLocalName() + " - " + text);
	}

	@Override
	protected void setup() {

		print("New PrisonMaster: " + getAID().getName());

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
			
			print("prepared request to prisoners");
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
				print("handleAllResultNotifications with notif count: "
						+ notifications.size());
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
			print("GAME ENDED YEY");
		}
	}

}
