package at.ac.tuwien.ifs.sos;

import jade.util.leap.Serializable;

public class GameRound implements Serializable{

	private int id;
	private boolean confession1;
	private boolean confession2;

	public GameRound() {
		super();
	}

	public GameRound(int id, boolean confession1, boolean confession2) {
		super();
		this.id = id;
		this.confession1 = confession1;
		this.confession2 = confession2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConfession1() {
		return confession1;
	}

	public void setConfession1(boolean confession1) {
		this.confession1 = confession1;
	}

	public boolean isConfession2() {
		return confession2;
	}

	public void setConfession2(boolean confession2) {
		this.confession2 = confession2;
	}

	@Override
	public String toString() {
		return "GameRound [id=" + id + ", confession1=" + confession1
				+ ", confession2=" + confession2 + "]";
	}
	

}
