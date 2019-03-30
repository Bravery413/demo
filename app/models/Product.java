package models;

import core.dbutils.Column;
import core.dbutils.Table;

import java.util.Date;

@Table(name = "product")
public class Product implements Comparable<Product> {

    @Column(name = "pid")
    private int pid;
    @Column(name = "category")
    private String category;
    @Column(name = "pname")
    private String pname;
    @Column(name = "price")
    private int price;
    @Column(name = "create_time")
    private Long create_time;

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

    public int getPrice() {
        return price;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
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

    @Override
    public int compareTo(Product o) {
        return (int)(o.create_time-create_time);
    }
}
