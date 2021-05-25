package com.zw.admin.server.utils;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidateUtil {
	public static String check(BindingResult br) {
		String msg = "";
		if (br.hasErrors()) {
			List<FieldError> fieldErrors = br.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				String field = fieldError.getField();
				String message = fieldError.getDefaultMessage();
				msg += field + ":" + message + " ";
			}
		}
		return msg.trim();
	}

}
