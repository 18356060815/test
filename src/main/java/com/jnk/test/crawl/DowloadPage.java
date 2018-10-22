package com.jnk.test.crawl;

import com.alibaba.druid.pool.DruidDataSource;
import com.jnk.test.CrawlInterface.Download;
import com.jnk.test.util.JsoupUtilPor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class DowloadPage{

    @Autowired
    private DataSource dataSource;



    @RequestMapping("/xs")
    @ResponseBody
    public String  downloadPage() {



        Document document=JsoupUtilPor.get("http://www.huoqiucaijing.com/Home/Index");
        Elements elements=document.select("div.content_article_lists").select("a");
        for(Element element:elements){
            String id=element.attr("href");
            String title=element.attr("href");
            String content=element.attr("href");
            Connection connection=null;
            try {
                connection= dataSource.getConnection();
                Statement st=connection.createStatement();
                st.execute("insert into xs (`id`,`title`,`content`) values ('"+id+"','"+title+"','"+content+"')");
            }catch (SQLException e){
                e.printStackTrace();
            }
            finally {
                try {
                    connection.close();
                }catch (SQLException E){

                }

            }
            System.out.print(dataSource);
            break;
        }
        return  "111";
    }

    public static void main(String[] args) throws Exception {
        DowloadPage dowloadPage=new DowloadPage();
        dowloadPage.downloadPage();

    }

//     ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("javascript");
//
//        String jsFileName = "C:\\Users\\iui\\Desktop\\index.js";   // 读取js文件
//
//        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
//        engine.eval(reader);
//
//        if(engine instanceof Invocable) {
//            Invocable invoke = (Invocable)engine;    // o，并传入两个参数
//
//            // c = merge(2, 3);
//
//            String  c = (String)invoke.invokeFunction("get_as_cp_signature");
//
//            System.out.println(c);
//        }
//
//        reader.close();
}
