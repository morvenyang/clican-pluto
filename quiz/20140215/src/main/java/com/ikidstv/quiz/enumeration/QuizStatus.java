package com.ikidstv.quiz.enumeration;

public enum QuizStatus {

	INIT(1),

	AUDITING(2),

	PUBLISHED(3),

	REJECTED(4),

	UNUNSED(5);

	private int status;

	private QuizStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static QuizStatus convert(int status) {
		for (QuizStatus member : values()) {
			if (member.getStatus() == status) {
				return member;
			}
		}
		return null;
	}

}
