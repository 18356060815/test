package com.jnk.test.Service;

import java.sql.*;
import java.util.*;
import javax.annotation.Resource;

import com.jnk.test.util.CheckUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Service
public class DBUtil {
    @Resource
    JdbcTemplate jdbcTemplate;

    //通用新闻插入 并返回自增主键值 用于插入file_info
    //有新闻就略过  没有就插入
    public  synchronized  void  insertAndQuery(String sql,String title,String hrefaddr,Object[] objects) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String querysql="select title from news_info where title=? or href_addr=?";
        List list=jdbcTemplate.queryForList(querysql,new Object[]{title,hrefaddr});
        System.out.println(list);


        if(list.size()!=0){
                return;
            }else {
                PreparedStatementCreator preparedStatementCreator = con -> {
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for(int i=1;i<=objects.length;i++){
                        ps.setObject(i,objects[i-1]);
                    }

                    return ps;
                };
                try {
                    jdbcTemplate.update(preparedStatementCreator,keyHolder);
                }catch (Throwable e){
                    e.printStackTrace();
                    CheckUtil.getTrace(e);
                    return;
                }
                //新闻资讯的图片 插入file_info表 标签遵守原规则为:header ()
                Long id= keyHolder.getKey().longValue();
                    if(!"".equals(id)&&id!=null){
                        List <Map<String,Object>>lists= jdbcTemplate.queryForList("select pic_url from news_info where id='"+id+"'");
                        String pic_url=lists.get(0).get("pic_url").toString();
                        String insertsql="insert into file_info (`target_table`,`target_field`,`target_id`,`file_path`) values ('news_info','header','"+id+"','"+pic_url+"')";
                        jdbcTemplate.execute(insertsql);
                        System.out.println(insertsql);

                    }

            }

    }



    //通用新闻插入 并返回自增主键值 用于插入file_info
    //有新闻就略过  没有就插入
    public  synchronized  void  insertAndQueryCoin(String updatesql,String insertsql ,String name,String symbol,Object[] objects) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String querysql="select id from virtual_currency_info where name=? and symbol=? ";
        List list=jdbcTemplate.queryForList(querysql,new Object[]{name,symbol});
        System.out.println(list);


        if(list.size()!=0){
            jdbcTemplate.update(updatesql);
        }else {
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement ps = con.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
                for(int i=1;i<=objects.length;i++){
                    ps.setObject(i,objects[i-1]);
                }

                return ps;
            };
            try {
                jdbcTemplate.update(preparedStatementCreator,keyHolder);
            }catch (Throwable e){
                e.printStackTrace();
                return;
            }
//            //新闻资讯的图片 插入file_info表 标签遵守原规则为:header ()
//            Long id= keyHolder.getKey().longValue();
//            if(!"".equals(id)&&id!=null){
//                String insertsqls="insert into file_info (`target_table`,`target_field`,`target_id`,`file_path`) values ('virtual_currency_info','logo','"+id+"','"+imgurl+"')";
//                jdbcTemplate.execute(insertsqls);
//                System.out.println(insertsqls);
//
//            }

        }

    }


    /**
     *
     * 查询库中是否有币数据
     *
     *
    **/

    public  boolean  findById(String id) {
        List list=jdbcTemplate.queryForList("select id from virtual_currency_info where id='"+id+"';");
        if(list.size()!=0){
            return  true;
        }else {
            return  false;
        }

    }

    //更新vix 恐慌指数
    public  void execute(String sql) {
        jdbcTemplate.execute(sql);

    }


    //查询种类是否是需要的种类
    public  Boolean queryIsexists(String types) {
        List list=jdbcTemplate.queryForList("select word from nv_word where word='"+types+"';");
        if(list.size()!=0)
            return true;
        else
            return false;
    }



    //查询
    public  List<Map<String, Object>> find(String sql) {
       return  jdbcTemplate.queryForList(sql);

    }
    //修改
    public  void executes(String name) {
        List list=jdbcTemplate.queryForList("select `name` from lexicon where `name` ='"+name+"';");
        if(list.size()==0){
            jdbcTemplate.execute("insert into lexicon (`name`) values ('"+name+"');");
        }else {
            return;
        }
    }

    //标签
    public  synchronized  Long   BixiaobaiinsertAndQuery(String sql,Object[] objects,String first_tag,String second_tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        first_tag=first_tag.replace("'","\\'");
        second_tag=second_tag.replace("'","\\'");

        List <Map<String ,Object>> l=jdbcTemplate.queryForList("select id from project_tag where first_tag='"+first_tag+"' and second_tag='"+second_tag+"'");
            if(l.size()!=0){
                Object o=l.get(0).get("id");
                return Long.parseLong(o.toString());
            }
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for(int i=1;i<=objects.length;i++){
                    ps.setObject(i,objects[i-1]);
                }

                return ps;
            };
            try {
                jdbcTemplate.update(preparedStatementCreator,keyHolder);
            }catch (Throwable e){
                CheckUtil.getTrace(e);
            }
            //新闻资讯的图片 插入file_info表 标签遵守原规则为:header ()
            Long id= keyHolder.getKey().longValue();
            return id;
