package com.zw.admin.server.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 2054813493011812469L;

	protected ID id;
	private Date createTime;
	private Date updateTime;

}
