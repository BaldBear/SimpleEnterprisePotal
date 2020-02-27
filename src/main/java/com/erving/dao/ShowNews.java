package com.erving.dao;

import com.erving.model.News;
import com.erving.util.DBConnection;
import com.erving.util.DbUtil;
import com.erving.util.Function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ShowNews {
    private DBConnection dbConnection = new DBConnection();
    private Function fun = new Function();

    /**
     *
     * @param sPage
     * @param strPage
     * @return
     */
    public String ListNewsFront(String sPage, String strPage){
        //获取数据库连接
        try {
            //获取数据库连接
            Connection connection = dbConnection.getConnToMySql();
            //创建查询语句对象
            Statement statement = connection.createStatement();
            //建立结果集
            ResultSet rs = null;
            //定义辅助变量
            StringBuffer sb = new StringBuffer();
            int page = 1;
            int pageSize = 5;
            int i;

            //查询语句
            String sql = "SELECT * FROM news ORDER BY NewsID DESC";
            //执行查询
            rs = statement.executeQuery(sql);
            //判断查询结果是否为空
            if(!rs.next()){
                //查询结果为空,返回属性添加字符串数据用于页面显示
                sb.append("<tr height=\"25\" bgcolor=\"#d6dff7\"  class=\"info1\"><td colspan=\"5\">\n");
                sb.append("<div align=\"center\"><b>没有记录!</b></div></td></tr>\r\n");
            }else{
                page = fun.StrToInt(strPage);
                sPage = fun.CheckReplace(sPage);
                if(page == 0) {
                    page = 1;
                }
                //计算当前页面新闻条数
                //resultSet第一个数据下标为1
                rs.absolute((page-1) * pageSize +1);
                i=0;
                while(i < pageSize && !rs.isAfterLast()){
                    News n = new News();
                    n.setNewsID(rs.getInt("NewsID"));
                    n.setNewsTitle(rs.getString("NewsTitle"));
//                    n.setNewsContent(rs.getString("NewsContent"));
                    n.setNewsDate(rs.getDate("NewsTime"));
                    n.setAdminName(rs.getString("AdminName"));
                    //使用sb拼接输出结果
                    sb.append("<tr>");
                    sb.append("<td>" + n.getNewsTitle() + "</td>");
                    sb.append("<td>" + n.getAdminName() + "</td>");
                    sb.append("<td>" + n.getNewsDate() + "</td>");
                    sb.append("<td ><a style=\"color:#3F862E\" target=\"_blank\" href=\"newsFrontDetail.jsp?newsId="
                            + n.getNewsID() + "\">详情</a></td></tr>");
                    rs.next();
                    i++;
                }
                //实现分页
                sb.append(fun.PageFront(sPage, rs, page, pageSize));
            }
            rs.close();
            statement.close();
            connection.close();
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "No";
        }
    }

    /**
     *
     * @param newsId
     * @return
     */
    public String FrontNewsDetail(String newsId){

        try {
            //获取数据库连接
            Connection connection = dbConnection.getConn();
            //创建查询语句对象
            Statement statement = connection.createStatement();
            //建立结果集
            ResultSet rs = null;
            int id = fun.StrToInt(newsId);
            if(id == 0){
                return "No";
            }else{
                try{
                    String sql = "SELECT * FROM news WHERE NewsID = " + id;
                    rs = statement.executeQuery(sql);
                    StringBuffer sb = new StringBuffer();
                    //定义其实数据
                    int i = 0;
                    while(i<1 && !rs.isAfterLast()){
                        //将光标移动至第一行数据
                        rs.next();
                        News n = new News();
                        n.setNewsTitle(rs.getString("NewsTitle"));
                        n.setNewsContent(rs.getString("NewsContent"));
                        String[] content = n.getNewsContent().split("#");
                        sb.append("<br><h2 style=\"font-size:28px;margin-left:30%\">" + n.getNewsTitle() + "</h2>");

                        for (int j=0; j<content.length; j++){
                            sb.append("<p>" + content[j] + "</p>");
                        }
                        i++;
                    }
                    rs.close();
                    statement.close();
                    connection.close();
                    return sb.toString();

                }catch(Exception e){
                    connection.rollback();
                    connection.close();
                    e.printStackTrace();
                    return "No";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "No";
        }

    }

    public String ListNews(String sPage, String strPage) {
        try {

            Connection Conn = dbConnection.getConn();
            Statement stmt = Conn.createStatement();
            ResultSet rs = null;
            StringBuffer sb = new StringBuffer();

            int i;
            int intPage = 1;
            int intPageSize = 5;

            String sSql = "select * from News order by NewsID desc";
            rs = stmt.executeQuery(sSql);

            if (!rs.next()) {
                sb.append("<tr height=\"25\" bgcolor=\"#d6dff7\"  class=\"info1\"><td colspan=\"4\">\r\n");
                sb.append("<div align=\"center\"><b>没有记录!</b></div></td></tr>\r\n");
            } else {

                intPage = fun.StrToInt(strPage);
                sPage = fun.CheckReplace(sPage);
                if (intPage == 0)
                    intPage = 1;

                rs.absolute((intPage - 1) * intPageSize + 1);
                i = 0;
                while (i < intPageSize && !rs.isAfterLast()) {
                    int NewsID = rs.getInt("NewsID");
                    String NewsTitle = rs.getString("NewsTitle");
                    String NewsContent = rs.getString("NewsContent");
                    String NewsTime = rs.getString("NewsTime");
                    String AdminName = rs.getString("AdminName");

                    sb.append("<tr>");
                    sb.append("<td class=\"table-id\">" + NewsID + "</td>");

                    sb.append("<td>" + NewsTitle + "</td>");
                    sb.append("<td class=\"table-title\">" + AdminName + "</td>");
                    sb.append("<td class=\"table-title\">" + NewsTime + "</td>");
                    sb.append("<td><div class=\"am-btn-toolbar\">");
                    sb.append("<div class=\"am-btn-group am-btn-group-xs\">");
                    sb.append("<input type=\"hidden\" value=\"" + NewsID + "\">");
                    sb.append("<input type=\"hidden\" value=\"" + NewsContent + "\">");
                    sb.append("<input type=\"hidden\" value=\"" + NewsTitle + "\">");
                    sb.append("<a onclick=\"edit(this);\"");
                    sb.append("class=\"am-btn am-btn-primary am-btn-xs \"");
                    sb.append("href=\"javascript:void(0);\"> <span></span> 修改 <a> ");
                    sb.append("<a rel=\"" + NewsID
                            + "\" onclick=\"del(this);\" class=\"am-btn am-btn-danger am-btn-xs \" href=\"javascript:void(0);\"> "
                            + "<span></span>删除<a>");
                    sb.append("</div></div></td></tr>");

                    rs.next();
                    i++;
                }
                sb.append(fun.Page(sPage, rs, intPage, intPageSize));
            }
            rs.close();
            stmt.close();
            Conn.close();
            return sb.toString();
        } catch (Exception e) {
            return "No";
        }
    }

    public String AddNews(String[] s, String s1, String s2) {
        try {
            Connection Conn = dbConnection.getConn();
            Statement stmt = Conn.createStatement();

            ResultSet rs = null;
            String sSql = "select * from News order by NewsID desc";
            rs = stmt.executeQuery(sSql);
            int z = 0;
            int newNum = 0;
            if (!rs.next()) {
                newNum = 1;
            } else {
                while (z < 1 && !rs.isAfterLast()) {
                    int NewsID = rs.getInt("NewsID");
                    newNum = NewsID + 1;
                    break;
                }
            }

            for (int i = 0; i < s.length; i++) {
                if (i != 1)
                    s[i] = fun.getStrCN(fun.CheckReplace(s[i]));
                else
                    s[i] = fun.getStrCN(s[i]);
            }

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String newsTime = format1.format(new Date());

            StringBuffer sql = new StringBuffer();
            sql.append("insert into News (NewsID,NewsTitle,NewsContent,NewsTime,AdminName) values (" + " '" + newNum
                    + "'," + " '" + s[0] + "'," + " '" + s[1] + "'," + " '" + newsTime + "'," + " '" + s1 + "')");

            System.out.println(sql);

            try {

                Conn.setAutoCommit(false);
                stmt.execute(sql.toString());
                Conn.commit();
                Conn.setAutoCommit(true);
                stmt.close();
                Conn.close();

                return "Yes";
            } catch (Exception e) {
                Conn.rollback();
                e.printStackTrace();
                Conn.close();
                return "添加成功!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
    }

    public String EditNews(String[] s, String s0, String s1, String s2) {
        try {

            Connection Conn = dbConnection.getConn();
            Statement stmt = Conn.createStatement();

            for (int i = 0; i < s.length; i++) {
                s[i] = fun.getStrCN(fun.CheckReplace(s[i]));
            }

            int NewsID = fun.StrToInt(s0);

            StringBuffer sql = new StringBuffer();
            sql.append("update News set NewsTitle='" + s[0] + "'" + " ,NewsContent='" + s[1] + "'" + " where NewsID='"
                    + NewsID + "'");

            stmt.executeUpdate(sql.toString());
            stmt.close();
            Conn.close();

            return "Yes";

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            return "编辑错误!";
        }
    }

    public boolean DelNews(String s0, String s1, String s2) {

        try {
            Connection Conn = dbConnection.getConn();
            Statement stmt = Conn.createStatement();
            int NewsID = fun.StrToInt(s0);
            if (NewsID == 0)
                return false;
            else {
                try {
                    String sql = "delete from News where NewsID=" + NewsID;

                    Conn.setAutoCommit(false);
                    stmt.executeUpdate(sql);

                    Conn.commit();
                    Conn.setAutoCommit(true);

                    stmt.close();
                    Conn.close();
                    return true;
                } catch (Exception e) {
                    Conn.rollback();
                    // e.printStackTrace();
                    Conn.close();
                    return false;
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // System.out.print(e.getMessage());

            return false;
        }

    }

}
