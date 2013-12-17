package com.rngtng.irdude.database;

public class Control {
	public int id;
	public int oriId;
	public int idBrand;
	public String name;
	
	public Control(int id, int oriId, int idBrand, String name) {
		super();
		this.id = id;
		this.oriId = oriId;
		this.idBrand = idBrand;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	

}
