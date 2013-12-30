package com.rngtng.irdude.database;

public class Brand{
	public int id;
	public int oriId;
	public String name;
	
	public Brand(int id, int oriId, String name) {
		super();
		this.id = id;
		this.oriId = oriId;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}	

}
