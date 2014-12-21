package at.ac.tuwien.ifs.sos;

import jade.core.Agent;

public class HelloAgent extends Agent {

	private static final long serialVersionUID = 4771420969544225263L;

	protected void setup() {
		System.out.println("Hello World. ");
		System.out.println("My name is " + getLocalName());
	}
}
