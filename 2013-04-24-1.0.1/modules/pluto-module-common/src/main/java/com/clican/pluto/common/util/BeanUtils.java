/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.util;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanUtils {

	private final static Log log = LogFactory.getLog(BeanUtils.class);
	
	public static Object getProperty(Object obj, String propertyName)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		Method method = null;
		try {
			method = obj.getClass().getMethod(
					StringUtils.getGetMethodName(propertyName), new Class[] {});
		} catch (NoSuchMethodException e) {
			method = obj.getClass().getMethod(
					StringUtils.getIsMethodName(propertyName), new Class[] {});
		}
		return method.invoke(obj, new Object[] {});
	}

	@SuppressWarnings("unchecked")
	public static Collection<Serializable> getCollectionProperty(Object obj,
			String propertyName) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		return (Collection<Serializable>) getProperty(obj, propertyName);
	}

	public static Calendar getCalendarProperty(Object obj, String propertyName)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		return (Calendar) getProperty(obj, propertyName);
	}

	public static void setProperty(Object obj, String propertyName,
			Object propertyValue) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		Method method = obj.getClass().getMethod(
				StringUtils.getSetMethodName(propertyName),
				new Class[] { propertyValue.getClass() });
		method.invoke(obj, new Object[] { propertyValue });
	}

	public static void setCollectionProperty(Object obj, String propertyName,
			Collection<?> coll) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		Method method = obj.getClass().getMethod(
				StringUtils.getSetMethodName(propertyName),
				new Class[] { Collection.class });
		method.invoke(obj, new Object[] { coll });
	}

	 /**
     * 把一个对象的list 转换成为一个 Map<String,Object>的list。其中map的key是属性名,value是属性值
     * <p>
     * 例如：User对象有两个属性id和name, beanList有10个User对象，那么调用此方法返回的结果为List<Map>， 长度为10,且每个Map对象包含两个key：id,name, value为对应属性的值.
     * </p>
     * 
     * @param <v>
     * @param beanList 需要进行转换的对象列表
     * @return 返回List对象
     */
    public static <V> List<Map<String, Object>> convertBeanToMapList(List<V> beanList) {
        if (beanList == null || beanList.size() < 1) {
            return null;
        }
        // 保存结果
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (V bean : beanList) {
            result.add(convertBeanToMap(bean));
        }
        return result;

    }

    /**
     * 把一个对象转换成为一个Map，其中map的key是属性名,value是属性值
     * <p>
     * 例如： 一个bean的属性有 id 和 name两个属性，对应属性值分别是 100 和 zhangsan 最终转换的结果就是 id=100,name="zhangsan"的Map
     * </p>
     * 
     * @param <Bean>
     * @param bean
     * @return
     */
    public static <Bean> Map<String, Object> convertBeanToMap(Bean bean) {
        if (bean == null)
            return null;

        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] psArray = propertyUtilsBean.getPropertyDescriptors(bean.getClass());
        List<String> properties = new ArrayList<String>();
        for (PropertyDescriptor ps : psArray) {
            properties.add(ps.getName());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (String property : properties) {
            // 跳过本次循环，不对class属性做map
            if ("class".equals(property)) {
                continue;
            }
            String methodName = StringUtils.getGetMethodName(property);
            try {
                Method method = bean.getClass().getMethod(methodName, new Class<?>[] {});
                Object propertyValue = method.invoke(bean, new Object[] {});
                if (propertyValue != null) {
                    map.put(property, propertyValue);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return map;
    }

    /**
     * 把一个对象的List按某个属性转换成属性的List。
     * <p>
     * 例如： 把User对象的name属性值组成List。 convertToList(List<User> values, "name") 返回List<String>
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　对象列表
     * @param propertyName 　　　　属性
     * @return 　返回由propertyName的值组成的List
     */
    @SuppressWarnings("unchecked")
    public static <K, V> List<K> convertToList(List<V> values, String propertyName) {
        List<K> result = new ArrayList<K>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
            String methodName = StringUtils.getGetMethodName(propertyName);
            try {
                Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                K k = (K) method.invoke(v, new Object[] {});
                if (k != null) {
                    result.add(k);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 把一个对象的List按某个属性转换成属性的Set。
     * <p>
     * 例如： 把User对象的name属性值组成Set。 convertToSet(List<User> values, "name") 返回Set<String>
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　对象列表
     * @param propertyName 　　　　属性
     * @return 　返回由propertyName的值组成的Set
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Set<K> convertToSet(List<V> values, String propertyName) {
        Set<K> result = new HashSet<K>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
            String methodName = StringUtils.getGetMethodName(propertyName);
            try {
                Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                K k = (K) method.invoke(v, new Object[] {});
                if (k != null) {
                    result.add(k);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 把一个对象List按某个属性转换成Map, key为属性值，value为对象。
     * <p>
     * 例如： convertToMap(List<User> values, "name")返回由name属性的值为Key,User对象为值的Map
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName 　　　　属性
     * @return 　　　　返回由propertyName值作为key组成的Map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> convertToMap(List<V> values, String propertyName) {
        Map<K, V> result = new HashMap<K, V>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
       	 if(propertyName.indexOf(".")>0){
            	String[] propertyNameList = propertyName.split("\\.");
            	try {
                    K k = (K) getPropertyByMthodName(v,propertyNameList);
                    if (k != null) {
                        if (!result.containsKey(k)) {
                            result.put(k, v);
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
       	 }else{
       		  String methodName = StringUtils.getGetMethodName(propertyName);
                 try {
                     Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                     K k = (K) method.invoke(v, new Object[] {});
                     if (k != null) {
                         result.put(k, v);
                     }
                 } catch (Exception e) {
                     log.error("", e);
                 }
       	 }
       }
        return result;
    }
    
    /**
     * 把一个对象List按某个属性转换成Map, key为属性值，value为由相应属性值对应的对象组成的List。
     * <p>
     * 例如： 　调用convertToMapAndList（List<User> value, "name"）返回结果为由User对象的name属性值作为key的Map, 如果name属性 　相同则把它们组成List.
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName 　　　　属性
     * @return 　　　返回由propertyName值作为key的Map,相同key的对象组成List.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> convertToMapAndList2(List<V> values, String propertyName) {
        Map<K, List<V>> result = new HashMap<K, List<V>>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
        	String methodName = null;
            if(propertyName.indexOf(".")>0){
            	String[] propertyNameList = propertyName.split("\\.");
            	
            	try {
                    K k = (K) getPropertyByMthodName(v,propertyNameList);
                    if (k != null) {
                        if (!result.containsKey(k)) {
                            result.put(k, new ArrayList<V>());
                        }
                        result.get(k).add(v);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }else {
            	methodName = StringUtils.getGetMethodName(propertyName);
            	try {
                    Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                    K k = (K) method.invoke(v, new Object[] {});
                    if (k != null) {
                        if (!result.containsKey(k)) {
                            result.put(k, new ArrayList<V>());
                        }
                        result.get(k).add(v);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            
        }
        return result;
    }
    
    /**
     * 把一个对象List按某个属性转换成Map, key为属性值，value为由相应属性值对应的对象组成的List。
     * <p>
     * 例如： 　调用convertToMapAndList（List<User> value, "name"）返回结果为由User对象的name属性值作为key的Map, 如果name属性 　相同则把它们组成List.
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName 　　　　属性
     * @return 　　　返回由propertyName值作为key的Map,相同key的对象组成List.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> convertToMapAndList2(List<V> values, String propertyName,Boolean sorted) {
    	  Map<K, List<V>> result = sorted ? new TreeMap<K, List<V>>() : new HashMap<K, List<V>>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
        	String methodName = null;
            if(propertyName.indexOf(".")>0){
            	String[] propertyNameList = propertyName.split("\\.");
            	
            	try {
                    K k = (K) getPropertyByMthodName(v,propertyNameList);
                    if (k != null) {
                        if (!result.containsKey(k)) {
                            result.put(k, new ArrayList<V>());
                        }
                        result.get(k).add(v);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }else {
            	methodName = StringUtils.getGetMethodName(propertyName);
            	try {
                    Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                    K k = (K) method.invoke(v, new Object[] {});
                    if (k != null) {
                        if (!result.containsKey(k)) {
                            result.put(k, new ArrayList<V>());
                        }
                        result.get(k).add(v);
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
	public static <K> Object getPropertyByMthodName(Object obj,String[] propertyNameList){
    	if(null==propertyNameList || propertyNameList.length<1){
    		return (K)obj;
    	}
    	try {
    		K result = null;
	    	String propertyName = propertyNameList[0];
	    	Method method = obj.getClass().getMethod(StringUtils.getGetMethodName(propertyName),  new Class<?>[] {});
	    	if(propertyNameList.length>1){	    		
				Object newObj = method.invoke(obj, new Object[]{});
				String[] newPropertyNameList = new String[propertyNameList.length-1];
				System.arraycopy(propertyNameList, 1, newPropertyNameList, 0, propertyNameList.length-1);
				return getPropertyByMthodName(newObj,newPropertyNameList);
	    	}else{
	    		result = (K)method.invoke(obj, new Object[]{});
	    	}
	    	
	    	return result;
    	
    	} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * 把一个对象List按某个属性转换成Map, key为属性值，value为由相应属性值对应的对象组成的List。
     * <p>
     * 例如： 　调用convertToMapAndList（List<User> value, "name"）返回结果为由User对象的name属性值作为key的Map, 如果name属性 　相同则把它们组成List.
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName 　　　　属性
     * @return 　　　返回由propertyName值作为key的Map,相同key的对象组成List.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> convertToMapAndList(List<V> values, String propertyName) {
        Map<K, List<V>> result = new HashMap<K, List<V>>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
            String methodName = StringUtils.getGetMethodName(propertyName);
            try {
                Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                K k = (K) method.invoke(v, new Object[] {});
                if (k != null) {
                    if (!result.containsKey(k)) {
                        result.put(k, new ArrayList<V>());
                    }
                    result.get(k).add(v);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 把一个对象List按某个属性转换成Map, key为属性值，value为由相应属性值对应的对象组成的List,并且可以按key进行排序。
     * <p>
     * 例如： 　调用convertToMapAndList（List<User> value, "name"）返回结果为由User对象的name属性值作为key的Map, 如果name属性 　相同则把它们组成List.
     * </p>
     * 
     * @param <K>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName 　　　　属性
     * @param sorted 　　　　是否排序
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> convertToMapAndList(List<V> values, String propertyName, boolean sorted) {
        Map<K, List<V>> result = sorted ? new TreeMap<K, List<V>>() : new HashMap<K, List<V>>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
            String methodName = StringUtils.getGetMethodName(propertyName);
            try {
                Method method = v.getClass().getMethod(methodName, new Class<?>[] {});
                K k = (K) method.invoke(v, new Object[] {});
                if (k != null) {
                    if (!result.containsKey(k)) {
                        result.put(k, new ArrayList<V>());
                    }
                    result.get(k).add(v);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 把一个对象List按两个属性转换成Map,key为propertyName1, 值为由propertyName2作为key的Map。
     * <p>
     * 例如： 调用convertToMutilMap（List<User> values, "id", "name"）返回由id作为key的Map, 值为由name作为key的Map.
     * </p>
     * 
     * @param <K1>
     * @param <K2>
     * @param <V>
     * @param values 　　　　对象列表
     * @param propertyName1 属性１
     * @param propertyName2 　　　　属性２
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K1, K2, V> Map<K1, Map<K2, V>> convertToMutilMap(List<V> values, String propertyName1,
            String propertyName2) {
        Map<K1, Map<K2, V>> result = new HashMap<K1, Map<K2, V>>();
        if (values == null) {
            return result;
        }
        for (V v : values) {
            String methodName1 = StringUtils.getGetMethodName(propertyName1);
            String methodName2 = StringUtils.getGetMethodName(propertyName2);
            try {
                Method method1 = v.getClass().getMethod(methodName1, new Class<?>[] {});
                K1 k1 = (K1) method1.invoke(v, new Object[] {});
                if (k1 != null) {
                    if (!result.containsKey(k1)) {
                        result.put(k1, new HashMap<K2, V>());
                    }
                    Method method2 = v.getClass().getMethod(methodName2, new Class<?>[] {});
                    K2 k2 = (K2) method2.invoke(v, new Object[] {});
                    if (k2 != null) {
                        if (!result.get(k1).containsKey(k2)) {
                            result.get(k1).put(k2, v);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }
    
    /**
	 * 把一个对象List按两个属性转换成Map,key为propertyName1, 值为由propertyName2作为key的Map。
	 * <p>
	 * 例如： 调用convertToMutilMap（List<User> values, "id", "name"）返回由id作为key的Map,
	 * 值为由name作为key的Map.
	 * </p>
	 * 
	 * @param <K1>
	 * @param <K2>
	 * @param <V>
	 * @param values
	 *            　　　　对象列表
	 * @param propertyName1
	 *            属性１
	 * @param propertyName2
	 *            　　　　属性２
	 * @param sorted           
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K1, K2, V> Map<K1, Map<K2, V>> convertToMutilMap(List<V> values, String propertyName1,
			String propertyName2, boolean sorted) {

		Map<K1, Map<K2, V>> result = sorted ? new TreeMap<K1, Map<K2, V>>() : new HashMap<K1, Map<K2, V>>();
		if (values == null) {
			return result;
		}
		for (V v : values) {
			String methodName1 = StringUtils.getGetMethodName(propertyName1);
			String methodName2 = StringUtils.getGetMethodName(propertyName2);
			try {
				Method method1 = v.getClass().getMethod(methodName1, new Class<?>[] {});
				K1 k1 = (K1) method1.invoke(v, new Object[] {});
				if (k1 != null) {
					if (!result.containsKey(k1)) {
						result.put(k1, new HashMap<K2, V>());
					}
					Method method2 = v.getClass().getMethod(methodName2, new Class<?>[] {});
					K2 k2 = (K2) method2.invoke(v, new Object[] {});
					if (k2 != null) {
						if (!result.get(k1).containsKey(k2)) {
							result.get(k1).put(k2, v);
						}
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return result;
	}
}

// $Id$