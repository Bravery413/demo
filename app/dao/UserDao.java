package dao;

import core.dbutils.DBException;
import core.dbutils.DBManager;
import models.User;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.junit.Test;


public class UserDao extends BaseDao{
    public void deleteUser() throws DBException {
        StringBuilder sql = new StringBuilder("delete from user where name=?");
//        User user = DBManager.queryForObject(User.class, sql.toString());
        DBManager.delete(sql.toString(),"name");
//        if (user!= null)
//        System.out.println(user);
    }
    public User findUser() throws DBException {
        StringBuffer sql = new StringBuffer("select * from user where name=name");
        User user = DBManager.queryForObject(User.class, sql.toString());
        System.out.println(user.name);
        return user;
    }
}