//            if(!"".equals(id)&&id!=null){
//                List <Map<String,Object>>lists= jdbcTemplate.queryForList("select pic_url from news_info where id='"+id+"'");
//                String pic_url=lists.get(0).get("pic_url").toString();
//                String insertsql="insert into file_info (`target_table`,`target_field`,`target_id`,`file_path`) values ('news_info','header','"+id+"','"+pic_url+"')";
//                jdbcTemplate.execute(insertsql);
//                System.out.println(insertsql);
//
//            }


    }




    //项目资讯插入
    public  synchronized  void  insertAndQuery_pro(String sql,String title,String hrefaddr,Object[] objects) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String querysql = "select title from news_info where title=? or href_addr=?";
        List list = jdbcTemplate.queryForList(querysql, new Object[]{title, hrefaddr});
        System.out.println(list);


        if (list.size() != 0) {
            return;
        } else {
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 1; i <= objects.length; i++) {
                    ps.setObject(i, objects[i - 1]);
                }

                return ps;
            };
            try {
                jdbcTemplate.update(preparedStatementCreator, keyHolder);
            } catch (Throwable e) {
                e.printStackTrace();
                return;
            }


        }

    }






    //资讯插入返回id
    public  synchronized  Long  insertAndQuery(String sql,Object[] objects,String name,String symbol) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        //如果有这个项目 直接返回id
        //
        name=name.replace("'","\\'");
        symbol=symbol.replace("'","\\'");

        List <Map<String ,Object>> l=jdbcTemplate.queryForList("select id from project_info where name='"+name+"' and symbol='"+symbol+"'");
        if(l.size()!=0){
            Object o=l.get(0).get("id");
            return Long.parseLong(o.toString());
        }

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i=1;i<=objects.length;i++){
                ps.setObject(i,objects[i-1]);
            }

            return ps;
        };
        try {
            jdbcTemplate.update(preparedStatementCreator,keyHolder);
        }catch (Throwable e){
            e.printStackTrace();
        }
        //新闻资讯的图片 插入file_info表 标签遵守原规则为:header ()
        Long id= keyHolder.getKey().longValue();
        return id;
//            if(!"".equals(id)&&id!=null){
//                List <Map<String,Object>>lists= jdbcTemplate.queryForList("select pic_url from news_info where id='"+id+"'");
//                String pic_url=lists.get(0).get("pic_url").toString();
//                String insertsql="insert into file_info (`target_table`,`target_field`,`target_id`,`file_path`) values ('news_info','header','"+id+"','"+pic_url+"')";
//                jdbcTemplate.execute(insertsql);
//                System.out.println(insertsql);
//
//            }


    }













    @Test
    public  void putFilePic(){
        List <Map<String ,Object>>list_news= jdbcTemplate.queryForList("SELECT id,pic_url from news_info where from_interface='BILAI_NEWS' or from_interface='HUOXING_NEWS' or from_interface='JINSE_NEWS' or from_interface='BABITE_NEWS' or from_interface='JINNIU_NEWS' ");
        List <Map<String ,Object>>list_pic= jdbcTemplate.queryForList("SELECT target_id  from file_info  where target_table='news_info';");
        System.out.println(list_news.size());
        System.out.println(list_pic.size());

        List <String>list_news_1=new ArrayList();
        List <String>list_pic_1=new ArrayList();

        Map maps=new LinkedHashMap();
        for(Map map:list_news){
            String id=map.get("id").toString();
            String pic_url=map.get("pic_url").toString();
            maps.put(id,pic_url);
            list_news_1.add(id);
        }
        System.out.println(maps);

        for(Map map:list_pic){
            String target_id=map.get("target_id").toString();
            list_pic_1.add(target_id);
        }

        for(String a:list_pic_1){
            Boolean b=list_news_1.contains(a);
            if(b) {
                System.out.println("新的还已经有的 : "+a);
                list_news_1.remove(a);
            }else {
                continue;
            }

        }
        System.out.println(list_news_1.size());
        System.out.println(list_pic_1.size());
        for(String id:list_news_1){
            System.out.println(id);
            jdbcTemplate.execute("insert into file_info (`target_table`,`target_field`,`target_id`,`file_path`) values ('news_info','header','"+id+"','"+maps.get(id)+"')");

        }
        System.out.println(list_news_1.size());
        System.out.println(list_pic_1.size());


    }



}
