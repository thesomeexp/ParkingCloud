package cn.edu.lingnan.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Max
 */
@Component
public class FtpUtil {
    /**
     * FTP_ADDRESS: ftp 服务器ip地址
     * FTP_PORT: ftp 服务器port，默认是21
     * FTP_USERNAME: ftp 服务器用户名
     * FTP_PASSWORD: ftp 服务器密码
     * FTP_BASE_PATH: ftp 服务器存储图片的绝对路径
     * IMAGE_BASE_URL: ftp 服务器外网访问图片路径
     */
    private static String FTP_ADDRESS = "192.168.1.67";

    private static Integer FTP_PORT = 21;

    private static String FTP_USERNAME = "infoimage";

    private static String FTP_PASSWORD = "45g2rVaZ8R9GGka8";

    /**
     * 上传图片
     * @param inputStream 输入流
     * @param name 文件名
     * @return 成功/失败
     * @throws IOException IO异常
     */
    public static boolean uploadImage(InputStream inputStream, String name, String path) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            System.out.println("FTP_ADDRESS: " + FTP_ADDRESS + ":" + FTP_PORT);
            ftpClient.enterLocalPassiveMode();
            ftpClient.connect(FTP_ADDRESS, FTP_PORT);
            ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
            if(path != null){
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 每次数据连接之前，FTPClient告诉FTPServer开通一个端口来传输数据
            ftpClient.enterLocalPassiveMode();
            boolean isSucceed = ftpClient.storeFile(name, inputStream);
            if (isSucceed){
                return true;
            }
            System.out.println("FtpUtil.uploadImage");
        }catch (Exception e){
            System.out.println("FTP上传错误");
            e.printStackTrace();
            return false;
        }finally {
            try{
                ftpClient.logout();
            } catch (Exception e){
                System.out.println("FTP关闭异常");
                e.printStackTrace();
            }
        }
        return false;
    }
}