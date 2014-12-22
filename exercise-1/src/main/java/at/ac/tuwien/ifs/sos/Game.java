package at.ac.tuwien.ifs.sos;

import java.util.Stack;

import jade.core.AID;

public class Game {
	private AID prisoner1;
	private AID prisoner2;

	private int iterations;
	private Stack<GameRound> rounds = new Stack<GameRound>();

	public Game(AID prisoner12, AID prisoner22, int iterations) {
		super();
		this.prisoner1 = prisoner12;
		this.prisoner2 = prisoner22;
		this.iterations = iterations;
	}

	public AID getPrisoner1() {
		return prisoner1;
	}

	public void setPrisoner1(AID prisoner1) {
		this.prisoner1 = prisoner1;
	}

	public AID getPrisoner2() {
		return prisoner2;
	}

	public void setPrisoner2(AID prisoner2) {
		this.prisoner2 = prisoner2;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public void pushRound(GameRound round) {
		rounds.push(round);
	}
}
