package com.weather.app.model;

public class County {
	private int id;
	private int c_id;
	private String co_name;
	private String co_code;

	public County() {
	}

	public County(int id, int cid, String name, String code) {
		this.id = id;
		this.c_id = cid;
		this.co_code = code;
		this.co_name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public String getCo_name() {
		return co_name;
	}

	public void setCo_name(String co_name) {
		this.co_name = co_name;
	}

	public String getCo_code() {
		return co_code;
	}

	public void setCo_code(String co_code) {
		this.co_code = co_code;
	}
}