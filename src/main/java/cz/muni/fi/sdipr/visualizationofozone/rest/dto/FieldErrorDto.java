package cz.muni.fi.sdipr.visualizationofozone.rest.dto;

import cz.muni.fi.sdipr.visualizationofozone.rest.exception.MesssageType;

public class FieldErrorDto {

	private String field;

	private String message;

	private MesssageType type;

	public FieldErrorDto(String field, String message) {
		this.field = field;
		this.message = message;
		this.type = MesssageType.ERROR;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MesssageType getType() {
		return type;
	}

	public void setType(MesssageType type) {
		this.type = type;
	}

}