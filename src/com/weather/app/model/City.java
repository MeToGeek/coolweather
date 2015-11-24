package com.weather.app.model;

public class City {
	private int id;
	private int p_id;
	private String c_name;
	private String c_code;

	public City() {
	}

	public City(int id, int pid, String name, String code) {
		this.id = id;
		this.p_id = pid;
		this.c_name = name;
		this.c_code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_code() {
		return c_code;
	}

	public void setC_code(String c_code) {
		this.c_code = c_code;
	}
}