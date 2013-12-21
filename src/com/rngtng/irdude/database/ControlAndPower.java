package com.rngtng.irdude.database;

public class ControlAndPower {
	public int id;
	public int oriId;
	public int idBrand;
	public String name;
	public Command power;
	
	public ControlAndPower(int id, int oriId, int idBrand, String name, Command power) {
		super();
		this.id = id;
		this.oriId = oriId;
		this.idBrand = idBrand;
		this.name = name;
		this.power = power;
	}

	@Override
	public String toString() {
		return name;
	}

}
