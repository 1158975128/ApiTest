package com.zw.admin.server.model;

import lombok.Data;

@Data
public class UserGame {
	private Long id;

	private String userId;

	private String username;

	private Long gameId;

	private String gamename;

	private Long isBuy;
}
