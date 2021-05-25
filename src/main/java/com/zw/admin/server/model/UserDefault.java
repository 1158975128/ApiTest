package com.zw.admin.server.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author larry
 * @Description:用户默认游戏参数
 * @date 2020/6/17 17:27
 */
@Data
public class UserDefault {

    /*
     *主键id
     */
    private Long id;

    /*
     *主键id
     */
    private String userId;

    /*
     *0训练内容1运动协调2认知训练3静态力量
     */
    private String defaultGameType;

    /*
     *1主2被3助4阻
     */
    private String defaultGameMode;

    /*
     *训练时间
     */
    private String defaultTime;

    /*
     *1.自定义 2.大 3.中4.小
     */
    private String defaultRange;

    /*
     *训练轨迹编号
     */
    private String defaultTrajectory;

    /*
     *训练等级
     */
    private String defaultSpeed;

    /*
     *力量保护大小
     */
    private String defaultPowerProtect;

    /*
     *助力类型
     */
    private String defaultAssistiveType;

    /*
     *用户的评估范围
     */
    private String defaultUserRange;

    /*
     *机型
     */
    private String defaultRobotType;

    /*
     *最近的一次游戏
     */
    private String lastGame;

    /*
     *是否已经同步
     */
    private String issync;

    /*
     *动作类型
     */
    private String defaultGameAction;

    /*
     *肢体部位
     */
    private String defaultBodyParts;

    /*
     *中立位
     */
    private String defaultNeutral;

    /*
     *自定义范围
     */
    private String defaultUserCustomRange;

    /*
     *牵伸单次时间
     */
    private String defaultStretchTime;

    /*
     *牵伸次数
     */
    private String defaultStretchCount;

}
