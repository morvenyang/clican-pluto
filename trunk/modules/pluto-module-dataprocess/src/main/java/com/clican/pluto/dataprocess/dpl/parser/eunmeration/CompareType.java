/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.eunmeration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.common.util.SearchUtils;
import com.clican.pluto.dataprocess.exception.CalculationException;

/**
 * 比较类型的枚举
 * 
 * @author wei.zhang
 * 
 */
public enum CompareType {

	LEFT_OUTER_JOIN("+="),

	NOT_EQUAL("!="),

	NOT_IN(" not in "),

	IN(" in "),

	NOT_LIKE("not like"),

	LIKE("like"),

	LESS_EQUAL("<="), BEFORE_NEAR("<~"), LESS("<"),

	GREAT_EQUAL(">="), AFTER_NEAR(">~"), GREAT(">"),

	EQUAL("="),

	IS_NULL("is null"),

	IS_NOT_NULL("is not null"),

	IS_EMPTY("is empty"),

	IS_NOT_EMPTY("is not empty")

	;

	private String operation;

	private CompareType(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	@SuppressWarnings("unchecked")
	public boolean compare(Object var1, Object var2,
			Set<Comparable<?>> varSet1, Set<Comparable<?>> varSet2)
			throws CalculationException {
		if (this == EQUAL || this == LEFT_OUTER_JOIN) {
			if (var1 != null) {
				if (var2 == null) {
					return false;
				} else {
					if (var1 instanceof String || var2 instanceof String) {
						return var1.toString().equals(var2.toString());
					} else {
						return var1.equals(var2);
					}
				}
			} else {
				return false;
			}
		} else if (this == NOT_EQUAL) {
			if (var1 != null) {
				if (var2 == null) {
					return false;
				} else {
					if (var1 instanceof String || var2 instanceof String) {
						return !var1.toString().equals(var2.toString());
					} else {
						return !var1.equals(var2);
					}
				}
			} else {
				return false;
			}
		} else if (this == LIKE) {
			if (var1 != null && var2 != null) {
				return var1.toString().matches(var2.toString());
			} else {
				return false;
			}
		} else if (this == NOT_LIKE) {
			if (var1 != null && var2 != null) {
				return !var1.toString().matches(var2.toString());
			} else {
				return false;
			}
		} else if (this == LESS) {
			if (var1 != null && var2 != null) {
				if (var1 instanceof Number && var2 instanceof Number) {
					return ((Number) var1).doubleValue() < ((Number) var2)
							.doubleValue();
				} else {
					return ((Comparable) var1).compareTo(var2) < 0;
				}
			} else {
				return false;
			}
		} else if (this == LESS_EQUAL) {
			if (var1 != null && var2 != null) {
				if (var1 instanceof Number && var2 instanceof Number) {
					return ((Number) var1).doubleValue() <= ((Number) var2)
							.doubleValue();
				} else {
					return ((Comparable) var1).compareTo(var2) <= 0;
				}
			} else {
				return false;
			}
		} else if (this == GREAT) {
			if (var1 != null && var2 != null) {
				if (var1 instanceof Number && var2 instanceof Number) {
					return ((Number) var1).doubleValue() > ((Number) var2)
							.doubleValue();
				} else {
					return ((Comparable) var1).compareTo(var2) > 0;
				}
			} else {
				return false;
			}
		} else if (this == GREAT_EQUAL) {
			if (var1 != null && var2 != null) {
				if (var1 instanceof Number && var2 instanceof Number) {
					return ((Number) var1).doubleValue() >= ((Number) var2)
							.doubleValue();
				} else {
					return ((Comparable) var1).compareTo(var2) >= 0;
				}
			} else {
				return false;
			}
		} else if (this == IS_NULL) {
			return var1 == null;
		} else if (this == IS_NOT_NULL) {
			return var1 != null;
		} else if (this == IS_EMPTY) {
			return StringUtils.isEmpty(var1.toString());
		} else if (this == IS_NOT_EMPTY) {
			return StringUtils.isNotEmpty(var1.toString());
		} else if (this == BEFORE_NEAR) {
			List<Comparable> list = new ArrayList<Comparable>(varSet2);
			Collections.sort(list);
			if (var1 instanceof Comparable) {
				return var2.equals(SearchUtils.binarySearch(list,
						(Comparable) var1, false));
			} else {
				throw new CalculationException("对于[" + BEFORE_NEAR
						+ "]比较的两端必须是Comarable接口的实现bean");
			}

		} else if (this == AFTER_NEAR) {
			List<Comparable> list = new ArrayList<Comparable>(varSet2);
			Collections.sort(list);
			if (var2 instanceof Comparable) {
				return var2.equals(SearchUtils.binarySearch(list,
						(Comparable) var1, true));
			} else {
				throw new CalculationException("对于[" + AFTER_NEAR
						+ "]比较的两端必须是Comarable接口的实现bean");
			}
		} else if (this == IN) {
			return varSet2.contains(var1);
		} else if (this == NOT_IN) {
			return varSet2.contains(var1);
		} else {
			throw new RuntimeException();
		}
	}

	public boolean isLeftOuterJoin() {
		if (this == LEFT_OUTER_JOIN || this == BEFORE_NEAR
				|| this == AFTER_NEAR) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSupportCompareCollection() {
		if (this == IN || this == NOT_IN) {
			return true;
		} else {
			return false;
		}
	}

}

// $Id: CompareType.java 16189 2010-07-15 08:35:52Z chulin.gui $