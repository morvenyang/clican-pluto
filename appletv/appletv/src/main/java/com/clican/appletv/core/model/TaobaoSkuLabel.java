package com.clican.appletv.core.model;

public class TaobaoSkuLabel implements Comparable<TaobaoSkuLabel> {

	private String labelName;

	private String labelValue;

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((labelValue == null) ? 0 : labelValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaobaoSkuLabel other = (TaobaoSkuLabel) obj;
		if (labelValue == null) {
			if (other.labelValue != null)
				return false;
		} else if (!labelValue.equals(other.labelValue))
			return false;
		return true;
	}

	@Override
	public int compareTo(TaobaoSkuLabel o) {
		return this.getLabelName().compareTo(o.getLabelName());
	}

	public String getLabelValue() {
		return labelValue;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}

}
