/*
 * Copyright @2012 365.com All rights reserved.
 */

package net.dbaeye.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 
 *计算两个节点相似度的工具包
 * @author jjr(Jul 27, 2012)
 * @version V1.0 
 */

public class SimilarityUtil {
	
	@SuppressWarnings("unchecked")
	public static Map sortByValue(Map map, final boolean reverse) {
        List list = new LinkedList(map.entrySet());
        Collections .sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (reverse) {
                    return -((Comparable) ((Map .Entry)o1).getValue())
                            .compareTo(((Map .Entry)o2).getValue());
                }
                return ((Comparable) ((Map .Entry)o1).getValue())
                        .compareTo(((Map .Entry)o2).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
}

	public static Map<String, Map<String, Integer>> mapCopy(
			Map<String, Map<String, Integer>> old, String exceptKey) {
		Map<String, Map<String, Integer>> m = new HashMap<String, Map<String, Integer>>();
		for (String key : old.keySet()) {
			if (exceptKey.equals(key)) {
				continue;
			}
			Map<String, Integer> newValueMap = new HashMap<String, Integer>();
			newValueMap.putAll(old.get(key));
			m.put(key, newValueMap);
		}
		return m;
	}

	public static double similarity(Map<String, Integer> dict1,
			Map<String, Integer> dict2) {
		double similarity = 0.0, numerator = 0.0, denominator1 = 0.0, denominator2 = 0.0;
		if (dict1.size() == 0 || dict2.size() == 0) {
			similarity = 0.0;
			return similarity;
		}
		int value1 = 0;
		int value2 = 0;
		int num = 0;
		for (String keyword : dict1.keySet()) {
			value1 = dict1.get(keyword);
			if (dict2.containsKey(keyword)) {
				value2 = dict2.get(keyword);
				dict2.remove(keyword);
				num++;
			} else {
				value2 = 0;
			}
			numerator += value1 * value2;
			denominator1 += value1 * value1;
			denominator2 += value2 * value2;
		}

		for (String keyword : dict2.keySet()) {
			value2 = dict2.get(keyword);
			denominator2 += value2 * value2;
		}
		similarity = numerator / (Math.sqrt(denominator1 * denominator2));
		return similarity;
	}

}
