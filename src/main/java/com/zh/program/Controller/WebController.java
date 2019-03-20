package com.zh.program.Controller;

import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Entity.Message;
import com.zh.program.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/web")
public class WebController {
    private String PREFIX = "/web/";
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/{id}/{openid}")
    public Object getWeb(@PathVariable("id") Integer id, @PathVariable("openid") String openid, Model model){
        Message message = messageService.selectById(id);
        if(StrUtils.isBlank(message.getInfo())){
            message.setInfo("ID填写错误");
        }
        model.addAttribute("item",message);
        return PREFIX + "web.html";
    }
}
