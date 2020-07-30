import cn.edu.lingnan.util.JWTConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {
    public static void main(String[] args) {
        System.out.println("len");
    }

    @Test
    public void JWTTest(){
        System.out.println("start");
        Calendar issuedCal = Calendar.getInstance();
        Date date1 = issuedCal.getTime();
        System.out.println("now:" + date1);

        issuedCal.add(Calendar.HOUR_OF_DAY, 1);
        Date date2 = issuedCal.getTime();

        String v_token = null;

        System.out.println("after now:" + date2);
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWTConfig.JWT_SECRET);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withIssuedAt(date1)
                    .withExpiresAt(date2)
                    .sign(algorithm)
                    ;
            System.out.println(token);
            v_token = token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }

//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret1");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(v_token);
            System.out.println("验证成功");
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            exception.printStackTrace();
            System.out.println("验证失败");
        }
    }

    @Test
    public void keyGenerate() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5"); // 可填入 HmacSHA1，HmacSHA256 等
            SecretKey key = keyGenerator.generateKey();

            byte[] keyBytes = key.getEncoded();
            System.out.println(keyBytes);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getJWTClaims(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYXJraW5nY2xvdWQtcHJvdmlkZXItdXNlci04MDExIiwibmFtZSI6IjMzM-awkei1hCIsImlkIjozLCJpc0FkbWluIjp0cnVlLCJleHAiOjE1OTE1MzU1NjgsImlhdCI6MTU5MTUzMTk2OH0.okQj5ZoHVDPbgSptu2hJsmqonwkYsruZWwsN5CMt9zA";
        DecodedJWT jwt = JWT.decode(token);

        Map<String, Claim> claims = jwt.getClaims();    //Key is the Claim name
        Claim claim = claims.get("isAdmin");
        if (claim.isNull()) {
            System.out.println("claim is null");
        } else {
            System.out.println(claim.asBoolean());
        }

    }
}
