/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

public enum VoteResult {

	// 同意
	AGREE("agree", "同意"),
	// 不同意
	DISAGREE("disagree", "不同意"),
	// 弃权
	DISCLAIM("disclaim", "弃权");

	private String voteResult;

	private String voteDesc;

	private VoteResult(String voteResult, String voteDesc) {
		this.voteResult = voteResult;
		this.voteDesc = voteDesc;
	}

	public String getVoteResult() {
		return voteResult;
	}

	public String getVoteDesc() {
		return voteDesc;
	}

	public static VoteResult convert(String voteResult) {
		for (VoteResult member : values()) {
			if (member.getVoteResult().equals(voteResult)) {
				return member;
			}
		}
		return DISCLAIM;
	}

}

// $Id$