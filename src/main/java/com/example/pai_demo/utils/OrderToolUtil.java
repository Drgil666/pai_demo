package com.example.pai_demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.pai_demo.exception.ErrorCode;
import com.example.pai_demo.exception.ErrorException;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.pai_demo.utils.errorDict.SORTER_ERROR;

/**
 * @author Gilbert
 * @date 2020/8/16 18:19
 */
@Slf4j
public class OrderToolUtil {
    private static TypeReference<Map<String, String>> SORTER_TYPE = new TypeReference<Map<String, String>>() {
    };

    /**
     * 把字符串转为Sorter的Map，以逗号分隔
     *
     * @param s 要传进来的字符串
     * @return sorter的Map
     */
    public static Map<String, String> stringToSorter(String s) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] sorterSet = s.split(";");
        for (String sorter : sorterSet) {
            String[] entry = sorter.split(":");
            AssertionUtil.isTrue(entry.length == 2, ErrorCode.BIZ_PARAM_ILLEGAL, SORTER_ERROR);
            String key = entry[0];
            String value = entry[1];
            map.put(key, value);
        }
        return map;
    }

    /**
     * 把sorter改成PageHelper用order
     *
     * @param s 要转化的字符串
     * @return 转化好的字符串
     */
    public static String toOrderString(String s) {
        Map<String, String> sorter;
        try {
            sorter = JSONObject.parseObject(s, SORTER_TYPE);
        } catch (JSONException e) {
            throw new ErrorException(ErrorCode.BIZ_PARAM_ILLEGAL, SORTER_ERROR);
        }
        StringBuilder orderBy = new StringBuilder();
        for (Map.Entry<String, String> entry : sorter.entrySet()) {
            String key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey());
            if ("ascend".equals(entry.getValue())) {
                orderBy.append(key).append(" asc");
            } else if ("descend".equals(entry.getValue())) {
                orderBy.append(key).append(" desc");
            }
            orderBy.append(",");
        }
        if (sorter.size() != 0) {
            orderBy.deleteCharAt(orderBy.length() - 1);
        }
        if (log.isDebugEnabled()) {
            log.debug(orderBy.toString());
        }
        return orderBy.toString();
    }

    public static String convertSorter(String s) {
        Map<String, String> map = stringToSorter(s);
        return JSON.toJSONString(map);
    }
}
