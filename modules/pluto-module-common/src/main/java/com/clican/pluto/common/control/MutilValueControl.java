/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.control;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class MutilValueControl extends Control {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4111062491595060620L;

	private String[] providedSelection;

	public String[] getProvidedSelection() {
		return providedSelection;
	}

	public void setProvidedSelection(String[] providedSelection) {
		this.providedSelection = providedSelection;
	}

	@Override
	public Control getCloneBean() {
		MutilValueControl control = (MutilValueControl) super.getCloneBean();
		if (this.providedSelection != null) {
			String[] providedSelection = new String[this.providedSelection.length];
			for (int i = 0; i < this.providedSelection.length; i++) {
				providedSelection[i] = this.providedSelection[i];
			}
			control.setProvidedSelection(providedSelection);
		}
		return control;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(158117211, -1358546635).appendSuper(
				super.hashCode()).append(this.providedSelection).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof MutilValueControl)) {
			return false;
		}
		MutilValueControl rhs = (MutilValueControl) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				this.providedSelection, rhs.providedSelection).isEquals();
	}

}

// $Id$