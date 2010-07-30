/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.dpl.parser.bean.DplResultSet;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.CompareType;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 比较2个对象通过各类<code>CompareType</code>来操作比较结果
 * 
 * @author wei.zhang
 * 
 */
public class CompareFilter extends Filter {

	public final static Log log = LogFactory.getLog(CompareFilter.class);

	public final static String DPL_RESULT_SET = "DPL_RESULT_SET";
	/**
	 * 左标志位置常量
	 */
	private final static String LEFT_POS = "leftPos";

	/**
	 * 右标志位常量
	 */
	private final static String RIGHT_POS = "rightPos";

	/**
	 * 左面表达式描述对象
	 */
	private PrefixAndSuffix leftPas;

	/**
	 * 右面表达式描述对象
	 */
	private PrefixAndSuffix rightPas;

	/**
	 * 
	 * @param leftPas
	 *            左面表达式描述对象
	 * @param rightPas
	 *            右面表达式描述对象
	 * @param leftVarName
	 *            如果左面表达式使用了From中的关键字则该值就是该关键字
	 * @param rightVarName
	 *            如果右面表达式使用了From中的关键字则该值就是该关键字
	 * @param compareType
	 *            比较类型对象
	 * @param expr
	 *            表达式
	 */
	public CompareFilter(PrefixAndSuffix leftPas, PrefixAndSuffix rightPas, CompareType compareType) throws PrefixAndSuffixException {
		this.leftPas = leftPas;
		this.rightPas = rightPas;
		List<String> fromParams = leftPas.getFromParams();
		if (fromParams.size() == 1) {
			this.leftVarName = fromParams.get(0);
		} else if (fromParams.size() > 1) {
			throw new PrefixAndSuffixException("在where条件中的函数中使用到的参数当且仅当包含一个from条件才可以");
		}
		fromParams = rightPas.getFromParams();
		if (fromParams.size() == 1) {
			this.rightVarName = fromParams.get(0);
		} else if (fromParams.size() > 1) {
			throw new PrefixAndSuffixException("在where条件中的函数中使用到的参数当且仅当包含一个from条件才可以");
		}
		this.compareType = compareType;
		this.expr = leftPas.toString() + compareType.getOperation() + rightPas.toString();
	}

	/**
	 * 操作符左面的操作组数在From中的名称，如果没有则为空
	 */
	private String leftVarName;

	/**
	 * 操作符右面的操作组数在From中的名称，如果没有则为空
	 */
	private String rightVarName;

	/**
	 * 整个操作描述
	 */
	private String expr;

	/**
	 * 操作类型
	 */
	private CompareType compareType;

	public String getExpr() {
		return expr;
	}

