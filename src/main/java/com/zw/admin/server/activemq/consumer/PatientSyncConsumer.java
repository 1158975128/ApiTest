package com.zw.admin.server.activemq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.zw.admin.server.model.User;
import com.zw.admin.server.service.UserService;
import com.zw.admin.server.utils.UserConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 接收FRIS同步的患者数据
 * @author: hanlin.xu
 * @create: 2021-05-17 18:22
 **/
@Slf4j
@Component
public class PatientSyncConsumer {
    @Resource
    private UserService userService;

    private static final String DESTINATION = "user.sync.patient";

    /**
     * 消息传递方式：点对点
     *
     * @param msg msg
     */
    @JmsListener(destination = DESTINATION, containerFactory = "queueListenerFactory")
    public void receiveMessage(String msg) {
        String processId = UUID.randomUUID().toString();
        try {
            log.info("processId: " + processId + ". Queue: " + DESTINATION + ". received msg: " + msg);
            handleMsg(msg);
            log.info("processId: " + processId + ". Queue: " + DESTINATION + ". handle msg successful");
        } catch (Exception e) {
            log.error("processId: " + processId + ". Queue: " + DESTINATION + ". Handle msg errored");
        }
    }

    private void handleMsg(String msg) throws Exception {
        Map<String, String> map = JSONObject.parseObject(msg, Map.class);
        String id = map.get("id");
//        String operate = map.get("operate"); // fris使用的字段，表示在fris用户是新增还是更新操作，对DAS无效
        String userStr = map.get("msg");
        User user = new User();
        UserConvertUtil.beansConvert(user, userStr);
        if (StringUtils.isNotBlank(userStr)) {
            // 判断是否患者
            if (Long.valueOf(0L).equals(user.getIdentity())) {
                user.setUserLanguage("cn");
                user.setUserStyle("light");
                userService.merge(user);
                log.info("msg from:" + id + ", userId:" + user.getId() + "add or update success");
            }
        }
    }

    // 数据DEMO:
    // {"msg":"{\"address\":\"陕西省铜锣县大幻术镇AS村bzu_(:з」∠)_\",\"age\":120,\"birthday\":\"1900-06-05 00:05:43\",\"createTime\":\"2021-05-19 10:06:06\",\"createUser\":\"傅利叶智能小傅\",\"createUserId\":\"07cbccc003904d94b34dfca2fe076a02\",\"email\":\"11@11.com\",\"id\":\"0ed4852931c849768d6bc48bdb23bd91\",\"identity\":0,\"identityNumber\":\"456978190006052236\",\"identityType\":1,\"isAuto\":1,\"isChecked\":0,\"linkman\":\"大头\",\"linkmanPhone\":\"13000000000\",\"marriage\":0,\"name\":\"测试数据2\",\"nationality\":\"维吾尔族\",\"needOperate\":0,\"organizationId\":28,\"phone\":\"13000000000\",\"profession\":10,\"relation\":2,\"resourceSys\":1,\"sex\":\"0\",\"socialSecurityNumber\":\"456978198906052236\",\"treatment\":{\"attendingDoctor\":\"zss管理员\",\"attendingDoctorId\":\"7881700bd26d4134ba52efb4ac297324\",\"costType\":1,\"createTime\":\"2021-05-19 10:06:06\",\"createUser\":\"傅利叶智能小傅\",\"createUserId\":\"07cbccc003904d94b34dfca2fe076a02\",\"hospitalNumber\":\"20210519M280005\",\"id\":25211,\"office\":1,\"organizationId\":28,\"outPatient\":1,\"outPatientStatus\":1,\"patientId\":\"0ed4852931c849768d6bc48bdb23bd91\",\"payStatus\":0,\"snapShot\":\"{\\\"address\\\":\\\"陕西省铜锣县大幻术镇AS村bzu_(:з」∠)_\\\",\\\"age\\\":120,\\\"birthday\\\":-2195625600000,\\\"createTime\\\":1621389966285,\\\"createUser\\\":\\\"傅利叶智能小傅\\\",\\\"createUserId\\\":\\\"07cbccc003904d94b34dfca2fe076a02\\\",\\\"email\\\":\\\"11@11.com\\\",\\\"id\\\":\\\"0ed4852931c849768d6bc48bdb23bd91\\\",\\\"identity\\\":0,\\\"identityNumber\\\":\\\"456978190006052236\\\",\\\"identityType\\\":1,\\\"isAuto\\\":1,\\\"isChecked\\\":0,\\\"linkman\\\":\\\"大头\\\",\\\"linkmanPhone\\\":\\\"13000000000\\\",\\\"marriage\\\":0,\\\"name\\\":\\\"测试数据2\\\",\\\"nationality\\\":\\\"维吾尔族\\\",\\\"needOperate\\\":0,\\\"organizationId\\\":28,\\\"phone\\\":\\\"13000000000\\\",\\\"profession\\\":10,\\\"relation\\\":2,\\\"resourceSys\\\":1,\\\"sex\\\":\\\"0\\\",\\\"socialSecurityNumber\\\":\\\"456978198906052236\\\",\\\"updateTime\\\":1621389966578,\\\"updateUser\\\":\\\"07cbccc003904d94b34dfca2fe076a02\\\"}\",\"sourceId\":1,\"startDate\":\"2021-05-19 10:06:06\"},\"updateTime\":\"2021-05-19 10:06:06\",\"updateUser\":\"07cbccc003904d94b34dfca2fe076a02\"}","operate":"insert","id":"kf"}
}
