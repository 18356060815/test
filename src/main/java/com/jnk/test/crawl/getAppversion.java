package com.jnk.test.crawl;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class getAppversion {

    @RequestMapping(value = "getAppVersion", method = RequestMethod.POST)
    public @ResponseBody String  getAppVersion() {
        Map map=new HashMap();

        map.put("code","2.0.1");
        map.put("msg","版本更新");
        JSONObject jsonObject=JSONObject.fromObject(map);
        return jsonObject.toString();
    }
}
