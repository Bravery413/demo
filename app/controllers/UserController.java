package controllers;

import models.User;
import play.data.validation.Required;
import play.mvc.Controller;
import play.cache.*;

public class UserController extends Controller {
    //跳转到登录页面
    public static void login(){
        render("Application/add.html");
    }

    //登录检测
    public static void loginCheck(@Required String name,
                                  @Required String password){
        if("py".equals(name)&&"123".equals(password)){
            User user = new User();
            user.setName(name);
            user.setPassword(password);
//            session.put("user",user);
            Cache.set("user",user);
            redirect("/listProduct");
        }else{
            renderArgs.put("msg", "账号不存在或者密码错误");
            render("Application/login.html");
        }
    }
    //退出登录
    public static void logout(){
        session.clear();
        render("Application/login.html");
    }
}
