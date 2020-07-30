import cn.edu.lingnan.util.FtpUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FTPFileTest {

    // fileTest
    @Test
    public void fileTest(){
        File file = new File("D:/1.jpg");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FtpUtil.uploadImage(fileInputStream, "1.jpg", "/home/nginx/www/head");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound一茶馆");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("io异常");
            e.printStackTrace();
        }

    }
}
