import core.dbutils.DBException;
import dao.ProductDao;
import dao.UserDao;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void deleteUser() throws DBException {
        ProductDao productDao = new ProductDao();
        productDao.addProduct();

    }

}
