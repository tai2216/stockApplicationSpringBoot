package com.vStock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCredential {
	private String clientId;
	private String client_id;
	private String credential;
	private String select_by;
}
