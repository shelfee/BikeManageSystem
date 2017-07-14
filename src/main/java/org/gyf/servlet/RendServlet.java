package org.gyf.servlet;


import org.gyf.common.BikeInfo;
import org.gyf.common.LocationInfo;
import org.gyf.common.StubInfo;
import org.gyf.common.UserInfo;
import org.gyf.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 为客户端提供借车接口
 * 客户端处于登录状态
 * 表单参数
 * stubID:车所在桩号
 * 输出对象:字符串
 * 向客户端传送字符串含"success"表示借车成功否则失败
 *
 * 借车操作需要对LocationInfo表，UserInfo表，BikeInfo表，StubInfo表同时更新保持一致性，一个出错恢复原状后返回失败
 *
 */
@WebServlet(name = "RendServlet")
public class RendServlet extends HttpServlet {
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String unameg = (String) request.getSession().getAttribute("username");
        String ustubg = request.getParameter("stubID");
        long currentTime = System.currentTimeMillis();
        String msg = "";
        if(SessionUserListener.checkIfHasLogin(unameg) && SessionUserListener.getUserMap().get(unameg).equals(request.getSession())){
            UserMysqlDaoImpl userDao = new UserMysqlDaoImpl();
            UserInfo userInfo = (UserInfo) userDao.Query(unameg);
            if(userInfo == null)
                msg = "Invalid ID";
            else{
                int balance = userInfo.getBalance();
                String bikeID = userInfo.getBike();
                long time = userInfo.getTime();
                if(balance <= 0)
                    msg = "Not enough money!";
                else{
                    if(!bikeID.equals("") || time != 0 )
                        msg = "You have rent a bike!";
                    else{

                        MysqlDao stubDao = new StubMysqlDaoImpl();
                        StubInfo stubInfo = (StubInfo) stubDao.Query(ustubg);
                        if(stubInfo == null)
                            msg = "Invalid stubID!";
                        else {
                            String bike = stubInfo.getBike();
                            MysqlDao bikeDao = new BikeMysqlDaoImpl();
                            BikeInfo bikeInfo = (BikeInfo) bikeDao.Query(bike);
                            if(bikeInfo == null)
                                msg = "The stub has no bike!";
                            else{
                                if(!bikeInfo.getStub().equals(ustubg)|| bikeInfo.getTime() != 0 || !bikeInfo.getUser().equals(""))
                                    msg = "False bike-stub relation!";
                                else{
                                    MysqlDao locationDao = new LocationMysqlDaoImpl();
                                    LocationInfo locationInfo = (LocationInfo) locationDao.Query(stubInfo.getLocation());

                                    //设置更新数据库的新数据

                                    LocationInfo newLocationInfo = new LocationInfo();
                                    newLocationInfo.setBikeNum(locationInfo.getBikeNum() - 1);
                                    newLocationInfo.setId(locationInfo.getId());
                                    newLocationInfo.setX(locationInfo.getX());
                                    newLocationInfo.setY(locationInfo.getY());
                                    newLocationInfo.setStubNum(locationInfo.getStubNum());

                                    StubInfo newStubInfo = new StubInfo();
                                    newStubInfo.setBike("");
                                    newStubInfo.setId(stubInfo.getId());
                                    newStubInfo.setLocation(stubInfo.getLocation());

                                    BikeInfo newBikeInfo = new BikeInfo();
                                    newBikeInfo.setId(bikeInfo.getId());
                                    newBikeInfo.setStub("");
                                    newBikeInfo.setTime(currentTime);
                                    newBikeInfo.setUser(unameg);

                                    UserInfo newUserInfo = new UserInfo();
                                    newUserInfo.setAge(userInfo.getAge());
                                    newUserInfo.setId(userInfo.getId());
                                    newUserInfo.setNickname(userInfo.getNickname());
                                    newUserInfo.setTime(currentTime);
                                    newUserInfo.setBalance(userInfo.getBalance());
                                    newUserInfo.setGender(userInfo.getGender());
                                    newUserInfo.setKey(userInfo.getKey());
                                    newUserInfo.setBike(bikeInfo.getId());


                                    //更新数据库，若失败则恢复原来数据
                                    if(userDao.Update(newUserInfo)) {
                                        if (bikeDao.Update(newBikeInfo)) {
                                            if (stubDao.Update(newStubInfo)) {
                                                if (locationDao.Update(newLocationInfo)) {
                                                    msg = "success!";
                                                }
                                                else{
                                                    stubDao.Update(stubInfo);
                                                    bikeDao.Update(bikeInfo);
                                                    userDao.Update(userInfo);
                                                }
                                            }
                                            else{
                                                bikeDao.Update(bikeInfo);
                                                userDao.Update(userInfo);
                                            }
                                        }
                                        else{
                                            userDao.Update(userInfo);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        else{
            msg = "Not invalid account!";
        }
        //创建输出对象，将处理结果返回到客户端
        PrintWriter outUser = response.getWriter();
        outUser.print(msg);
        //关闭输出对象
        outUser.flush();
        outUser.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
