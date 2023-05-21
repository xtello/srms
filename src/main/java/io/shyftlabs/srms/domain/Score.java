package io.shyftlabs.srms.domain;

public enum Score {
	A,
	B,
	C,
	D,
	E,
	F;


	public static Score fromString(String score) {
		if (score == null) {
			return null;
		}
		return valueOf(score.toUpperCase());
	}
}
