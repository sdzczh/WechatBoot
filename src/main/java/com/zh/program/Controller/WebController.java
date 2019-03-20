package com.zh.program.Controller;

import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Entity.Message;
import com.zh.program.Service.BrowseRecordService;
import com.zh.program.Service.MessageService;
import com.zh.program.Service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/web")
public class WebController {
    private String PREFIX = "/web/";
    @Autowired
    private MessageService messageService;
    @Autowired
    private BrowseRecordService browseRecordService;

    @RequestMapping(value = "/{id}/{openid}")
    public Object getWeb(@PathVariable("id") Integer id, @PathVariable("openid") String openid, Model model, HttpSession session){
        Message message = messageService.selectById(id);
        if(message == null || StrUtils.isBlank(message.getInfo())){
            message = new Message();
            message.setInfo("ID填写错误");
            return PREFIX + "web.html";
        }
        browseRecordService.check(message, session, openid);
        model.addAttribute("item",message);
        return PREFIX + "web.html";
    }
}
