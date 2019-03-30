package dto;


import core.dbutils.Table;

import java.util.Date;


public class DProduct {


    private int pid;

    private String category;

    private String pname;

    private int price;

    private Date create_time;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", category='" + category + '\'' +
                ", pname='" + pname + '\'' +
                ", price=" + price +
                ", create_time=" + create_time +
                '}';
    }
}
