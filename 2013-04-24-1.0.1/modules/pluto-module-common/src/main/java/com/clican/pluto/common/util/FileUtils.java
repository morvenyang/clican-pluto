/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.util;

import java.io.File;

public class FileUtils {

	public static void recurDelete(File file) {
		File[] files = file.listFiles();
		if (files.length != 0) {
			for (File f : files) {
				if (f.isDirectory()) {
					recurDelete(f);
				} else {
					f.delete();
				}
			}
		}
		file.delete();
	}
}

// $Id$