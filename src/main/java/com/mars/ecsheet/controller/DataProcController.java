package com.mars.ecsheet.controller;

import cn.hutool.json.JSONUtil;
import com.mars.ecsheet.common.AjaxResult;
import com.mars.ecsheet.utils.ExcelUtils;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Controller
public class DataProcController {
    @ResponseBody
    @PostMapping("/excel/analyze")
    public AjaxResult analyze(@RequestParam(value = "exceldata") String exceldata, @RequestParam(value = "wbid") String wbid, @RequestParam(value = "se") String se, HttpServletRequest request,
                              HttpServletResponse response) {
        cn.hutool.json.JSONObject seObject = JSONUtil.parseObj(se);
//        cn.hutool.json.JSONArray jsonArray = JSONUtil.parseArray(exceldata);
        cn.hutool.json.JSONObject idObject = JSONUtil.parseObj(wbid);
        String id = idObject.get("wbid").toString();
        String startend = seObject.get("se").toString();
        exceldata = exceldata.replace("&#xA;", "\\r\\n");
        ExcelUtils.exportLuckySheetXlsx(exceldata, request, response, id);
        List<String> selist = Arrays.asList(startend.split(","));
        String usecols = selist.get(0).substring(0,1)+":"+selist.get(1).substring(0,1);
        String srow = selist.get(0).substring(1,2);
        String erow = selist.get(1).substring(1,2);
        System.out.println(usecols+" "+srow+" "+erow);
        Process proc;
        try {
            String exe = "D:\\semi\\Scripts\\python.exe";
            String command = "G:\\luckysheet-import-team-edit-export-master\\src\\main\\java\\com\\mars\\ecsheet\\exceldata\\main.py";
            String cmdArr = exe+" "+command+" --usecols "+usecols+" --srow "+srow+" --erow "+erow+" --excelname "+id+".xlsx";
            proc = Runtime.getRuntime().exec(cmdArr);// 执行py文件
            //用输入输出流来截取结果
            System.out.println(cmdArr);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            String message = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if(message==null) message=line+"\n";
                else message+=line+"\n";
            }
            in.close();
            proc.waitFor();

            return new AjaxResult(200, "success", message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AjaxResult(500,"error");

    }
}
