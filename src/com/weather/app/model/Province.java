package com.weather.app.model;

public class Province {
	private int id;
	private String p_name;
	private String p_code;

	public Province() {
	}

	public Province(int id, String name, String code) {
		this.id = id;
		this.p_name = name;
		this.p_code = code;
	}

	public Province(String code, String name) {
		this.p_code = code;
		this.p_name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_code() {
		return p_code;
	}

	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
}