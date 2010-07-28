/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @see org.hibernate.tool.hbm2ddl.ForeignKeyMetadata
 * 
 * @author wezhang
 * 
 */
public class ForeignKeyMetadata {
	private final String name;
	private final List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();

	ForeignKeyMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("FK_NAME");
	}

	public String getName() {
		return name;
	}

	public void addColumn(ColumnMetadata column) {
		if (column != null)
			columns.add(column);
	}

	public ColumnMetadata[] getColumns() {
		return (ColumnMetadata[]) columns.toArray(new ColumnMetadata[0]);
	}

	public String toString() {
		return "ForeignKeyMetadata(" + name + ')';
	}
}

// $Id$