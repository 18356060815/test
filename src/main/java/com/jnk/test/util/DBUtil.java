package com.jnk.test.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DBUtil {
    private static DataSource ds = null;
    static {
        try{
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db_server.properties");
            Properties props = new Properties();
            props.load(in);
            ds = DruidDataSourceFactory.createDataSource(props);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException{
        System.out.print(ds);
        return ds.getConnection();
    }



    public static void main(String[] args) {
        for (int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Connection connection= DBUtil.getConnection();

                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery("select  id  from  xs  where id='1'");
                        while (rs.next()){

                            String id=rs.getString("id");
                        }
                        connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }

                }
            }).start();

        }



    }
}
