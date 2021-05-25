package com.zw.admin.server.utils;

import org.apache.commons.beanutils.Converter;


import java.text.SimpleDateFormat;

/**
 * @author larry
 * @Description:日期转换器
 * @date 2020/5/13 11:15
 */
public class CustomerDateConverter implements Converter {
    private final static SimpleDateFormat DATE_FORMATE_SHOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//根据传来的时间字符串格式：例如：20130224201210
    public Object convert(Class type, Object value){
        if (type.equals(java.util.Date.class) ) {
            try {
                return DATE_FORMATE_SHOW.parse(value.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
