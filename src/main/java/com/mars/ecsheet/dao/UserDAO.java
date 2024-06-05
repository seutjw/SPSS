package com.mars.ecsheet.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mars.ecsheet.utils.DBUtils;
import com.mars.ecsheet.entity.UserEntity;
public class UserDAO {

    //查询所有用户信息
    public List<UserEntity> get_all_user_info()
    {
        List<UserEntity> list =new ArrayList<UserEntity>();
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="select * from user";
            pstmt=con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()){
                list.add(new UserEntity(rs.getInt(1),rs.getString(2),
                        rs.getString(3)));//放到集合中
            }
            return list;

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    //密码验证
    public boolean check_pwd(String pwd,String UNAME)
    {
        String list = "";
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="select User_pwd from user where User_name = ?";
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1, UNAME);
            rs = pstmt.executeQuery();

            while(rs.next()){
                list = rs.getString(1);
            }
            if(Objects.equals(pwd, list))
                return true;
            else
                return false;

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    //更改用户昵称
    public void update_user_name()
    {

    }
    //注册用，创建新表项，用户ID递增分配
    public void insert_user_info(String NAME,String PWD)
    {
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="INSERT INTO user (User_name,User_pwd) " +
                    "VALUES (?,?)";
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1,NAME);
            pstmt.setString(2,PWD);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("数据插入成功！");
            } else {
                System.out.println("数据插入失败！");
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    //更新用，插入新的UID,WBID对
    public void insert_new_uid_wbid(int UID,String WBID)
    {
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="INSERT INTO user_workbook (uid,workbookid) " +
                    "VALUES (?,?)";
            pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,UID);
            pstmt.setString(2,WBID);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("数据插入成功！");
            } else {
                System.out.println("数据插入失败！");
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    //获得该用户uid下的所有工作簿ID
    public List<String> getall_workbookid_byuid(int UID)
    {
        List<String> list =new ArrayList<String>();
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="select workbookid from user_workbook where uid = ?";
            pstmt=con.prepareStatement(sql);
            pstmt.setInt(1, UID);
            rs = pstmt.executeQuery();

            while(rs.next()){
                list.add(rs.getString(1));//放到集合中
            }
            return list;

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    public void delete_by_wbid(String WBID)
    {
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="DELETE FROM user_workbook WHERE workbookid = ? ";
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1,WBID);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("数据删除成功！");
            } else {
                System.out.println("数据删除失败！");
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }
    }
    public UserEntity getOneUser(String NAME)
    {
        UserEntity u = new UserEntity();
        String list = "";
        int id = 0;
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement pstmt=null;
        try{
            con = DBUtils.getConnection();
            System.out.println("成功连接");
            String sql="select User_pwd ,User_id from user where User_name = ?";
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1, NAME);
            rs = pstmt.executeQuery();

            while(rs.next()){
                list = rs.getString(1);
                id = rs.getInt(2);
            }
            u.setUserName(NAME);
            u.setPwd(list);
            u.setUid(id);
            return u;
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.close(rs,pstmt,con);
        }

    }
}