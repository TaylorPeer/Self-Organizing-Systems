package at.ac.tuwien.ifs.sos;

import java.io.IOException;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class TestBehavior extends CyclicBehaviour{

	private static final long serialVersionUID = 1L;

	private void print(String text){
		System.out.println(myAgent.getAID().getLocalName() + " - " + text);
	}
	
	@Override
	public void action() {

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg!=null){
			
			try {
				Boolean test = (Boolean) msg.getContentObject();
				
				if(test){
					
					ACLMessage reply = msg.createReply();
					
					reply.setContentObject(new Boolean(false));
					
					print("received: " + test + "- reply: " + false);
					
				}else{
					ACLMessage reply = msg.createReply();
					
					reply.setContentObject(new Boolean(true));
					
					print("received: " + test + "- reply: " + true);
				}
				
			} catch (UnreadableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			
		}
		else{
			block();
		}
		
		
		
	}

}
