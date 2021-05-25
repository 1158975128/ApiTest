package com.zw.admin.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.dto.CommonUserDto;
import com.zw.admin.server.dto.UserDto;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author larry
 * @Description:远程调用返回的用户Dto转换成系统内用户Dto
 * @date 2020/5/12 16:04
 */
public class UserConvertUtil {

    /**
     * 功能描述:类型转换，属性拷贝
     *
     * @param target
     * @param orig
     * @return T
     * @author larry
     * @date 2020/5/13
     */
    public static <T> T beansConvert(T target, String orig) {
        if (target == null || orig == null) {
            return null;
        }
        try {
            CustomerDateConverter dateConverter = new CustomerDateConverter ();
            ConvertUtils.register(dateConverter, Date.class);
            JSONObject jsonObject = JSON.parseObject(orig);
            BeanUtils.copyProperties(target, jsonObject);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
