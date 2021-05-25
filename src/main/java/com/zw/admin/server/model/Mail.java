package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Mail extends BaseEntity<Long> {

	private static final long serialVersionUID = 5613231124043303948L;

	private String userId;
	private String toUsers;
	private String subject;
	private String content;

}
