package controllers;

import models.User;
import notifiers.Mails;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import play.Play;
import play.data.validation.Required;
import play.data.validation.URL;
import play.libs.Codec;
import play.libs.Images;
import play.libs.Mail;
import play.mvc.Controller;
import play.cache.*;
import play.libs.mail.*;

import static play.mvc.Mailer.*;

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
    public static void sendEmail(){
        Mails.welcome();
        render("Application/success.html");
    }
    public static void resiger(){
        String randomID = Codec.UUID();
        render("Application/resiger.html",randomID);
    }
    public static void captcha(String id){
        Images.Captcha ca = Images.captcha();
        String code = ca.getText("#E4EaFD");
        Cache.set(id,code,"10mn");
        renderBinary(ca);
    }
    public static void check(String validate,String randomID) {
        validation.equals(
                validate, Cache.get(randomID)
        ).message("Invalid code. Please type it again");
        if(validation.hasErrors()) {
            flash.put("info", "validate error");
            resiger();
        }

    }



}
