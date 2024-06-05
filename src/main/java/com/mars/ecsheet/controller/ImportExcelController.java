
/** 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mars.ecsheet.controller;


import cn.hutool.json.JSONUtil;

import com.mars.ecsheet.common.AjaxResult;

import com.mars.ecsheet.dao.UserDAO;
import com.mars.ecsheet.entity.UserEntity;
import com.mars.ecsheet.entity.WorkBookEntity;
import com.mars.ecsheet.entity.WorkSheetEntity;
import com.mars.ecsheet.repository.WorkBookRepository;
import com.mars.ecsheet.repository.WorkSheetRepository;
import com.mars.ecsheet.utils.SheetUtil;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mars.ecsheet.controller.IndexController;

/**
* @desc: Luckysheet-online-teamwork-edit-demo
* @name: ImportExcelController.java
* @author: tompai
* @email：liinux@qq.com
* @createTime: 2021年5月22日 下午4:34:04
* @history:
* @version: v1.0
*/
@Controller
public class ImportExcelController {

    private static final Logger log = LoggerFactory.getLogger(ImportExcelController.class);
    @Autowired
    private WorkBookRepository workBookRepository;

    @Autowired
    private WorkSheetRepository workSheetRepository;

    // 接受前端传来的exceldata",存到数据库中 并返回dataid



    @ResponseBody
    @PostMapping("/excel/importFile")
    public AjaxResult importExcelFile(@RequestParam(value = "exceldata") String exceldata, @RequestParam(value = "wbid") String wbid,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        // 去除luckysheet中 &#xA的换行
        exceldata = exceldata.replace("&#xA;", "\\r\\n");
//实例化一个Gson对象
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(exceldata);
//        cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(exceldata);
        cn.hutool.json.JSONObject idObject = JSONUtil.parseObj(wbid);
        cn.hutool.json.JSONObject info= JSONUtil.parseObj(jsonObject.get("info"));
        log.info(idObject.toString());
        log.info(exceldata.toString());
        log.info(idObject.get("wbid").toString());
        String id= idObject.get("wbid").toString();
        cn.hutool.json.JSONArray sheetJsonarry= JSONUtil.parseArray(jsonObject.get("sheets"));
        WorkBookEntity wb = new WorkBookEntity();
        wb.setName(info.get("name").toString().replace(".xlsx", ""));
        wb.setOption(SheetUtil.getDefautOption(info));
        WorkBookEntity saveWb = workBookRepository.save(wb);
        workBookRepository.deleteById(id);
        sheetJsonarry.forEach(sheet -> {
            WorkSheetEntity ws = new WorkSheetEntity();
            ws.setWbId(saveWb.getId());
            cn.hutool.json.JSONObject jsonObjects = JSONUtil.parseObj(sheet);
            ws.setData(jsonObjects);
            ws.setDeleteStatus(0);
            workSheetRepository.save(ws);
        });

        //此处导入mysql数据库
        String WBID=saveWb.getId();
        String UID=request.getParameter("uid");
        UserDAO ud = new UserDAO();
        int Uid = Integer.parseInt(UID);
        ud.insert_new_uid_wbid(Uid,WBID);
        ud.delete_by_wbid(id);
        //

        return new AjaxResult(200, "success", saveWb.getId().toString());



    }


    @GetMapping("/import")
    public String importExcel() {
        return "import";
    }

    @ResponseBody
    @PostMapping("/excel/deleteFile")
    public AjaxResult deleteExcelFile(@RequestParam(value = "wbid") String wbid,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {

//        cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(exceldata);
        cn.hutool.json.JSONObject idObject = JSONUtil.parseObj(wbid);
        log.info(idObject.toString());
        log.info(idObject.get("wbid").toString());
        String id= idObject.get("wbid").toString();
        workBookRepository.deleteById(id);
        UserDAO ud = new UserDAO();
        ud.delete_by_wbid(id);
        return new AjaxResult(200, "success", id);



    }
    @ResponseBody
    @PostMapping("/excel/share")
    public AjaxResult share(@RequestParam(value = "obj") String obj,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {

//        cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(exceldata);
        cn.hutool.json.JSONObject idObject = JSONUtil.parseObj(obj);
        log.info(idObject.toString());
        log.info(idObject.get("wbid").toString());
        String id= idObject.get("wbid").toString();
        String username= idObject.get("shareuser").toString();
        UserDAO ud = new UserDAO();
        UserEntity ue = ud.getOneUser(username);
        ud.insert_new_uid_wbid(ue.getUid(), id);
        return new AjaxResult(200, "success", id);



    }

}
