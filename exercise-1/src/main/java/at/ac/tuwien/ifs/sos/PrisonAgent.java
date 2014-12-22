package at.ac.tuwien.ifs.sos;

import at.ac.tuwien.ifs.sos.strategies.DefaultStrategy;
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

	private static final String DEFAULT_STRAT = "default";
	private static final String RANDOM_STRAT = "random";
	private static final String TITTAT_STRAT = "tittat";
	private String strategy;

	private void print(String text) {
		System.out.println(getAID().getLocalName() + " - " + text);
	}

	@Override
	protected void setup() {

		print("Started PrisonAgent: " + getAID().getName());

		handleArguments();

		addBehaviour(createResponder());

		print("setup complete");

	}

	private void handleArguments() {
		Object[] args = getArguments();

		if (args == null || args.length != 1) {
			// print("Error: need to supply at least one argument for strategie: <titForTat> or <naive>");
			print("no or wrong strategie argument; set default strategy");
			strategy = DEFAULT_STRAT;

		} else {
			String strat = (String) args[0];

			// print(strat + " , not implemented LOL ; set TitForTatStrategy");

			if (!strat.equals(RANDOM_STRAT) && !strat.equals(TITTAT_STRAT)) {
				print("wrong strategie argument; set default strategy");
				strategy = DEFAULT_STRAT;
			} else {
				strategy = strat;
			}

		}

	}

	private AchieveREResponder createResponder() {
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
				// print("handled request successfully");
				return agree;
			}
		};

		if (strategy.equals(TITTAT_STRAT)) {
			arer.registerPrepareResultNotification(new TittyForTattyStrategy());
		} else if (strategy.equals(RANDOM_STRAT)) {
			arer.registerPrepareResultNotification(new RandomStrategy());
		} else {
			arer.registerPrepareResultNotification(new DefaultStrategy());
		}
		print("set responding strategy to " + strategy);
		return arer;
	}

}