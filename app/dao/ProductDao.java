package dao;

import core.dbutils.DBException;
import core.dbutils.DBManager;
import models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao extends BaseDao<Product> {
    public List<Product> queryList(){
        StringBuffer sql = new StringBuffer("SELECT * FROM product ");
        List<Product> products = new ArrayList<>();
        try {
            products = DBManager.queryForList(Product.class, sql.toString());
        } catch (DBException e) {
            e.printStackTrace();
        }
        return products;
    }
    public void addProduct(Product product) throws DBException {

        Integer pid = product.getPid();
        System.out.println("addProduct的pid"+pid);
        //pid为空 add操作
        if(pid==0){
            StringBuffer sql = new StringBuffer("insert into product values(null,?,?,?,?)");
            List<Object> params = new ArrayList<Object>();
            params.add(product.getCategory());
            params.add(product.getPname());
            params.add(product.getPrice());
            params.add(product.getCreate_time());
            DBManager.insert(sql.toString(),params.toArray());
        //pid不为空 update操作
        }else{
            StringBuffer sql = new StringBuffer("UPDATE product SET pname=?, category =?,price=?,create_time, WHERE pid = ?");
            List<Object> params = new ArrayList<>();
            params.add(product.getPname());
            params.add(product.getCategory());
            params.add(product.getPrice());
            params.add(product.getCreate_time());
            params.add(pid);
            DBManager.update(sql.toString(),params);
        }
    }
    public void delProduct(int pid) throws DBException {
        StringBuffer sql = new StringBuffer("delete from product where pid=?");
        DBManager.delete(sql.toString(), pid);
    }
    public Product getProduct(int pid) throws DBException {
        StringBuffer sql = new StringBuffer("SELECT * FROM `product` where pid=?");
        Product product = DBManager.queryForObject(Product.class, sql.toString(), pid);
        return product;
    }
}
