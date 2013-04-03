/* Generated By:JavaCC: Do not edit this line. DplParserJavacc.java */
package com.clican.pluto.dataprocess.dpl.parser.javacc;

import java.util.ArrayList;
import java.util.List;

import com.clican.pluto.dataprocess.dpl.function.impl.Divide;
import com.clican.pluto.dataprocess.dpl.function.impl.Minus;
import com.clican.pluto.dataprocess.dpl.function.impl.Multi;
import com.clican.pluto.dataprocess.dpl.function.impl.Plus;
import com.clican.pluto.dataprocess.dpl.parser.bean.Column;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.CompareType;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.dpl.parser.object.Select;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.AndFilter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.CompareFilter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.Filter;
import com.clican.pluto.dataprocess.dpl.parser.object.filter.OrFilter;

public class DplParserJavacc implements DplParserJavaccConstants {

	final public void BindVariable() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case S_BIND:
			jj_consume_token(S_BIND);
			break;
		case 50:
			jj_consume_token(50);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case S_NUMBER:
				jj_consume_token(S_NUMBER);
				break;
			case S_IDENTIFIER:
				jj_consume_token(S_IDENTIFIER);
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case 51:
					jj_consume_token(51);
					jj_consume_token(S_IDENTIFIER);
					break;
				default:
					jj_la1[0] = jj_gen;
					;
				}
				break;
			default:
				jj_la1[1] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			break;
		default:
			jj_la1[2] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final public String SubQuery() throws ParseException {
		SelectStatement();
		{
			if (true)
				return "";
		}
		throw new Error("Missing return statement in function");
	}

	final public void parse() throws ParseException {
		SelectStatement();
		FromClause();
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case K_WHERE:
			WhereClause();
			break;
		default:
			jj_la1[3] = jj_gen;
			;
		}
	}

	final public Select SelectStatement() throws ParseException {
		Select select;
		jj_consume_token(K_SELECT);
		select = SelectList();
		{
			if (true)
				return select;
		}
		throw new Error("Missing return statement in function");
	}

	final public Select SelectList() throws ParseException {
		List<Column> columns = new ArrayList<Column>();
		Select select = new Select(columns);
		Column column = null;
		column = SelectItem();
		columns.add(column);
		label_1: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 52:
				;
				break;
			default:
				jj_la1[4] = jj_gen;
				break label_1;
			}
			jj_consume_token(52);
			column = SelectItem();
			columns.add(column);
		}
		{
			if (true)
				return select;
		}
		throw new Error("Missing return statement in function");
	}

	final public String OracleObjectName() throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case S_IDENTIFIER:
			jj_consume_token(S_IDENTIFIER);
			{
				if (true)
					return token.image;
			}
			break;
		case S_QUOTED_IDENTIFIER:
			jj_consume_token(S_QUOTED_IDENTIFIER);
			String s = token.image;
			{
				if (true)
					return s.substring(1, s.length() - 1);
			}
			break;
		default:
			jj_la1[5] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		throw new Error("Missing return statement in function");
	}

	final public Column SelectItem() throws ParseException {
		String columnName = null;
		PrefixAndSuffix pas;
		pas = SQLSimpleExpression();
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case K_AS:
		case S_IDENTIFIER:
		case S_QUOTED_IDENTIFIER:
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_AS:
				jj_consume_token(K_AS);
				break;
			default:
				jj_la1[6] = jj_gen;
				;
			}
			columnName = OracleObjectName();
			break;
		default:
			jj_la1[7] = jj_gen;
			;
		}
		if (columnName == null || columnName.length() == 0) {
			if (pas.getFunction() != null) {
			} else {
				String suffix = pas.getSuffix();
				String prefix = pas.getPrefix();
				if (suffix == null || suffix.length() == 0) {
					columnName = prefix;
				} else {
					if (suffix.contains(".")) {
						columnName = suffix.substring(suffix.lastIndexOf(".") + 1);
					} else {
						columnName = suffix;
					}
				}
			}
		}
		Column col = new Column(pas, columnName);
		{
			if (true)
				return col;
		}
		throw new Error("Missing return statement in function");
	}

	final public From FromClause() throws ParseException {
		List<String> variableNames = new ArrayList<String>();
		From from = new From(variableNames);
		String tableName = null;
		jj_consume_token(K_FROM);
		tableName = TableReference();
		variableNames.add(tableName);
		label_2: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 52:
				;
				break;
			default:
				jj_la1[8] = jj_gen;
				break label_2;
			}
			jj_consume_token(52);
			tableName = TableReference();
			variableNames.add(tableName);
		}
		{
			if (true)
				return from;
		}
		throw new Error("Missing return statement in function");
	}

	final public String TableReference() throws ParseException {
		String tableName = null;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case S_IDENTIFIER:
		case S_QUOTED_IDENTIFIER:
			tableName = TableName();
			break;
		case K_LEFT_BRACE:
			jj_consume_token(K_LEFT_BRACE);
			SubQuery();
			jj_consume_token(K_RIGHT_BRACE);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_AS:
				jj_consume_token(K_AS);
				tableName = OracleObjectName();
				break;
			default:
				jj_la1[9] = jj_gen;
				;
			}
			{
				if (true)
					return tableName;
			}
			break;
		default:
			jj_la1[10] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		throw new Error("Missing return statement in function");
	}

	final public PrefixAndSuffix SQLSimpleExpression() throws ParseException {
		Function function = null;
		List<Object> params = new ArrayList<Object>();
		Object param;
		param = SQLMultiplicativeExpression();
		params.add(param);
		label_3: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 53:
			case 54:
				;
				break;
			default:
				jj_la1[11] = jj_gen;
				break label_3;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 53:
				jj_consume_token(53);
				if (function != null) {
					params.clear();
					params.add(function);
				}
				function = new Plus();
				try {
					function.setParams(params);
				} catch (Exception e) {
					{
						if (true)
							throw new ParseException(e.getMessage());
					}
				}
				break;
			case 54:
				jj_consume_token(54);
				if (function != null) {
					params.clear();
					params.add(function);
				}
				function = new Minus();
				try {
					function.setParams(params);
				} catch (Exception e) {
					{
						if (true)
							throw new ParseException(e.getMessage());
					}
				}
				break;
			default:
				jj_la1[12] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
			param = SQLMultiplicativeExpression();
			params.add(param);
		}
		if (function != null) {
			{
				if (true)
					return new PrefixAndSuffix(function);
			}
		} else {
			PrefixAndSuffix pas;
			if (param instanceof Function) {
				pas = new PrefixAndSuffix((Function) param);
			} else {
				try {
					pas = new PrefixAndSuffix((String) param);
				} catch (Exception e) {
					{
						if (true)
							throw new ParseException(e.getMessage());
					}
				}

			}
			{
				if (true)
					return pas;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public Object SQLMultiplicativeExpression() throws ParseException {
		Function function = null;
		List<Object> params = new ArrayList<Object>();
		Object param;
		param = SQLExponentExpression();
		params.add(param);
		label_4: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 55:
			case 56:
				;
				break;
			default:
				jj_la1[13] = jj_gen;
				break label_4;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 55:
				jj_consume_token(55);
				if (function != null) {
					params.clear();
					params.add(function);
				}
				function = new Multi();
				try {
					function.setParams(params);
				} catch (Exception e) {
					{
						if (true)
							throw new ParseException(e.getMessage());
					}
				}
				break;
			case 56:
				jj_consume_token(56);
				if (function != null) {
					params.clear();
					params.add(function);
				}
				function = new Divide();
				try {
					function.setParams(params);
				} catch (Exception e) {
					{
						if (true)
							throw new ParseException(e.getMessage());
					}
				}
				param = SQLExponentExpression();
				params.add(param);
				break;
			default:
				jj_la1[14] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		if (function != null) {
			{
				if (true)
					return function;
			}
		} else {
			{
				if (true)
					return param;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public Object SQLExponentExpression() throws ParseException {
		Object all = "";
		all = SQLPrimaryExpression();
		{
			if (true)
				return all;
		}
		throw new Error("Missing return statement in function");
	}

	final public Object SQLPrimaryExpression() throws ParseException {
		Object expr = null;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case S_NUMBER:
			jj_consume_token(S_NUMBER);
			break;
		case S_CHAR_LITERAL:
			jj_consume_token(S_CHAR_LITERAL);
			break;
		case K_LEFT_BRACE:
			jj_consume_token(K_LEFT_BRACE);
			if (jj_2_1(3)) {
				SelectStatement();
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case K_LEFT_BRACE:
				case S_NUMBER:
				case S_IDENTIFIER:
				case S_BIND:
				case S_CHAR_LITERAL:
				case S_QUOTED_IDENTIFIER:
				case 50:
					expr = SQLSimpleExpression();
					break;
				default:
					jj_la1[15] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
			jj_consume_token(K_RIGHT_BRACE);
			break;
		case S_BIND:
		case 50:
			BindVariable();
			break;
		default:
			jj_la1[16] = jj_gen;
			if (jj_2_2(2147483647)) {
				expr = FunctionCall();
			} else {
				switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
				case S_IDENTIFIER:
				case S_QUOTED_IDENTIFIER:
					expr = TableColumn();
					break;
				default:
					jj_la1[17] = jj_gen;
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
		}
		if (expr != null) {
			{
				if (true)
					return expr;
			}
		} else {
			{
				if (true)
					return token.image;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public String FunctionReference() throws ParseException {
		String name;
		name = ObjectReference();
		{
			if (true)
				return name;
		}
		throw new Error("Missing return statement in function");
	}

	final public Function FunctionCall() throws ParseException {
		String functionName = "";
		Function function = null;
		List<Object> params = new ArrayList<Object>();
		functionName = FunctionReference();
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.clican.pluto.dataprocess.dpl.function.impl." + functionName.substring(0, 1).toUpperCase() + functionName.substring(1));
			function = (Function) clazz.newInstance();
		} catch (Exception e) {

		}
		jj_consume_token(K_LEFT_BRACE);
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case K_LEFT_BRACE:
		case S_NUMBER:
		case S_IDENTIFIER:
		case S_BIND:
		case S_CHAR_LITERAL:
		case S_QUOTED_IDENTIFIER:
		case 50:
			params = FunctionArgumentList();
			break;
		default:
			jj_la1[18] = jj_gen;
			;
		}
		jj_consume_token(K_RIGHT_BRACE);
		try {
			function.setParams(params);
		} catch (Exception e) {
			{
				if (true)
					throw new ParseException(e.getMessage());
			}
		}
		{
			if (true)
				return function;
		}
		throw new Error("Missing return statement in function");
	}

	final public List<Object> FunctionArgumentList() throws ParseException {
		List<Object> params = new ArrayList<Object>();
		Object param;
		param = FunctionArgument();
		params.add(param);
		label_5: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 52:
				;
				break;
			default:
				jj_la1[19] = jj_gen;
				break label_5;
			}
			jj_consume_token(52);
			param = FunctionArgument();
			params.add(param);
		}
		{
			if (true)
				return params;
		}
		throw new Error("Missing return statement in function");
	}

	final public Object FunctionArgument() throws ParseException {
		Object param;
		if (jj_2_3(2)) {
			param = OracleObjectName();
			jj_consume_token(57);
		} else if (jj_2_4(4)) {
			param = OracleObjectName();
			jj_consume_token(51);
			OracleObjectName();
			jj_consume_token(57);
		} else {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_LEFT_BRACE:
			case S_NUMBER:
			case S_IDENTIFIER:
			case S_BIND:
			case S_CHAR_LITERAL:
			case S_QUOTED_IDENTIFIER:
			case 50:
				param = SQLSimpleExpression();
				break;
			default:
				jj_la1[20] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		{
			if (true)
				return param;
		}
		throw new Error("Missing return statement in function");
	}

	final public Filter WhereClause() throws ParseException {
		Filter filter;
		jj_consume_token(K_WHERE);
		filter = FilterExpression();
		{
			if (true)
				return filter;
		}
		throw new Error("Missing return statement in function");
	}

	final public Filter FilterExpression() throws ParseException {
		List<Filter> filterList = new ArrayList<Filter>();
		Filter filter = null;
		filter = FilterAndExpression();
		filterList.add(filter);
		label_6: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_OR:
				;
				break;
			default:
				jj_la1[21] = jj_gen;
				break label_6;
			}
			jj_consume_token(K_OR);
			filter = FilterAndExpression();
			filterList.add(filter);
		}
		Filter previousFilter = filterList.get(0);
		for (int i = 1; i < filterList.size(); i++) {
			previousFilter = new OrFilter(previousFilter, filterList.get(i));
		}
		{
			if (true)
				return previousFilter;
		}
		throw new Error("Missing return statement in function");
	}

	final public Filter FilterAndExpression() throws ParseException {
		List<Filter> filterList = new ArrayList<Filter>();
		Filter filter = null;
		filter = FilterBraceExpression();
		filterList.add(filter);
		label_7: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_AND:
				;
				break;
			default:
				jj_la1[22] = jj_gen;
				break label_7;
			}
			jj_consume_token(K_AND);
			filter = FilterBraceExpression();
			filterList.add(filter);
		}
		Filter previousFilter = filterList.get(0);
		for (int i = 1; i < filterList.size(); i++) {
			previousFilter = new AndFilter(previousFilter, filterList.get(i));
		}
		{
			if (true)
				return previousFilter;
		}
		throw new Error("Missing return statement in function");
	}

	final public Filter FilterBraceExpression() throws ParseException {
		Filter filter = null;
		if (jj_2_5(2147483647)) {
			jj_consume_token(K_LEFT_BRACE);
			filter = FilterExpression();
			jj_consume_token(K_RIGHT_BRACE);
		} else {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case K_LEFT_BRACE:
			case S_NUMBER:
			case S_IDENTIFIER:
			case S_BIND:
			case S_CHAR_LITERAL:
			case S_QUOTED_IDENTIFIER:
			case 50:
				filter = CompareExpression();
				break;
			default:
				jj_la1[23] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		{
			if (true)
				return filter;
		}
		throw new Error("Missing return statement in function");
	}

	final public CompareFilter CompareExpression() throws ParseException {
		PrefixAndSuffix left = null;
		PrefixAndSuffix right = null;
		CompareType compareType = null;
		CompareFilter filter = null;
		/*
		 * Only after looking past "(", Expression() and "," we will know that
		 * it is expression list
		 */

		left = SQLSimpleExpression();
		compareType = OperatorExpression();
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case K_LEFT_BRACE:
		case S_NUMBER:
		case S_IDENTIFIER:
		case S_BIND:
		case S_CHAR_LITERAL:
		case S_QUOTED_IDENTIFIER:
		case 50:
			right = SQLSimpleExpression();
			break;
		default:
			jj_la1[24] = jj_gen;
			;
		}
		try {
			filter = new CompareFilter(left, right, compareType);
			{
				if (true)
					return filter;
			}
		} catch (Exception e) {
			{
				if (true)
					throw new ParseException(e.getMessage());
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public CompareType OperatorExpression() throws ParseException {
		String operation = null;
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case K_LEFT_OUTER_JOIN:
			jj_consume_token(K_LEFT_OUTER_JOIN);
			break;
		case K_NOT_EQUAL:
			jj_consume_token(K_NOT_EQUAL);
			break;
		case K_GREAT_EQUAL:
			jj_consume_token(K_GREAT_EQUAL);
			break;
		case K_AFTER_NEAR:
			jj_consume_token(K_AFTER_NEAR);
			break;
		case K_GREAT:
			jj_consume_token(K_GREAT);
			break;
		case K_LESS_EQUAL:
			jj_consume_token(K_LESS_EQUAL);
			break;
		case K_BEFORE_NEAR:
			jj_consume_token(K_BEFORE_NEAR);
			break;
		case K_LESS:
			jj_consume_token(K_LESS);
			break;
		case K_NOT_IN:
			jj_consume_token(K_NOT_IN);
			break;
		case K_IN:
			jj_consume_token(K_IN);
			break;
		case K_IS_NOT_NULL:
			jj_consume_token(K_IS_NOT_NULL);
			break;
		case K_IS_NULL:
			jj_consume_token(K_IS_NULL);
			break;
		case K_IS_NOT_EMPTY:
			jj_consume_token(K_IS_NOT_EMPTY);
			break;
		case K_IS_EMPTY:
			jj_consume_token(K_IS_EMPTY);
			break;
		case K_NOT_LIKE:
			jj_consume_token(K_NOT_LIKE);
			break;
		case K_LIKE:
			jj_consume_token(K_LIKE);
			break;
		case K_EQUAL:
			jj_consume_token(K_EQUAL);
			break;
		default:
			jj_la1[25] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		operation = token.image;
		;
		for (CompareType compareType : CompareType.values()) {
			if (operation.contains(compareType.getOperation())) {
				{
					if (true)
						return compareType;
				}
			}
		}
		{
			if (true)
				return null;
		}
		throw new Error("Missing return statement in function");
	}

	final public String TableColumn() throws ParseException {
		String expr;
		expr = ObjectReference();
		{
			if (true)
				return expr;
		}
		throw new Error("Missing return statement in function");
	}

	final public String TableName() throws ParseException {
		String s;
		s = OracleObjectName();
		{
			if (true)
				return s;
		}
		throw new Error("Missing return statement in function");
	}

	final public String ObjectReference() throws ParseException {
		String s;
		StringBuilder name = new StringBuilder();
		s = OracleObjectName();
		name.append(s);
		switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
		case 51:
			jj_consume_token(51);
			s = OracleObjectName();
			name.append(".").append(s);
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case 51:
				jj_consume_token(51);
				s = OracleObjectName();
				name.append(".").append(s);
				break;
			default:
				jj_la1[26] = jj_gen;
				;
			}
			break;
		default:
			jj_la1[27] = jj_gen;
			;
		}
		{
			if (true)
				return name.toString();
		}
		throw new Error("Missing return statement in function");
	}

	private boolean jj_2_1(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_1();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(0, xla);
		}
	}

	private boolean jj_2_2(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_2();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(1, xla);
		}
	}

	private boolean jj_2_3(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_3();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(2, xla);
		}
	}

	private boolean jj_2_4(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_4();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(3, xla);
		}
	}

	private boolean jj_2_5(int xla) {
		jj_la = xla;
		jj_lastpos = jj_scanpos = token;
		try {
			return !jj_3_5();
		} catch (LookaheadSuccess ls) {
			return true;
		} finally {
			jj_save(4, xla);
		}
	}

	private boolean jj_3R_21() {
		if (jj_3R_23())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_24()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_34() {
		if (jj_3R_18())
			return true;
		return false;
	}

	private boolean jj_3R_19() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(6))
			jj_scanpos = xsp;
		if (jj_3R_10())
			return true;
		return false;
	}

	private boolean jj_3R_15() {
		if (jj_3R_18())
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_19())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3R_36() {
		if (jj_3R_9())
			return true;
		if (jj_scan_token(K_LEFT_BRACE))
			return true;
		return false;
	}

	private boolean jj_3_5() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(5)) {
			jj_scanpos = xsp;
			if (jj_scan_token(23)) {
				jj_scanpos = xsp;
				if (jj_scan_token(27)) {
					jj_scanpos = xsp;
					if (jj_scan_token(37))
						return true;
				}
			}
		}
		return false;
	}

	private boolean jj_3R_14() {
		if (jj_scan_token(S_QUOTED_IDENTIFIER))
			return true;
		return false;
	}

	private boolean jj_3R_13() {
		if (jj_scan_token(S_IDENTIFIER))
			return true;
		return false;
	}

	private boolean jj_3R_10() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_13()) {
			jj_scanpos = xsp;
			if (jj_3R_14())
				return true;
		}
		return false;
	}

	private boolean jj_3R_9() {
		if (jj_3R_12())
			return true;
		return false;
	}

	private boolean jj_3R_26() {
		if (jj_scan_token(54))
			return true;
		return false;
	}

	private boolean jj_3R_11() {
		if (jj_3R_15())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_16()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3_2() {
		if (jj_3R_9())
			return true;
		if (jj_scan_token(K_LEFT_BRACE))
			return true;
		return false;
	}

	private boolean jj_3R_20() {
		if (jj_scan_token(51))
			return true;
		if (jj_3R_10())
			return true;
		return false;
	}

	private boolean jj_3R_17() {
		if (jj_scan_token(51))
			return true;
		if (jj_3R_10())
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_20())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3R_12() {
		if (jj_3R_10())
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_17())
			jj_scanpos = xsp;
		return false;
	}

	private boolean jj_3_1() {
		if (jj_3R_8())
			return true;
		return false;
	}

	private boolean jj_3R_33() {
		if (jj_3R_37())
			return true;
		return false;
	}

	private boolean jj_3R_32() {
		if (jj_3R_36())
			return true;
		return false;
	}

	private boolean jj_3R_39() {
		if (jj_scan_token(S_IDENTIFIER))
			return true;
		return false;
	}

	private boolean jj_3R_31() {
		if (jj_3R_35())
			return true;
		return false;
	}

	private boolean jj_3R_25() {
		if (jj_scan_token(53))
			return true;
		return false;
	}

	private boolean jj_3R_30() {
		if (jj_scan_token(K_LEFT_BRACE))
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3_1()) {
			jj_scanpos = xsp;
			if (jj_3R_34())
				return true;
		}
		return false;
	}

	private boolean jj_3R_8() {
		if (jj_scan_token(K_SELECT))
			return true;
		if (jj_3R_11())
			return true;
		return false;
	}

	private boolean jj_3R_27() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(39)) {
			jj_scanpos = xsp;
			if (jj_scan_token(48)) {
				jj_scanpos = xsp;
				if (jj_3R_30()) {
					jj_scanpos = xsp;
					if (jj_3R_31()) {
						jj_scanpos = xsp;
						if (jj_3R_32()) {
							jj_scanpos = xsp;
							if (jj_3R_33())
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_22() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_25()) {
			jj_scanpos = xsp;
			if (jj_3R_26())
				return true;
		}
		return false;
	}

	private boolean jj_3R_23() {
		if (jj_3R_27())
			return true;
		return false;
	}

	private boolean jj_3R_37() {
		if (jj_3R_12())
			return true;
		return false;
	}

	private boolean jj_3R_18() {
		if (jj_3R_21())
			return true;
		Token xsp;
		while (true) {
			xsp = jj_scanpos;
			if (jj_3R_22()) {
				jj_scanpos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_38() {
		if (jj_scan_token(50))
			return true;
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(39)) {
			jj_scanpos = xsp;
			if (jj_3R_39())
				return true;
		}
		return false;
	}

	private boolean jj_3R_35() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_scan_token(47)) {
			jj_scanpos = xsp;
			if (jj_3R_38())
				return true;
		}
		return false;
	}

	private boolean jj_3R_29() {
		if (jj_scan_token(56))
			return true;
		return false;
	}

	private boolean jj_3_4() {
		if (jj_3R_10())
			return true;
		if (jj_scan_token(51))
			return true;
		if (jj_3R_10())
			return true;
		if (jj_scan_token(57))
			return true;
		return false;
	}

	private boolean jj_3R_24() {
		Token xsp;
		xsp = jj_scanpos;
		if (jj_3R_28()) {
			jj_scanpos = xsp;
			if (jj_3R_29())
				return true;
		}
		return false;
	}

	private boolean jj_3R_28() {
		if (jj_scan_token(55))
			return true;
		return false;
	}

	private boolean jj_3_3() {
		if (jj_3R_10())
			return true;
		if (jj_scan_token(57))
			return true;
		return false;
	}

	private boolean jj_3R_16() {
		if (jj_scan_token(52))
			return true;
		return false;
	}

	/** Generated Token Manager. */
	public DplParserJavaccTokenManager token_source;
	SimpleCharStream jj_input_stream;
	/** Current token. */
	public Token token;
	/** Next token. */
	public Token jj_nt;
	private int jj_ntk;
	private Token jj_scanpos, jj_lastpos;
	private int jj_la;
	private int jj_gen;
	final private int[] jj_la1 = new int[28];
	static private int[] jj_la1_0;
	static private int[] jj_la1_1;
	static {
		jj_la1_init_0();
		jj_la1_init_1();
	}

	private static void jj_la1_init_0() {
		jj_la1_0 = new int[] { 0x0, 0x0, 0x0, 0x8000000, 0x0, 0x0, 0x40, 0x40, 0x0, 0x40, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x800000,
				0x20, 0x0, 0x0, 0xf07ec000, 0x0, 0x0, };
	}

	private static void jj_la1_init_1() {
		jj_la1_1 = new int[] { 0x80000, 0x1080, 0x48000, 0x0, 0x100000, 0x21000, 0x0, 0x21000, 0x100000, 0x0, 0x21020, 0x600000, 0x600000, 0x1800000,
				0x1800000, 0x790a0, 0x580a0, 0x21000, 0x790a0, 0x100000, 0x790a0, 0x0, 0x0, 0x790a0, 0x790a0, 0x1f, 0x80000, 0x80000, };
	}

	final private JJCalls[] jj_2_rtns = new JJCalls[5];
	private boolean jj_rescan = false;
	private int jj_gc = 0;

	/** Constructor with InputStream. */
	public DplParserJavacc(java.io.InputStream stream) {
		this(stream, null);
	}

	/** Constructor with InputStream and supplied encoding */
	public DplParserJavacc(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source = new DplParserJavaccTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream) {
		ReInit(stream, null);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream.ReInit(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Constructor. */
	public DplParserJavacc(java.io.Reader stream) {
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new DplParserJavaccTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Constructor with generated Token Manager. */
	public DplParserJavacc(DplParserJavaccTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	/** Reinitialise. */
	public void ReInit(DplParserJavaccTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 28; i++)
			jj_la1[i] = -1;
		for (int i = 0; i < jj_2_rtns.length; i++)
			jj_2_rtns[i] = new JJCalls();
	}

	private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			if (++jj_gc > 100) {
				jj_gc = 0;
				for (int i = 0; i < jj_2_rtns.length; i++) {
					JJCalls c = jj_2_rtns[i];
					while (c != null) {
						if (c.gen < jj_gen)
							c.first = null;
						c = c.next;
					}
				}
			}
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	static private final class LookaheadSuccess extends java.lang.Error {

		/**
		 * 
		 */
		private static final long serialVersionUID = 388878315757056328L;
	}

	final private LookaheadSuccess jj_ls = new LookaheadSuccess();

	private boolean jj_scan_token(int kind) {
		if (jj_scanpos == jj_lastpos) {
			jj_la--;
			if (jj_scanpos.next == null) {
				jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
			} else {
				jj_lastpos = jj_scanpos = jj_scanpos.next;
			}
		} else {
			jj_scanpos = jj_scanpos.next;
		}
		if (jj_rescan) {
			int i = 0;
			Token tok = token;
			while (tok != null && tok != jj_scanpos) {
				i++;
				tok = tok.next;
			}
			if (tok != null)
				jj_add_error_token(kind, i);
		}
		if (jj_scanpos.kind != kind)
			return true;
		if (jj_la == 0 && jj_scanpos == jj_lastpos)
			throw jj_ls;
		return false;
	}

	/** Get the next Token. */
	final public Token getNextToken() {
		if (token.next != null)
			token = token.next;
		else
			token = token.next = token_source.getNextToken();
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	/** Get the specific Token. */
	final public Token getToken(int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null)
				t = t.next;
			else
				t = t.next = token_source.getNextToken();
		}
		return t;
	}

	private int jj_ntk() {
		if ((jj_nt = token.next) == null)
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		else
			return (jj_ntk = jj_nt.kind);
	}

	private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
	private int[] jj_expentry;
	private int jj_kind = -1;
	private int[] jj_lasttokens = new int[100];
	private int jj_endpos;

	private void jj_add_error_token(int kind, int pos) {
		if (pos >= 100)
			return;
		if (pos == jj_endpos + 1) {
			jj_lasttokens[jj_endpos++] = kind;
		} else if (jj_endpos != 0) {
			jj_expentry = new int[jj_endpos];
			for (int i = 0; i < jj_endpos; i++) {
				jj_expentry[i] = jj_lasttokens[i];
			}
			jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
				int[] oldentry = (int[]) (it.next());
				if (oldentry.length == jj_expentry.length) {
					for (int i = 0; i < jj_expentry.length; i++) {
						if (oldentry[i] != jj_expentry[i]) {
							continue jj_entries_loop;
						}
					}
					jj_expentries.add(jj_expentry);
					break jj_entries_loop;
				}
			}
			if (pos != 0)
				jj_lasttokens[(jj_endpos = pos) - 1] = kind;
		}
	}

	/** Generate ParseException. */
	public ParseException generateParseException() {
		jj_expentries.clear();
		boolean[] la1tokens = new boolean[58];
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 28; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
					if ((jj_la1_1[i] & (1 << j)) != 0) {
						la1tokens[32 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 58; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.add(jj_expentry);
			}
		}
		jj_endpos = 0;
		jj_rescan_token();
		jj_add_error_token(0, 0);
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = jj_expentries.get(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	/** Enable tracing. */
	final public void enable_tracing() {
	}

	/** Disable tracing. */
	final public void disable_tracing() {
	}

	private void jj_rescan_token() {
		jj_rescan = true;
		for (int i = 0; i < 5; i++) {
			try {
				JJCalls p = jj_2_rtns[i];
				do {
					if (p.gen > jj_gen) {
						jj_la = p.arg;
						jj_lastpos = jj_scanpos = p.first;
						switch (i) {
						case 0:
							jj_3_1();
							break;
						case 1:
							jj_3_2();
							break;
						case 2:
							jj_3_3();
							break;
						case 3:
							jj_3_4();
							break;
						case 4:
							jj_3_5();
							break;
						}
					}
					p = p.next;
				} while (p != null);
			} catch (LookaheadSuccess ls) {
			}
		}
		jj_rescan = false;
	}

	private void jj_save(int index, int xla) {
		JJCalls p = jj_2_rtns[index];
		while (p.gen > jj_gen) {
			if (p.next == null) {
				p = p.next = new JJCalls();
				break;
			}
			p = p.next;
		}
		p.gen = jj_gen + xla - jj_la;
		p.first = token;
		p.arg = xla;
	}

	static final class JJCalls {
		int gen;
		Token first;
		int arg;
		JJCalls next;
	}

}