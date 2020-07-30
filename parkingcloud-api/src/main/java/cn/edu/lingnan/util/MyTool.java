package cn.edu.lingnan.util;

import ch.hsr.geohash.GeoHash;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyTool {
    public static final double[] praseLocation(String location) throws Exception {
        String[] locationArray = location.split(",");
        String x = locationArray[0];
        String y = locationArray[1];
        double dou_x = Double.valueOf(x).doubleValue();
        double dou_y = Double.valueOf(y).doubleValue();
        double[] xyArray = new double[2];
        xyArray[0] = dou_x;
        xyArray[1] = dou_y;
        return xyArray;
    }

    public static final boolean isStringEmpty(String... strArray) throws Exception {
        for (String str : strArray)
            if (str == null || str.equals("") || str.equals("null"))
                return true;
        return false;
    }

    public static final boolean isValueLengthLegal(String val, int len) {
        if (val.length() > len)
            return false;
        return true;
    }

    public static final boolean isXYLegal(double x, double y) throws Exception {
        if (x < MagicVariable.MINIMUM_X ||
                x > MagicVariable.MAXIMUM_X ||
                y < MagicVariable.MINIMUM_Y ||
                y > MagicVariable.MAXIMUM_Y) {
            return false;
        } else {
            return true;
        }
    }

    public static final boolean isStateLegal(int... states) throws Exception {
        for (int state : states)
            if (state < MagicVariable.MINIMUM_STATE ||
                    state > MagicVariable.MAXIMUM_STATE)
                return false;
        return true;
    }

    public static final boolean isStarLegal(int... stars) throws Exception {
        for (int star : stars)
            if (star < MagicVariable.MINIMUM_STAR ||
                    star > MagicVariable.MAXIMUM_STAR)
                return false;
        return true;
    }

    public static boolean isUserGeoHashInArray(String userGeoHash, String infoGeoHash, GeoHash[] arryGeoHash) {
        if (userGeoHash.equals(infoGeoHash))
            return true;
        for (GeoHash geoHash : arryGeoHash) {
            if (geoHash.toBase32().equals(userGeoHash))
                return true;
        }
        return false;
    }

    // 判断JWT签名是否合法
    // 过期了也不行
    public static boolean isJWTAuthorized(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWTConfig.JWT_SECRET);
            JWT.require(algorithm)
                    .build()
                    .verify(token); //Reusable verifier instance
            System.out.println("isJWTAuthorized:JWT签名合法--验证成功");
            return true;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            exception.printStackTrace();
            System.out.println("isJWTAuthorized:JWT签名不合法--验证失败");
            return false;
        }
    }

    public static int getUserId(String token) throws NullPointerException{
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("id");
        if (claim.isNull()) {
            throw new NullPointerException();
        } else {
            return claim.asInt();
        }
    }

    public static int getUserScore(String token) throws NullPointerException{
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("score");
        if (claim.isNull()) {
            throw new NullPointerException();
        } else {
            return claim.asInt();
        }
    }

    public static String getUserName(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("name");
        if (claim.isNull()) {
            throw new NullPointerException();
        } else {
            return claim.asString();
        }
    }

    public static boolean isAdmin(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("isAdmin");
        if (claim.isNull()) {
            throw new NullPointerException();
        } else {
            return claim.asBoolean();
        }
    }

    public static String headerToToken(String header) {
        return header.replace(JWTConfig.TOKEN_PREFIX, "");
    }

//    读取文件的 0. latitude 1. longitude 2. time
    public static String[] readFileInfomation(File file)  throws ImageProcessingException, IOException {
        String[] origin = readExif(file);
        String[] result = new String[3];
        String[] pla = parseLocation(origin[0], origin[1]);
        result[0] = pla[0];
        result[1] = pla[1];
        result[2] = origin[3];
        return result;
    }

    public static String[] readExif(File file) throws ImageProcessingException, IOException {
        String[] array = new String[3];
//如果你对图片的格式有限制，可以直接使用对应格式的Reader如：JPEGImageReader
        ImageMetadataReader.readMetadata(file)
                .getDirectories().forEach(v ->
                v.getTags().forEach(t -> {
                    switch (t.getTagName()) {
                        //                    经度
                        case "GPS Latitude":
                            array[0] = t.getDescription();
                            break;
                        //                        纬度
                        case "GPS Longitude":
                            array[1] = t.getDescription();
                            break;
                        //                        拍摄时间
                        case "Date/Time Original":
                            array[2] = t.getDescription();
                        default:
                            break;
                    }
                })
        );
        String[] res = parseLocation(array[0], array[1]);
        array[0] = res[0];
        array[1] = res[1];
        return array;
    }

    public static String[] parseLocation(String latitude, String longitude) {
        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);
        String dou_lat = parseLocation(latitude);
        String dou_lon = parseLocation(longitude);
        String[] dou = new String[2];
        dou[0] = dou_lat;
        dou[1] = dou_lon;
        return dou;
    }

    public static String parseLocation(String a) {
        String[] array = a.split("[°]");
        String a1 = array[0].trim();
        System.out.println(array[0]);
        String[] array2 = array[1].split("[']");
        String a2 = array2[0].trim();
        String[] array3 = array2[1].split("[\"]");
        String a3 = array3[0].trim();
        Double d1 = Double.valueOf(a1);
        Double d2 = Double.valueOf(a2);
        Double d3 = Double.valueOf(a3);
        Double res = (d1 + d2/60 + d3/60/60);
        return res.toString();
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File("aaa.jpg");
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
