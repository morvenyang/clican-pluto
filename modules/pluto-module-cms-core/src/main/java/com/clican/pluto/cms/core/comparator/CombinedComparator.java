/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.comparator;

import java.util.Comparator;
import java.util.List;

public class CombinedComparator<T> implements Comparator<T> {

	private List<Comparator<T>> comparators;

	public CombinedComparator(List<Comparator<T>> comparators) {
		this.comparators = comparators;
	}

	public int compare(T o1, T o2) {
		for (Comparator<T> comp : comparators) {
			int result = comp.compare(o1, o2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}

// $Id$