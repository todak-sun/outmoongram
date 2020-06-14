package me.highdk.api.v1.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FetchType {
	FULL("FULL"), ORIGIN("ORIGIN");
	
	private String typeCode;
	
	FetchType(String typeCode) {
		this.typeCode = typeCode;
	}
	
	
	
	@Override
	public String toString() {
		return this.typeCode;
	}



	@JsonCreator
	public static FetchType fromValue(String typeCode) {
		switch(typeCode) {
		case "FULL":
			return FetchType.FULL;
		case "ORIGIN":
			return FetchType.ORIGIN;
		}
		return null;
	}
}
