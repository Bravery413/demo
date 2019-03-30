package controllers;

import core.dbutils.DBException;
import dao.ProductDao;
import dto.DProduct;
import models.Product;
import models.User;
import play.mvc.Controller;
import play.cache.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProductController extends Controller {
    public static void listAll(){
        ProductDao pd = new ProductDao();
        List<Product> ps = pd.queryList();
        Collections.sort(ps);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<DProduct> products = new ArrayList<DProduct>();
        for (Product p:ps) {
            long create_time = p.getCreate_time();
            Date date = new Date(create_time);
            DProduct dProduct = new DProduct();
            dProduct.setPid(p.getPid());
            dProduct.setCategory(p.getCategory());
            dProduct.setCreate_time(date);
            dProduct.setPname(p.getPname());
            dProduct.setPrice(p.getPrice());
            products.add(dProduct);
        }
        User user = (User)Cache.get("user");
        if(user!=null)
        System.out.println(user.name);
        render("Application/product.html",products,user);
    }
    public static void addP(){
        render("Application/add.html");
    }
    public static void addProduct(DProduct product) throws DBException, ParseException {
        Product p = new Product();
        p.setPname(product.getPname());
        p.setCategory(product.getCategory());
        p.setPrice(product.getPrice());
        product.getPname();
        Integer pid = product.getPid();
        if(pid!=0){
            p.setPid(pid);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String format = simpleDateFormat.format(product.getCreate_time());
        Date data = simpleDateFormat.parse(format);
        long l=data.getTime();
        p.setCreate_time(l);
        ProductDao productDao = new ProductDao();
        productDao.addProduct(p);
        redirect("/listProduct");
    }
    public static void delProduct(int pid) throws DBException { ;
        ProductDao productDao = new ProductDao();
        productDao.delProduct(pid);
        redirect("/listProduct");
    }
    public static void editProduct(int pid) throws DBException {
        ProductDao productDao = new ProductDao();
        Product p = productDao.getProduct(pid);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long create_time = p.getCreate_time();
        Date date = new Date(create_time);
        DProduct dp = new DProduct();
        dp.setPid(p.getPid());
        dp.setCategory(p.getCategory());
        dp.setCreate_time(date);
        dp.setPname(p.getPname());
        dp.setPrice(p.getPrice());
        render("Application/add.html",dp);
    }

}
