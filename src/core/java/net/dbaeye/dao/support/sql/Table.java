/**
 * @(#)Table.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <a href="Table.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Table.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Table {
	//~ Static fields/initializers =============================================
	
	//~ Instance fields ========================================================
	
	private String name;
	private String alias;
	
	private Column[] columns;
	private Column[] allColumns;
	private PrimaryKey primaryKey;
	
	private Column[] insertColumns;
	private Column[] updateColumns;
	
	private Map<String, Column> columnMap;
	
	private String _sql_select;
	private String _sql_insert;
	private String _sql_update;
	private String _sql_delete;
	
	private String _all_columns;
	private String _pk_condition;
	private String _all_insert_columns;
	
	//~ Constructors ===========================================================

	public Table() {}
	
	public Table(String name, String primaryKey, String[] columnNames) {
		this(name, new String[] { primaryKey }, columnNames);
	}
	
	public Table(String name, String[] primaryColumnNames, String[] columnNames) {
		this(name, null, primaryColumnNames, columnNames);
	}
	
	public Table(String name, String alias, String[] primaryColumnNames, String[] columnNames) {
		this.name = name;
		this.alias = alias;
		
		this.primaryKey = new PrimaryKey(primaryColumnNames);
		
		this.columns = new Column[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			this.columns[i] = new Column(columnNames[i]);
		}
		
		prepareColumns();
	}
	
	public Table(String name, PrimaryKey pk, Column[] columns) {
		this(name, null, pk, columns);
	}
	
	public Table(String name, String alias, PrimaryKey pk, Column[] columns) {
		this.name = name;
		this.alias = alias;
		this.primaryKey = pk;
		this.columns = columns;
		prepareColumns();
	}
	
	private void prepareColumns() {
		Column[] primaryColumns = this.primaryKey.getColumns();
		
		this.allColumns = new Column[primaryColumns.length + columns.length];
		
		System.arraycopy(primaryColumns, 0, this.allColumns, 0, primaryColumns.length);
		System.arraycopy(columns, 0, allColumns, primaryColumns.length, columns.length);

		this.columnMap = new HashMap<String, Column>(allColumns.length);
		
		List<Column> inserts = new ArrayList<Column>(allColumns.length);
		List<Column> updates = new ArrayList<Column>(allColumns.length);
		
		for (int i = 0; i < allColumns.length; i++) {
			allColumns[i].setTable(this);
			this.columnMap.put(allColumns[i].getName(), allColumns[i]);
			
			if (allColumns[i].isInsert()) {
				inserts.add(allColumns[i]);
			}
			if (allColumns[i].isUpdate()) {
				updates.add(allColumns[i]);
			}			
		}
		insertColumns = inserts.toArray(new Column[inserts.size()]);
		updateColumns = updates.toArray(new Column[updates.size()]);
	}

	
	//~ Methods ================================================================
	
	public Table alias(String alias) {
		return new Table(name, alias, primaryKey.getColumnNames(), getColumnNames());
	}
	
	public String[] getColumnNames() {
		String[] columnNames = new String[columns.length];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columns[i].getName();
		}
		return columnNames;
	}
	
	public Column[] getColumns() {
		return columns;
	}
	
	public Column[] getAllColumns() {
		return allColumns;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public Column column(String name) {
		return columnMap.get(name);
	}
	
	//~ SQL helpers ================================================================
	
	public String getSqlSelect() {
		if (_sql_select == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.buildSqlSelect(null, null));
			sb.append(" WHERE ").append(_primaryKeyCondition());
			
			_sql_select = sb.toString();
		}
		
		return _sql_select;
	}
	
	public String _primaryKeyCondition() {
		if (_pk_condition == null) {
			StringBuilder sb = new StringBuilder();
			Column[] primaryCols = primaryKey.getColumns();
			for (int i = 0; i < primaryCols.length; i++) {
				sb.append(primaryCols[i].toSQL()).append(" = ?");
				if (i < primaryCols.length - 1) {
					sb.append(" AND ");
				}
			}
			_pk_condition = sb.toString();
		}
		return _pk_condition;
	}
	
	public String _allInsertColumns() {
		if (_all_insert_columns == null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < insertColumns.length; i++) {
				sb.append(insertColumns[i].toSQL());
				if (i < insertColumns.length - 1) {
					sb.append(", ");
				}
			}
			_all_insert_columns = sb.toString();
		}
		return _all_insert_columns;
	}
	
	public String _allColumns() {
		if (_all_columns == null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < allColumns.length; i++) {
				sb.append(allColumns[i].toSQL());
				if (i < allColumns.length - 1) {
					sb.append(", ");
				}
			}
			_all_columns = sb.toString();
		}
		return _all_columns;
	}
	
	public String buildSqlSelect(String whereClause) {
		return buildSqlSelect(whereClause, null);
	}
	
	public String buildSqlSelect(String whereClause, String orderBy) {
		return buildSqlSelect(null, whereClause, orderBy, -1, -1);
	}
	
	public String buildSqlSelect(String selectClause, String whereClause, String orderBy) {
		return buildSqlSelect(selectClause, whereClause, orderBy, -1, -1);
	}

	public String buildSqlSelect(String whereClause, String orderBy, int offset, int limit) {
		return buildSqlSelect(null, whereClause, orderBy, offset, limit);
	}
	
	public String buildSqlSelect(String selectClause, String whereClause, String orderBy, int offset, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(selectClause == null ? _allColumns() : selectClause);
		sb.append(" FROM ").append(name);
		if (whereClause != null) {
			sb.append(" WHERE ").append(whereClause);
		}
		if (orderBy != null) {
			sb.append(" ORDER BY ").append(orderBy);
		}
		if (limit > 0) {
			sb.append(" LIMIT ?, ?");
		}
		return sb.toString();
	}
	
	public String buildSqlCount(String whereClause) {
		return buildSqlCount(null, whereClause);
	}
	
	public String buildSqlCount(String column, String whereClause) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(").append(column == null ? "*" : column).append(")");
		sb.append(" FROM ").append(name);
		if (whereClause != null) {
			sb.append(" WHERE ").append(whereClause);
		}
		return sb.toString();
	}
	
	public String buildSqlUpdate(String[] columnNames) {
		return buildSqlUpdate(columnNames, null);
	}
	
	public String buildSqlUpdate(String[] columnNames, String whereClause) {
		Column[] columns = new Column[columnNames.length];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = column(columnNames[i]);
		}
		return buildSqlUpdate(columns, whereClause);
	}
	
	public String buildSqlUpdate(Column[] columns) {
		return buildSqlUpdate(columns, null);
	}
	
	public String buildSqlUpdate(Column[] columns, String whereClause) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(name).append(" SET ");
		for (int i = 0; i < columns.length; i++) {
			sb.append(columns[i].toSQL()).append(" = ?");
			if (i < columns.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(" WHERE ");
		if (whereClause != null) {
			sb.append(whereClause);
		} else {
			sb.append(_primaryKeyCondition());
		}
		return sb.toString();
	}
	
	
	public String getSqlInsert() {
		if (_sql_insert == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ").append(name).append(" (");
			sb.append(_allInsertColumns());
			sb.append(") VALUES (");
			for (int i = 0; i < insertColumns.length; i++) {
				sb.append("?");
				if (i < insertColumns.length - 1) {
					sb.append(", ");
				}
			}
			sb.append(")");
			_sql_insert = sb.toString();
		}
		return _sql_insert;
	}
	
	public String getSqlUpdate() {
		if (_sql_update == null) {
			_sql_update = buildSqlUpdate(updateColumns);
		}
		return _sql_update;
	}
	
	public String getSqlDelete() {
		if (_sql_delete == null) {
			_sql_delete = buildSqlDelete(null);
		}
		return _sql_delete;
	}
	
	public String buildSqlDelete(String whereClause) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(name).append(" WHERE ");
		sb.append(whereClause == null ? _primaryKeyCondition() : whereClause);
		return sb.toString();
	}
}
