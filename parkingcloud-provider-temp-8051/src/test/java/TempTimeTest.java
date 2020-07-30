import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TempTimeTest {

    @Test
    public void maina() {

        String DateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss"));
        System.out.println(DateNow);

    }
}
