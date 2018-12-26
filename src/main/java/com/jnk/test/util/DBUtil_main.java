package com.jnk.test.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DBUtil_main {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil_main.class);
    public static Connection getConnection() {
        String url = "jdbc:mysql://103.229.116.92:3306/bee360";
        String user = "caijian";
        String password = "Vhldzcqi@2Bf";

//        String url = "jdbc:mysql://localhost:3306/bee360";
//        String user = "root";
//        String password = "a86508650";


        String driverClassName = "com.mysql.jdbc.Driver";
        Connection conn = null;
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;


    }



    public static Boolean CheakConnectionAndQuery(Statement st,String querySql) {//查询验证是否有数据 返回Boolean
        Boolean flag=true;
        try {
            if (st.isClosed()) {
                st = getConnection().createStatement();
            }
            ResultSet rs=st.executeQuery(querySql);
            flag=rs.next();

        }catch (Throwable e){
            e.printStackTrace();
        }
//        finally {
//            try {
//                st.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//
//        }
        return flag;
    }

    public static ResultSet CheakConnectionAndQueryStr(Statement st,String querySql) {//查询验证是否有数据 并且返回结果集
        ResultSet rs=null;
        try {
            if (st.isClosed()) {
                st = getConnection().createStatement();
            }
            rs=st.executeQuery(querySql);
        }catch (Throwable e){
            e.printStackTrace();
        }
//        finally {
//            try {
//                st.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//
//        }
        return rs;


    }






    public static void CheakConnectionAndEx(Statement st, String sql) {//执行插入更新

        try {
            if (st.isClosed()) {
                st = getConnection().createStatement();
            }
            st.executeUpdate(sql);

        }catch (Throwable e){
            e.printStackTrace();
        }
//        finally {
//            try {
//                st.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//
//        }

    }




}
