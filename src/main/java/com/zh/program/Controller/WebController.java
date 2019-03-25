package com.zh.program.Controller;

import com.zh.program.Common.RedisKey;
import com.zh.program.Common.exception.WeixinException;
import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Entity.Message;
import com.zh.program.Entity.Sysparams;
import com.zh.program.Entity.WechatUser;
import com.zh.program.Service.BrowseRecordService;
import com.zh.program.Service.MessageService;
import com.zh.program.Service.SysparamsService;
import com.zh.program.Service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


@Controller
@RequestMapping("/web")
public class WebController {
    private String PREFIX = "/web/";
    @Autowired
    private MessageService messageService;
    @Autowired
    private BrowseRecordService browseRecordService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private SysparamsService sysparamsService;

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

    @ResponseBody
    @GetMapping("/getReferUrl")
    public String getReferUrl(Integer id, String code, String openid, String callback){
        Sysparams sysparams = sysparamsService.getSysparams(RedisKey.SYSTEM_URL);
        if(sysparams == null){
            return WeixinException.ERROR_SERVER_URL;
        }
        String sys_url = sysparams.getKeyValue();
        StringBuffer referUrl = new StringBuffer();
        referUrl.append(sys_url).append("/index/").append(id).append("/").append(openid);
        String result =  "{'ret':'" + referUrl.toString() + "'}";
        result = callback + "("+result+")";
        return result;
    }
}
