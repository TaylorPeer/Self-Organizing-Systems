package at.ac.tuwien.ifs.sos;

import at.ac.tuwien.ifs.sos.strategies.RandomStrategy;
import at.ac.tuwien.ifs.sos.strategies.TittyForTattyStrategy;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class PrisonAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private boolean defaultStrat;

	private void print(String text) {
		System.out.println(getAID().getLocalName() + " - " + text);
	}

	@Override
	protected void setup() {

		print("Started PrisonAgent: " + getAID().getName());

		handleArguments();

		addBehaviour(createQueryProtocol());

		print("setup complete");

	}

	private void handleArguments() {
		Object[] args = getArguments();

		if (args == null || args.length != 1) {
			// print("Error: need to supply at least one argument for strategie: <titForTat> or <naive>");
			print("no or wrong strategie argument; set RandinStrategy");

			defaultStrat = true;
		} else {
			String strat = (String) args[0];

			print(strat + " , not implemented LOL ; set TitForTatStrategy");

			defaultStrat = false;

		}

	}

	private AchieveREResponder createQueryProtocol() {
		MessageTemplate queryMessageTemplate = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
						MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));

		AchieveREResponder arer = new AchieveREResponder(this,
				queryMessageTemplate) {
			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				ACLMessage agree = request.createReply();
				agree.setPerformative(ACLMessage.AGREE);
				//print("handled request successfully");
				return agree;
			}
		};

		// TODO REFACTOR BEHAVIOUR
		if (defaultStrat) {
			arer.registerPrepareResultNotification(new RandomStrategy());
		} else {
			arer.registerPrepareResultNotification(new TittyForTattyStrategy());
		}

		print("created queryProtocol");
		return arer;
	}

}