/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.comparator;

import java.util.Comparator;

import com.clican.pluto.orm.dynamic.inter.IDirectory;

public class DirectoryDataModelComparator<T> extends BaseComparator<T>
		implements Comparator<T> {

	public int compare(T o1, T o2) {
		boolean o1IsDirectory = false;
		boolean o2IsDirectory = false;
		if (o1 instanceof IDirectory) {
			o1IsDirectory = true;
		}

		if (o2 instanceof IDirectory) {
			o2IsDirectory = true;
		}
		if (o1IsDirectory && !o2IsDirectory) {
			return convertByOrder(-1);
		} else if (!o1IsDirectory && o2IsDirectory) {
			return convertByOrder(1);
		} else {
			return 0;
		}
	}
}

// $Id$