	/**
	 * 存在DplResultSet并且left和right都是from关键字并且这2个from关键字都存在于DplResultSet中
	 * 
	 * @param context
	 */
	private void existDplResultAndExist2FromAndContain2From(ProcessorContext context) throws DplParseException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		List<Map<String, Object>> resultSet = original.getResultSet();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 用来统计非重复的不存在左外链接关系的行记录
		Map<Object, Map<String, Object>> leftCompCount = new HashMap<Object, Map<String, Object>>();
		// 由于左右2端的变量都存在于该结果结中，只需要遍历已经存在的结果集合
		for (Map<String, Object> rs : resultSet) {
			Comparable<?> leftComp = leftPas.getValue(rs);
			Comparable<?> rightComp = rightPas.getValue(rs);

			// 对于外链的情况下任意一个为空则不需要再进行比较了直接算比较通过
			if (compareType.isLeftOuterJoin() && (leftComp == null || rightComp == null)) {
				result.add(rs);
				continue;
			}
			// 对于外链接记录下对应的对象的记录行
			if (compareType.isLeftOuterJoin() && !leftCompCount.containsKey(rs.get(leftVarName))) {
				leftCompCount.put(rs.get(leftVarName), rs);
			}
			if (compareType.compare(leftComp, rightComp, null, null)) {
				if (compareType.isLeftOuterJoin()) {
					// 对于外链接如果匹配到某个记录行则设置为空
					leftCompCount.put(rs.get(leftVarName), null);
				}
				result.add(rs);
			}
		}
		if (compareType.isLeftOuterJoin()) {
			// 对于外链接那些没有匹配到记录行的记录则把对应的rightVar设置为空然后加入到结果集中
			for (Object leftComp : leftCompCount.keySet()) {
				Map<String, Object> rs = leftCompCount.get(leftComp);
				if (rs != null) {
					rs.put(rightVarName, null);
					result.add(rs);
				}
			}
		}
		// 覆盖原来的结果集
		original.setResultSet(result);
		context.setAttribute(DPL_RESULT_SET, original);
	}

	/**
	 * 比较左右2个List的交集
	 * 
	 * @param context
	 *            <code>ProcessorContext</code>对象
	 * @param leftVarList
	 *            左List
	 * @param rightVarList
	 *            右List
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> compareLeftAndRight(ProcessorContext context, List<?> leftVarList, List<?> rightVarList) throws DplParseException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		// 如果变量名已经存在于现有的结果集合中则flag=true
		boolean leftFlag = original != null && original.getResultNames().contains(leftVarName);
		boolean rightFlag = original != null && original.getResultNames().contains(rightVarName);

		// 用来存放同样的key的list列表
		Map<Comparable<?>, List<Object>> leftMap = new HashMap<Comparable<?>, List<Object>>();
		Map<Comparable<?>, List<Object>> rightMap = new HashMap<Comparable<?>, List<Object>>();

		// 列表的数序记录的Map
		final Map<Object, Integer> leftOrderMap = new HashMap<Object, Integer>();
		final Map<Object, Integer> rightOrderMap = new HashMap<Object, Integer>();

		// 把左面List中的数据根据Key值转换为一个Map
		for (int i = 0; i < leftVarList.size(); i++) {
			Object leftVarObj = leftVarList.get(i);
			leftOrderMap.put(leftVarObj, i);
			Map<String, Object> row = new HashMap<String, Object>();
			if (leftFlag) {
				row.putAll((Map) leftVarObj);
			} else {
				row.put(leftVarName, leftVarObj);
			}
			Comparable<?> key = this.leftPas.getValue(row);
			if (!leftMap.containsKey(key)) {
				leftMap.put(key, new ArrayList<Object>());
			}
			leftMap.get(key).add(leftVarObj);
		}
		// 把右面List中的数据根据Key值转换为一个Map
		for (int i = 0; i < rightVarList.size(); i++) {
			Object rightVarObj = rightVarList.get(i);
			rightOrderMap.put(rightVarObj, i);
			Map<String, Object> row = new HashMap<String, Object>();
			if (rightFlag) {
				row.putAll((Map) rightVarObj);
			} else {
				row.put(rightVarName, rightVarObj);
			}
			Comparable<?> key = this.rightPas.getValue(row);
			if (!rightMap.containsKey(key)) {
				rightMap.put(key, new ArrayList<Object>());
			}
			rightMap.get(key).add(rightVarObj);
		}

		// 求交集
		List<Map<String, Object>> resultSet = getResultSet(context, leftMap, rightMap, leftVarName, rightVarName);
		// 把结果集根据与原有的顺序还原
		Collections.sort(resultSet, new Comparator<Map<String, Object>>() {

			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Object left1 = o1.get(LEFT_POS);
				Object left2 = o2.get(LEFT_POS);

				Object right1 = o1.get(RIGHT_POS);
				Object right2 = o2.get(RIGHT_POS);

				Integer left1Index = leftOrderMap.get(left1);
				Integer left2Index = leftOrderMap.get(left2);

				Integer right1Index = rightOrderMap.get(right1);
				Integer right2Index = rightOrderMap.get(right2);
				if (left1Index != left2Index) {
					if (left1Index == null && left2Index == null) {
						return 0;
					} else if (left1Index == null) {
						return -1;
					} else if (left2Index == null) {
						return 1;
					} else {
						return left1Index - left2Index;
					}
				} else {
					if (right1Index == null && right2Index == null) {
						return 0;
					} else if (right1Index == null) {
						return -1;
					} else if (right2Index == null) {
						return 1;
					} else {
						return right1Index - right2Index;
					}
				}
			}
		});
		// 移除不需要要的记顺序属性
		for (Map<String, Object> row : resultSet) {
			row.remove(LEFT_POS);
			row.remove(RIGHT_POS);
		}
		return resultSet;
	}

	/**
	 * 根据左或右表达式过滤数据
	 * 
	 * @param context
	 *            <code>ProcessorContext</code>对象
	 * @param varName
	 *            在From关键字的名称
	 * @param pas1
	 *            表达式1
	 * @param pas2
	 *            表达式2
	 * @param varList
	 *            数据集合
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void compareLeftOrRight(ProcessorContext context, String varName, PrefixAndSuffix pas1, PrefixAndSuffix pas2, List<?> varList)
			throws DplParseException {
		// 行记录集合
		List<Map<String, Object>> newVarList = new ArrayList<Map<String, Object>>();
		// 单个对象记录集合
		List<Object> newSimpleVarList = new ArrayList<Object>();
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		// 如果varName存在于已有的结果集合中则flag=true
		boolean flag = original != null && original.getResultNames().contains(varName);
		// 逐条处理数据集合
		for (Object varObj : varList) {
			// 统计把记录集合用Map来包装
			Map<String, Object> row = new HashMap<String, Object>();
			if (flag) {
				row.putAll((Map) varObj);
			} else {
				row.put(varName, varObj);
			}
			// 获取比较对象1和比较对象2
			Object comp1 = pas1.getValue(row);
			// 比较对象2可以是Collection所以使用Object
			Object comp2 = pas2.getValue(row);

			if (comp2 instanceof Comparable || comp2 == null) {
				// 2个Comparable对象的比较
				if (compareType.compare(comp1, comp2, null, null)) {
					if (flag) {
						newVarList.add(row);
					}
					newSimpleVarList.add(row.get(varName));
				}
			} else if (comp2 instanceof Collection) {
				// 如果比较方法支持对象和集合的比较的话则进行比较
				if (compareType.isSupportCompareCollection()) {
					if (compareType.compare(comp1, null, null, new HashSet((Collection) comp2))) {
						if (flag) {
							newVarList.add(row);
						}
						newSimpleVarList.add(row.get(varName));
					}
				} else {
					throw new DplParseException("该" + this.expr + "[" + compareType.name() + "]操作不支持对象和Collection的比较");
				}
			} else {
				throw new DplParseException("该" + this.expr + "操作左右两端的比较对象类型无法比较");
			}
		}
		if (flag) {
			// 如果结果集合已经存在则覆盖原来的结果集合
			original.setResultSet(newVarList);
		}
		// 如果结果集合不存在则只是覆盖基础的数据集合
		context.setAttribute(varName, newSimpleVarList);
	}

	/**
	 * 存在DplResultSet并且left和right都是from关键字并且这2个from关键字有一个存在于DplResultSet中
	 * 
	 * @param context
	 */
	private void existDplResultAndExist2FromAndContain1From(ProcessorContext context) throws DplParseException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		List<?> leftVarList = null;
		List<?> rightVarList = null;
		if (original.getResultNames().contains(this.leftVarName)) {
			leftVarList = original.getResultSet();
		} else {
			leftVarList = context.getAttribute(leftVarName);
		}
		if (original.getResultNames().contains(this.rightVarName)) {
			rightVarList = original.getResultSet();
		} else {
			rightVarList = context.getAttribute(rightVarName);
		}
		List<Map<String, Object>> resultSet = this.compareLeftAndRight(context, leftVarList, rightVarList);
		original.getResultNames().add(leftVarName);
		original.getResultNames().add(rightVarName);
		original.setResultSet(resultSet);
	}

	/**
	 * 存在DplResultSet并且left和right都是from关键字并且这2个from关键字有都不存在于DplResultSet中
	 * 
	 * @param context
	 */
	private void existDplResultAndExist2FromAndContain0From(ProcessorContext context) throws DplParseException {
		List<?> leftVarList = context.getAttribute(leftVarName);
		List<?> rightVarList = context.getAttribute(rightVarName);
		List<Map<String, Object>> resultSet = this.compareLeftAndRight(context, leftVarList, rightVarList);
		DplResultSet dplResultSet = new DplResultSet();
		dplResultSet.getResultNames().add(leftVarName);
		dplResultSet.getResultNames().add(rightVarName);
		dplResultSet.setResultSet(resultSet);
		context.setAttribute(DPL_RESULT_SET, dplResultSet);
	}

	/**
	 * 存在DplResultSet并且left和right中一个是From关键字并且这个关键字存在于DplResultSet中
	 * 
	 * @param context
	 */
	private void existDplResultAndExist1FromAndContain1From(ProcessorContext context) throws DplParseException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		if (StringUtils.isNotEmpty(leftVarName)) {
			List<?> leftVarList = original.getResultSet();
			this.compareLeftOrRight(context, leftVarName, leftPas, rightPas, leftVarList);
		} else if (StringUtils.isNotEmpty(rightVarName)) {
			List<?> rightVarList = original.getResultSet();
			this.compareLeftOrRight(context, rightVarName, rightPas, leftPas, rightVarList);
		} else {
			throw new DplParseException("表达式不正确，可能缺少list前缀");
		}

	}

	/**
	 * 存在DplResultSet并且left和right中一个是From关键字并且这个关键字不存在于DplResultSet中
	 * 
	 * @param context
	 */
	private void existDplResultAndExist1FromAndContain0From(ProcessorContext context) throws DplParseException {
		if (StringUtils.isNotEmpty(leftVarName)) {
			List<?> leftVarList = context.getAttribute(leftVarName);
			this.compareLeftOrRight(context, leftVarName, leftPas, rightPas, leftVarList);
		} else if (StringUtils.isNotEmpty(rightVarName)) {
			List<?> rightVarList = context.getAttribute(rightVarName);
			this.compareLeftOrRight(context, rightVarName, rightPas, leftPas, rightVarList);
		} else {
			throw new DplParseException("表达式不正确，可能缺少list前缀");
		}
	}

	/**
	 * 不存在DplResultSet并且left和right都是from关键字
	 * 
	 * @param context
	 */
	private void notExistDplResultAndExist2From(ProcessorContext context) throws DplParseException {
		List<?> leftVarList = context.getAttribute(leftVarName);
		List<?> rightVarList = context.getAttribute(rightVarName);
		List<Map<String, Object>> resultSet = this.compareLeftAndRight(context, leftVarList, rightVarList);
		DplResultSet dplResultSet = new DplResultSet();
		dplResultSet.getResultNames().add(leftVarName);
		dplResultSet.getResultNames().add(rightVarName);
		dplResultSet.setResultSet(resultSet);
		context.setAttribute(DPL_RESULT_SET, dplResultSet);
	}

	/**
	 * 不存在DplResultSet并且left和right中有一个是From关键字
	 * 
	 * @param context
	 */
	private void notExistDplResultAndExist1From(ProcessorContext context) throws DplParseException {
		if (StringUtils.isNotEmpty(leftVarName)) {
			List<?> leftVarList = context.getAttribute(leftVarName);
			this.compareLeftOrRight(context, leftVarName, leftPas, rightPas, leftVarList);
		} else if (StringUtils.isNotEmpty(rightVarName)) {
			List<?> rightVarList = context.getAttribute(rightVarName);
			this.compareLeftOrRight(context, rightVarName, rightPas, leftPas, rightVarList);
		} else {
			throw new DplParseException("表达式不正确，可能缺少list前缀");
		}
	}

	/**
	 * 进行比较操作
	 */
	public void filter(ProcessorContext context) throws DplParseException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		if (original != null) {
			if (StringUtils.isNotEmpty(leftVarName) && StringUtils.isNotEmpty(rightVarName)) {
				if (original.getResultNames().contains(leftVarName) && original.getResultNames().contains(rightVarName)) {
					// EF2D2
					existDplResultAndExist2FromAndContain2From(context);
				} else if (original.getResultNames().contains(leftVarName)) {
					// EF2D1
					existDplResultAndExist2FromAndContain1From(context);
				} else if (original.getResultNames().contains(rightVarName)) {
					// EF2D1
					existDplResultAndExist2FromAndContain1From(context);
				} else {
					// EF2D0
					existDplResultAndExist2FromAndContain0From(context);
				}
			} else if (StringUtils.isNotEmpty(leftVarName)) {
				if (original.getResultNames().contains(leftVarName)) {
					// EF1D1
					existDplResultAndExist1FromAndContain1From(context);
				} else {
					// EF1D0
					existDplResultAndExist1FromAndContain0From(context);
				}
			} else if (StringUtils.isNotEmpty(rightVarName)) {
				if (original.getResultNames().contains(rightVarName)) {
					// EF1D1
					existDplResultAndExist1FromAndContain1From(context);
				} else {
					// EF1D0
					existDplResultAndExist1FromAndContain0From(context);
				}
			} else {
				throw new DplParseException("比较的两端不能都是常量这样的比较没有意义[" + this.getExpr() + "]");
			}
		} else {
			if (StringUtils.isNotEmpty(leftVarName) && StringUtils.isNotEmpty(rightVarName)) {
				// !EF2D0
				notExistDplResultAndExist2From(context);
			} else if (StringUtils.isNotEmpty(leftVarName)) {
				// !EF1D0
				notExistDplResultAndExist1From(context);
			} else if (StringUtils.isNotEmpty(rightVarName)) {
				// !EF1D0
				notExistDplResultAndExist1From(context);
			} else {
				throw new DplParseException("比较的两端不能都是常量这样的比较没有意义[" + this.getExpr() + "]");
			}
		}
	}

	/**
	 * 返回Join后的结果集合
	 * 
	 * @param context
	 *            <code>ProcessorContext</code>
	 * @param leftMap
	 *            左面的结果集合
	 * @param rightMap
	 *            右面的结果集合
	 * @param leftVarName
	 *            左面的变量名
	 * @param rightVarName
	 *            右面的变量名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getResultSet(ProcessorContext context, Map<Comparable<?>, List<Object>> leftMap,
			Map<Comparable<?>, List<Object>> rightMap, String leftVarName, String rightVarName) throws CalculationException {
		DplResultSet original = context.getAttribute(DPL_RESULT_SET);
		List<Map<String, Object>> resultSet = new ArrayList<Map<String, Object>>();
		for (Comparable<?> leftKey : leftMap.keySet()) {
			List<Object> leftVarObjList = leftMap.get(leftKey);
			List<Object> rightVarObjList = new ArrayList<Object>();
			if (compareType.isLeftOuterJoin()) {
				for (Comparable<?> rightKey : rightMap.keySet()) {
					if (compareType.compare(leftKey, rightKey, leftMap.keySet(), rightMap.keySet())) {
						rightVarObjList.addAll(rightMap.get(rightKey));
					}
				}
			} else {
				for (Comparable<?> rightKey : rightMap.keySet()) {
					if (compareType.compare(leftKey, rightKey, leftMap.keySet(), rightMap.keySet())) {
						rightVarObjList.addAll(rightMap.get(rightKey));
					}
				}
			}
			if (leftVarObjList != null && rightVarObjList != null) {
				for (Object leftVarObj : leftVarObjList) {
					if (rightVarObjList.size() == 0 && compareType.isLeftOuterJoin()) {
						Map<String, Object> result = new HashMap<String, Object>();
						if (original != null && original.getResultNames().contains(leftVarName)) {
							result.putAll((Map<String, Object>) leftVarObj);
						} else {
							result.put(leftVarName, leftVarObj);
						}
						result.put(rightVarName, null);
						result.put(LEFT_POS, leftVarObj);
						result.put(RIGHT_POS, null);
						resultSet.add(result);
					} else {
						for (Object rightVarObj : rightVarObjList) {
							Map<String, Object> result = new HashMap<String, Object>();
							if (original != null && original.getResultNames().contains(leftVarName)) {
								result.putAll((Map<String, Object>) leftVarObj);
							} else {
								result.put(leftVarName, leftVarObj);
							}
							if (original != null && original.getResultNames().contains(rightVarName)) {
								result.putAll((Map<String, Object>) rightVarObj);
							} else {
								result.put(rightVarName, rightVarObj);
							}
							result.put(LEFT_POS, leftVarObj);
							result.put(RIGHT_POS, rightVarObj);
							resultSet.add(result);
						}
					}
				}
			}
		}
		return resultSet;
	}

	public int priority() {
		// 左外链接最先执行然后是普通字段过滤最后才是内链接
		if (StringUtils.isNotEmpty(leftVarName) && StringUtils.isNotEmpty(rightVarName)) {
			if (compareType.isLeftOuterJoin()) {
				return 3;
			} else {
				return 1;
			}
		} else {
			return 2;
		}
	}

}

// $Id: CompareFilter.java 16069 2010-07-13 09:03:31Z wei.zhang $