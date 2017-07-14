package org.gyf.servlet;

import org.gyf.common.LocationInfo;
import org.gyf.dao.LocationMysqlDaoImpl;
import org.gyf.dao.MysqlDao;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.ArrayList;

/**
 * 向安卓客户端提供所有区域信息的接口
 * 客户端可处于任意状态
 * 表单参数无
 * 输出对象：ArrayList<LocationInfo>
 * 接收到对象为空则表示服务器端查询失败
 *
 */
@javax.servlet.annotation.WebServlet(name = "LocationInfoServlet")
public class LocationInfoServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<LocationInfo> loc  = null;
        //将响应的编码规范成 utf-8
        response.setContentType("text/html;charset=UTF-8");
        try {
            //调用数据访问层代码进行访问
            LocationMysqlDaoImpl LocationDao = new LocationMysqlDaoImpl();
            loc =  LocationDao.Query();
            System.out.println("Location:");
            for(int i = 0; i < loc.size(); i++){
                System.out.println(loc.get(i).getId() + ":" + String.valueOf(loc.get(i).getBikeNum()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //创建输出对象，将处理结果返回到客户端
        ServletOutputStream outUser = response.getOutputStream();
        ObjectOutputStream objectUser = new ObjectOutputStream(outUser);
        objectUser.writeObject(loc);
        objectUser.close();
        //关闭输出对象
        outUser.flush();
        outUser.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
