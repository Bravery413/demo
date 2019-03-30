package models;

import core.dbutils.Column;
import core.dbutils.Table;
import play.db.jpa.Model;
@Table(name = "user")
public class User extends Model {



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "name")
    public String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "password")
    public String password;
}
