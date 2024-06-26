package com.mars.ecsheet.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mars.ecsheet.entity.WorkBookEntity;
import com.mars.ecsheet.entity.WorkSheetEntity;
import com.mars.ecsheet.repository.WorkBookRepository;
import com.mars.ecsheet.repository.WorkSheetRepository;
import com.mars.ecsheet.utils.SheetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.mars.ecsheet.dao.UserDAO;
/**
 * @author Mars
 * @date 2020/10/28
 * @description
 */
@RestController
public class IndexController {

    @Autowired
    private WorkBookRepository workBookRepository;

    @Autowired
    private WorkSheetRepository workSheetRepository;

    @GetMapping("index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<WorkBookEntity> all = new ArrayList<>();
        UserDAO ud =new UserDAO();

        String username = request.getParameter("username");
        String uid = request.getParameter("uid");

        List<String> l = ud.getall_workbookid_byuid(Integer.parseInt(uid));
        for(int i = 0;i<l.size();i++)
        {
            String WBID = l.get(i);
            WorkBookEntity w = workBookRepository.findById(WBID).get();
            all.add(w);
        }

        ModelMap model = new ModelMap();
        model.addAttribute("username",username);
        model.addAttribute("uid",uid);
        model.addAttribute("all",all);
        ModelAndView mav = new ModelAndView("pageinfo", model);
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("index2")
    public ModelAndView index2() throws IOException {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index2");
        return mav;
    }


    @GetMapping("index/create")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String uid = request.getParameter("uid");
        WorkBookEntity wb = new WorkBookEntity();
        wb.setName("default");
        wb.setOption(SheetUtil.getDefautOption());
        WorkBookEntity saveWb = workBookRepository.save(wb);
        //生成sheet数据
        generateSheet(saveWb.getId());
        String WBID=saveWb.getId();
        String UID=uid;
        UserDAO ud = new UserDAO();
        int Uid = Integer.parseInt(UID);
        ud.insert_new_uid_wbid(Uid,WBID);
        response.sendRedirect("/index/"+saveWb.getId()+"?username="+username+"&uid="+uid);
    }


    @GetMapping("/index/{wbId}")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "wbId") String wbId) {
        String username = request.getParameter("username");
        String uid = request.getParameter("uid");
        Optional<WorkBookEntity> Owb = workBookRepository.findById(wbId);
        WorkBookEntity wb = new WorkBookEntity();
        if (!Owb.isPresent()) {
            wb.setId(wbId);
            wb.setName("default");
            wb.setOption(SheetUtil.getDefautOption());
            WorkBookEntity result = workBookRepository.save(wb);
            generateSheet(wbId);
        } else {
            wb = Owb.get();
        }
        ModelMap model = new ModelMap();
        model.addAttribute("username",username);
        model.addAttribute("uid",uid);
        model.addAttribute("wb",wb);
        ModelAndView mav = new ModelAndView("pageinfo", model);
        mav.setViewName("websocket");
        return mav;
    }

    @PostMapping("/load/{wbId}")
    public String load(@PathVariable(value = "wbId") String wbId) {

        List<WorkSheetEntity> wsList = workSheetRepository.findAllBywbId(wbId);
        List<JSONObject> list = new ArrayList<>();
        wsList.forEach(ws -> {
            list.add(ws.getData());
        });


        return JSONUtil.toJsonStr(list);
    }


    @PostMapping("/loadSheet/{wbId}")
    public String loadSheet(@PathVariable(value = "wbId") String wbId) {
        List<WorkSheetEntity> wsList = workSheetRepository.findAllBywbId(wbId);
        List<JSONObject> list = new ArrayList<>();
        wsList.forEach(ws -> {
            list.add(ws.getData());
        });
        if (!list.isEmpty()) {
            return SheetUtil.buildSheetData(list).toString();
        }
        return SheetUtil.getDefaultAllSheetData().toString();
    }


    public void generateSheet(String wbId) {
        SheetUtil.getDefaultSheetData().forEach(jsonObject -> {
            WorkSheetEntity ws = new WorkSheetEntity();
            ws.setWbId(wbId);
            ws.setData(jsonObject);
            ws.setDeleteStatus(0);
            workSheetRepository.save(ws);
        });
    }

}
