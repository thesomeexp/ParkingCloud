import cn.edu.lingnan.util.MyTool;
import org.junit.Test;

public class JWTTest {

    @Test
    public void isJWTAuth() {
        System.out.println(MyTool.isJWTAuthorized("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.GGp_bE9wxYlRrZ6cazIcGTCbia-qPHD-75BcvUCLGWo"));

    }
}
