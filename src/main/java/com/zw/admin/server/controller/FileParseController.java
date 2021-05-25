package com.zw.admin.server.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zw.admin.server.dao.MachineInfoDao;
import com.zw.admin.server.model.Province;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author larry
 * @Description:
 * @date 2020/11/11 15:52
 */
@RestController
@RequestMapping("/fileParse")
public class FileParseController {

    @Resource
    private MachineInfoDao machineInfoDao;
    
    /**
     * 功能描述:初始化icf数据
     *
     * @param json
     * @return io.renren.common.utils.R
     * @author larry
     * @Date 2020/9/2 15:20
     */
    @PostMapping("/init")
    public ResultVO<?> initInternalClassicalFunctioning(@RequestBody JSONObject json) throws Exception {
        //String enPath = json.getStr("enPath");
        String provincePath = json.getStr("provincePath");
        //File enfile = new File(enPath);
        File chfile = new File(provincePath);
        List<JSONObject> resourceList = new ArrayList<JSONObject>();
        Map<String, JSONObject> resourceMap = new HashMap<>();
        String chs = FileUtils.readFileToString(chfile, "UTF-8");
        JSONObject provinceJson = new JSONObject(chs);
        JSONUtil.toBean(provinceJson,Map.class);
        Map<String,Object> provinces = (Map)JSON.parse(chs);
        Map<String, Object> map = new HashMap<>();
        Set<String> mapSet = provinces.keySet();
        for(String  str: mapSet){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(str,provinces.get(str));
            Province province = new Province();
            province.setProvince(str);
            province.setCoordinate(provinces.get(str).toString());
            machineInfoDao.initPlaceCoordinate(province);
        }
        return R.success();
    }

    public String getProperties(Map<String, JSONObject> map, String properties, String id, JSONObject json) {
        Map<String, String> cols = new HashMap<String, String>();
        cols.put("en-US", json.getStr(properties));
        JSONObject obj = map.get(id);
        if (obj != null) {
            //log.info("id====="+id);
            cols.put("zh-CN", map.get(id).getStr(properties));
        }
        //判断是否都是空
        if (cols.get("en-US") == null && cols.get("zh-CN") == null) {
            return null;
        }
        return JSONUtil.toJsonStr(cols);
    }

    public List<JSONObject> initJsonEnToList(JSONArray array, List<JSONObject> resourceList) {
        ArrayList<JSONObject> arrays = array.toList(JSONObject.class);
        //递归判断
        for (JSONObject json : arrays) {
            JSONArray children = (JSONArray) json.get("children");
            if (children != null) {
                this.initJsonEnToList(children, resourceList);
            } else {
                //叶子节点的初始化
                resourceList.add(json);
            }
        }
        return resourceList;
    }
}
