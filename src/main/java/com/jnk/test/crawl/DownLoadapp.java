package com.jnk.test.crawl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DownLoadapp {

    @GetMapping("/Bee360/apps")
    public String  getApp(HttpServletRequest request){
        String agent= request.getHeader("user-agent");
        if(agent.contains("Android")) {
            return "redirect:http://www.bee360.io/download/bee360.apk";
        }
        else  if(agent.contains("iPhone")||agent.contains("iPod")||agent.contains("iPad")){
            return "redirect:https://itunes.apple.com/cn/app/id1432468252?mt=8";
        }else {
            return "redirect:https://www.bee360.io/down.html";
        }

    }

    @GetMapping("/Bee360/appss")
    public String  getAppss(HttpServletRequest request){
        String agent= request.getHeader("user-agent");
        if(agent.contains("Android")) {
            return "redirect:http://www.bee360.io/download/bee360.apk";
        }
        else  if(agent.contains("iPhone")||agent.contains("iPod")||agent.contains("iPad")){
            return "redirect:https://itunes.apple.com/cn/app/id1432468252?mt=8";
        }else {
            return "redirect:https://www.bee360.io/down.html";
        }

    }
}
