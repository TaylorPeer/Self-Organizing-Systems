package at.ac.tuwien.ifs.sos;

import at.ac.tuwien.ifs.sos.strategies.DefaultStrategy;
import at.ac.tuwien.ifs.sos.strategies.RandomStrategy;
import at.ac.tuwien.ifs.sos.strategies.RetaliationStrategy;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class PrisonAgent extends Agent {

	private static final long serialVersionUID = 1L;

	private static final String STRATEGY_DEFAULT = "default";
	private static final String STRATEGY_RANDOM = "random";
	private static final String STRATEGY_RETAILIATION = "retaliation";
	private String strategy = STRATEGY_DEFAULT;

	private void print(String text) {
		System.out.println(getAID().getLocalName() + " - " + text);
	}

	@Override
	protected void setup() {
		print("Started PrisonAgent: " + getAID().getName());
		handleArguments();
		addBehaviour(createResponder());
		print("Setup of PrisonAgent " + getAID().getName() + " complete");
	}

	private void handleArguments() {
		Object[] args = getArguments();
		
		boolean validStrategy = true;
		if (args != null && args.length == 1) {
			strategy = (String) args[0];
			if (!strategy.equals(STRATEGY_RANDOM) && !strategy.equals(STRATEGY_RETAILIATION)) {
				validStrategy = false;
			}
		} else {
			validStrategy = false;
		}
		
		if (!validStrategy) {
			print("Strategy argument was missing or invalid. Using default strategy");
		}
	}

	private AchieveREResponder createResponder() {
		MessageTemplate queryMessageTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
				MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));

		AchieveREResponder arer = new AchieveREResponder(this, queryMessageTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				ACLMessage agree = request.createReply();
				agree.setPerformative(ACLMessage.AGREE);
				return agree;
			}
		};

		if (strategy.equals(STRATEGY_RETAILIATION)) {
			arer.registerPrepareResultNotification(new RetaliationStrategy());
		} else if (strategy.equals(STRATEGY_RANDOM)) {
			arer.registerPrepareResultNotification(new RandomStrategy());
		} else {
			arer.registerPrepareResultNotification(new DefaultStrategy());
		}
		print("Set strategy: " + strategy);
		return arer;
	}

}