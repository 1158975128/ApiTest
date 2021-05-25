package com.zw.admin.server.dao;

import java.util.List;
import java.util.Map;

import com.zw.admin.server.model.UserTrainEvaluationBo;
import com.zw.admin.server.model.UserTrainLogBo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zw.admin.server.model.UserTrainEvaluation;

/**
 *功能描述:用户评估数据同步
 * @author larry
 * @Date 2020/10/22 19:02
 */
@Mapper
public interface UserTrainEvaluationDao {

    @Select("select * from user_train_evaluation t where t.id = #{id}")
    UserTrainEvaluation getById(Long id);

    @Delete("delete from user_train_evaluation where id = #{id}")
    int delete(Long id);

    int update(UserTrainEvaluation userTrainEvaluation);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("replace into user_train_evaluation(evaluation_id, user_id, machine_id, type, data,update_time) values(#{evaluationId}, #{userId}, #{machineId}, #{type}, #{data},#{updateTime})")
    int save(UserTrainEvaluation userTrainEvaluation);

    int count(@Param("params") Map<String, Object> params);

    List<UserTrainEvaluation> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 功能描述:分页查询患者评估报告
     *
     * @param userTrainLog
     * @return java.util.List<com.zw.admin.server.model.UserTrainEvaluation>
     * @author larry
     * @Date 2021/1/12 14:56
     */
    List<UserTrainEvaluationBo> selectTrainEvaluationPage(UserTrainLogBo userTrainLog);

    /**
     * 功能描述:查询评估报告历史记录
     *
     * @param userTrainEvaluationBo
     * @return java.util.List<com.zw.admin.server.model.UserTrainEvaluation>
     * @author larry
     * @Date 2021/1/12 14:56
     */
    List<UserTrainEvaluationBo> selectTrainEvaluationBoList(UserTrainEvaluationBo userTrainEvaluationBo);
}