package com.zw.admin.server.model;

import java.util.Date;

public class Language extends BaseEntity<Long> {

	private String k;
	private String val;

	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}

}
