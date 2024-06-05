package com.mars.ecsheet.utils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;
public class DBUtils {
    private static String driver="com.mysql.jdbc.Driver";
    private static String url="jdbc:mysql://127.0.0.1:3306/spss?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";
    private static String username="root";
    private static String pwd="Tjw172839";
    static
    {
        try
        {
            Class.forName(driver);
        } catch (ClassNotFoundException e)
        {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    //连接数据库
    public static Connection getConnection() {
        Connection con=null;
        try {
            con=DriverManager.getConnection(url,username,pwd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return con;
    }
    //关闭数据库
    public static void close(ResultSet rs,PreparedStatement ps,Connection cn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (cn != null) {
            try {
                cn.close();  //  此时的关闭，是归还给连接池对象
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}


