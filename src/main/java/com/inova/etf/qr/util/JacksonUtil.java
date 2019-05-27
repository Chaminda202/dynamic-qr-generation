package com.inova.etf.qr.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

	private static ObjectMapper mapper = new ObjectMapper();

	public static String getToString(Object src ) {
		try {
			return mapper.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			//error in convert object to json
			
		}
		return null;
	}
}
