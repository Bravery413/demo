package controllers;

import core.dbutils.DBException;
import dao.UserDao;
import play.*;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {


    public static void index() {
        render();
    }
    @Before(unless = "logout")
    public static void check(){
        if (session.get("user")!=null){
            render("Application/index.html");
            System.out.println("change by bravery");
            System.out.println("ddd");
            System.out.println("change by bravery");
            System.out.println("brave change something");
        }
    }


    public static void delete() throws DBException {
        UserDao dao = new UserDao();
        dao.deleteUser();
        render();
    }
    public static void findUser() throws DBException {
        UserDao userDao = new UserDao();
        User user = userDao.findUser();
        System.out.println(user);
//        renderArgs.put("user", user);

//        render("Application/index.html");
        renderJSON(user);
    }



    public static void hello(){
        User user = new User();
        user.setName("py");
        user.setPassword("123");
        User u2 = new User();
        u2.setName("pyy");
        u2.setPassword("123");
        ArrayList<User> users = new ArrayList<>();
        users.add(u2);
        users.add(user);
        render("Application/index.html");
        System.out.println("hello");
    }
}