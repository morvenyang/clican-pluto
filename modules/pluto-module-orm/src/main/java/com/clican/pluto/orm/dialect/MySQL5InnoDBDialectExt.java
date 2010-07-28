/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.mapping.Column;

public class MySQL5InnoDBDialectExt extends MySQL5InnoDBDialect implements
		DialectExtention {

	public String getModifyColumnString(Column column) {
		return "modify column";
	}

	public String getRenameColumnString(String oldName, String newName) {
		return "change column " + oldName + " " + newName;
	}

	public boolean isAddColumnDefinitionWhenRename() {
		return true;
	}

	public boolean needDropForeignKeyBeforeDropColumn() {
		return true;
	}

	public String getDropFKString() {
		return "drop foreign key";
	}

	
}

// $Id$