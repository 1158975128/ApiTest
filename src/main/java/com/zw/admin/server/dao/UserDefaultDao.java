package com.zw.admin.server.dao;

import com.zw.admin.server.model.UserDefault;
import org.apache.ibatis.annotations.Mapper;

/**
 * 功能描述:用户默认数据数据层
 *
 * @author larry
 * @Date 2020/6/18 10:38
 */
@Mapper
public interface UserDefaultDao {

    /**
     * 功能描述:查询用户默认数据
     *
     * @param userDefault
     * @return com.zw.admin.server.model.UserDefault
     * @author larry
     * @Date 2020/6/18 14:11
     */
    UserDefault selectOneUserDefault(UserDefault userDefault);

    /**
     * 功能描述:添加用户默认数据
     *
     * @param userDefault
     * @return int
     * @author larry
     * @Date 2020/6/18 14:12
     */
    int insertUserDefault(UserDefault userDefault);

    /**
     * 功能描述:修改用户默认数据
     *
     * @param userDefault
     * @return int
     * @author larry
     * @Date 2020/6/18 14:12
     */
    int updateUserDefault(UserDefault userDefault);
}
