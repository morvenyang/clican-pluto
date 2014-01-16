package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MULTI_CHOICE")
@Entity
public class Bingo implements Metadata{

	private Long id;
	private String picture1;
	private String word1;
	private String recording1;
	private String picture2;
	private String word2;
	private String recording2;
	private String picture3;
	private String word3;
	private String recording3;
	private String picture4;
	private String word4;
	private String recording4;
	private String picture5;
	private String word5;
	private String recording5;
	private String picture6;
	private String word6;
	private String recording6;
	private String picture7;
	private String word7;
	private String recording7;
	private String picture8;
	private String word8;
	private String recording8;
	private String picture9;
	private String word9;
	private String recording9;
	private String picture10;
	private String word10;
	private String recording10;
	private String picture11;
	private String word11;
	private String recording11;
	private String picture12;
	private String word12;
	private String recording12;
	private String picture13;
	private String word13;
	private String recording13;
	private String picture14;
	private String word14;
	private String recording14;
	private String picture15;
	private String word15;
	private String recording15;
	private String picture16;
	private String word16;
	private String recording16;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getPicture1() {
		return picture1;
	}

	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}

	@Column
	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}

	@Column
	public String getRecording1() {
		return recording1;
	}

	public void setRecording1(String recording1) {
		this.recording1 = recording1;
	}

	@Column
	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	@Column
	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}

	@Column
	public String getRecording2() {
		return recording2;
	}

	public void setRecording2(String recording2) {
		this.recording2 = recording2;
	}

	@Column
	public String getPicture3() {
		return picture3;
	}

	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}

	@Column
	public String getWord3() {
		return word3;
	}

	public void setWord3(String word3) {
		this.word3 = word3;
	}

	@Column
	public String getRecording3() {
		return recording3;
	}

	public void setRecording3(String recording3) {
		this.recording3 = recording3;
	}

	@Column
	public String getPicture4() {
		return picture4;
	}

	public void setPicture4(String picture4) {
		this.picture4 = picture4;
	}

	@Column
	public String getWord4() {
		return word4;
	}

	public void setWord4(String word4) {
		this.word4 = word4;
	}

	@Column
	public String getRecording4() {
		return recording4;
	}

	public void setRecording4(String recording4) {
		this.recording4 = recording4;
	}

	@Column
	public String getPicture5() {
		return picture5;
	}

	public void setPicture5(String picture5) {
		this.picture5 = picture5;
	}

	@Column
	public String getWord5() {
		return word5;
	}

	public void setWord5(String word5) {
		this.word5 = word5;
	}

	@Column
	public String getRecording5() {
		return recording5;
	}

	public void setRecording5(String recording5) {
		this.recording5 = recording5;
	}

	@Column
	public String getPicture6() {
		return picture6;
	}

	public void setPicture6(String picture6) {
		this.picture6 = picture6;
	}

	@Column
	public String getWord6() {
		return word6;
	}

	public void setWord6(String word6) {
		this.word6 = word6;
	}

	@Column
	public String getRecording6() {
		return recording6;
	}

	public void setRecording6(String recording6) {
		this.recording6 = recording6;
	}

	@Column
	public String getPicture7() {
		return picture7;
	}

	public void setPicture7(String picture7) {
		this.picture7 = picture7;
	}

	@Column
	public String getWord7() {
		return word7;
	}

	public void setWord7(String word7) {
		this.word7 = word7;
	}

	@Column
	public String getRecording7() {
		return recording7;
	}

	public void setRecording7(String recording7) {
		this.recording7 = recording7;
	}

	@Column
	public String getPicture8() {
		return picture8;
	}

	public void setPicture8(String picture8) {
		this.picture8 = picture8;
	}

	@Column
	public String getWord8() {
		return word8;
	}

	public void setWord8(String word8) {
		this.word8 = word8;
	}

	@Column
	public String getRecording8() {
		return recording8;
	}

	public void setRecording8(String recording8) {
		this.recording8 = recording8;
	}

	@Column
	public String getPicture9() {
		return picture9;
	}

	public void setPicture9(String picture9) {
		this.picture9 = picture9;
	}

	@Column
	public String getWord9() {
		return word9;
	}

	public void setWord9(String word9) {
		this.word9 = word9;
	}

	@Column
	public String getRecording9() {
		return recording9;
	}

	public void setRecording9(String recording9) {
		this.recording9 = recording9;
	}

	@Column
	public String getPicture10() {
		return picture10;
	}

	public void setPicture10(String picture10) {
		this.picture10 = picture10;
	}

	@Column
	public String getWord10() {
		return word10;
	}

	public void setWord10(String word10) {
		this.word10 = word10;
	}

	@Column
	public String getRecording10() {
		return recording10;
	}

	public void setRecording10(String recording10) {
		this.recording10 = recording10;
	}

	@Column
	public String getPicture11() {
		return picture11;
	}

	public void setPicture11(String picture11) {
		this.picture11 = picture11;
	}

	@Column
	public String getWord11() {
		return word11;
	}

	public void setWord11(String word11) {
		this.word11 = word11;
	}

	@Column
	public String getRecording11() {
		return recording11;
	}

	public void setRecording11(String recording11) {
		this.recording11 = recording11;
	}

	@Column
	public String getPicture12() {
		return picture12;
	}

	public void setPicture12(String picture12) {
		this.picture12 = picture12;
	}

	@Column
	public String getWord12() {
		return word12;
	}

	public void setWord12(String word12) {
		this.word12 = word12;
	}

	@Column
	public String getRecording12() {
		return recording12;
	}

	public void setRecording12(String recording12) {
		this.recording12 = recording12;
	}

	@Column
	public String getPicture13() {
		return picture13;
	}

	public void setPicture13(String picture13) {
		this.picture13 = picture13;
	}

	@Column
	public String getWord13() {
		return word13;
	}

	public void setWord13(String word13) {
		this.word13 = word13;
	}

	@Column
	public String getRecording13() {
		return recording13;
	}

	public void setRecording13(String recording13) {
		this.recording13 = recording13;
	}

	@Column
	public String getPicture14() {
		return picture14;
	}

	public void setPicture14(String picture14) {
		this.picture14 = picture14;
	}

	@Column
	public String getWord14() {
		return word14;
	}

	public void setWord14(String word14) {
		this.word14 = word14;
	}

	@Column
	public String getRecording14() {
		return recording14;
	}

	public void setRecording14(String recording14) {
		this.recording14 = recording14;
	}

	@Column
	public String getPicture15() {
		return picture15;
	}

	public void setPicture15(String picture15) {
		this.picture15 = picture15;
	}

	@Column
	public String getWord15() {
		return word15;
	}

	public void setWord15(String word15) {
		this.word15 = word15;
	}

	@Column
	public String getRecording15() {
		return recording15;
	}

	public void setRecording15(String recording15) {
		this.recording15 = recording15;
	}

	@Column
	public String getPicture16() {
		return picture16;
	}

	public void setPicture16(String picture16) {
		this.picture16 = picture16;
	}

	@Column
	public String getWord16() {
		return word16;
	}

	public void setWord16(String word16) {
		this.word16 = word16;
	}

	@Column
	public String getRecording16() {
		return recording16;
	}

	public void setRecording16(String recording16) {
		this.recording16 = recording16;
	}

}
