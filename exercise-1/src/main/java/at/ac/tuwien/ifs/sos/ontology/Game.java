package at.ac.tuwien.ifs.sos.ontology;

import jade.content.Concept;

import java.io.Serializable;

public class Game implements Serializable, Concept {
	
	private Integer roundsCount;

	public Game() {
	}

	public Game(Integer rounds) {
		this.roundsCount = rounds;
	}

	public Integer getRoundsCount() {
		return roundsCount;
	}

	public void setRoundsCount(Integer roundsCount) {
		this.roundsCount = roundsCount;
	}
}
