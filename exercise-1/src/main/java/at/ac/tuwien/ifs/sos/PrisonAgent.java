package at.ac.tuwien.ifs.sos;

import jade.content.ContentElement;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionInitiator;

public class PrisonAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private void print(String text) {
		System.out.println(getAID().getLocalName() + " - " + text);
	}

	@Override
	protected void setup() {

		print("Started PrisonAgent: " + getAID().getName());

		// TODO arguments and change behavior

		DFAgentDescription gamemasterServiceTemplate = new DFAgentDescription();
		ServiceDescription gamemasterServiceTemplateSD = new ServiceDescription();
		gamemasterServiceTemplateSD.setType("prisonMaster");
		gamemasterServiceTemplate.addServices(gamemasterServiceTemplateSD);
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(1L);
		DFAgentDescription[] results = null;

		try {
			results = DFService.searchUntilFound(this, getDefaultDF(),
					gamemasterServiceTemplate, sc, 10000L);

		} catch (FIPAException e) {
			e.printStackTrace();
		}
		if (results == null) {
			System.out.println("ERROR, didnt find prisonmaster");
			return;
		}
		DFAgentDescription dfd = results[0];
		AID gamemasterAID = dfd.getName();

		ParallelBehaviour behavior = new ParallelBehaviour(this,
				ParallelBehaviour.WHEN_ALL);

		behavior.addSubBehaviour(createQueryProtocol());
		behavior.addSubBehaviour(createSubscriptionProtocol(gamemasterAID));

		addBehaviour(behavior);
		
		print("setup complete");

	}

	private AchieveREResponder createQueryProtocol() {
		MessageTemplate queryMessageTemplate = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_QUERY),
						MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));
		AchieveREResponder arer = new AchieveREResponder(this,
				queryMessageTemplate) {
			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				ACLMessage agree = request.createReply();
				agree.setPerformative(ACLMessage.AGREE);
				return agree;
			}
		};
		
		//TODO CHANGE BEHAVIOR
		arer.registerPrepareResultNotification(new TestBehavior());
		return arer;
	}

	private SubscriptionInitiator createSubscriptionProtocol(AID gamemasterAID) {
		ACLMessage subscribeMsg = new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribeMsg.addReceiver(gamemasterAID);
		subscribeMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		return new SubscriptionInitiator(this, subscribeMsg) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void handleRefuse(ACLMessage refuse) {
				print("failed to subscribe to " + refuse.getSender().getName());
				super.handleRefuse(refuse);
			}

			@Override
			protected void handleAgree(ACLMessage agree) {
				print("agreed to subscribe to " + agree.getSender().getName());
				super.handleAgree(agree);
			}

			@Override
			protected void handleInform(ACLMessage inform) {
				print("been informed by " + inform.getSender().getName());

				try {
					Game currGameInfo = (Game) inform.getContentObject();

					print("round: " + currGameInfo);

				} catch (UnreadableException e) {
					e.printStackTrace();
				}

			}
		};
	}
}