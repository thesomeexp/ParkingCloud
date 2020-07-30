import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.github.pagehelper.util.StringUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class EXIFTest {

    @Test
    public void testFile() throws ImageProcessingException, IOException {
//        System.out.println("test");
//        File f = new File("C:\\Users\\i\\Pictures\\ParkingCLoud\\aaa.txt");
//        System.out.println(f.length());
        File f = new File("C:\\Users\\i\\Pictures\\ParkingCLoud\\IMG_0312.jpg");
        String[] sa = this.readExif(f);
        System.out.println(sa[0] + " " + sa[1] + " " + sa[2]);

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

                            System.out.println("----------" + Dms2D(array[0]));
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
        return array;
    }

    @Test
    public void Testttt() {

//        String a = "23° 9' 20.33\"";
        String a = "113° 29' 37.97\"";
        String[] array = a.split("[°]");
        String a1 = array[0].trim();
        System.out.println(array[0]);
        String w222 = array[1];
        String degrees=array[0];//得到度
        String[] array2 = w222.split("[']");
        String a2 = array2[0].trim();
        System.out.println(array2[0].trim());
        String w333 = array2[1];
        String[] array3 = w333.split("[\"]");
        String a3 = array3[0].trim();
        System.out.println(array3[0].trim());

        Double d1 = Double.valueOf(a1);
        Double d2 = Double.valueOf(a2);
        Double d3 = Double.valueOf(a3);

        System.out.println(d1 + d2/60 + d3/60/60);


    }

    public static String Dms2D(String jwd){
        if(StringUtil.isNotEmpty(jwd)&&(jwd.contains("°"))){//如果不为空并且存在度单位
            //计算前进行数据处理
            jwd = jwd.replace("E", "").replace("N", "").replace(":", "").replace("：", "");
            double d=0,m=0,s=0;
            d = Double.parseDouble(jwd.split("°")[0]);
            //不同单位的分，可扩展
            if(jwd.contains("′")){//正常的′
                m = Double.parseDouble(jwd.split("°")[1].split("′")[0]);
            }else if(jwd.contains("'")){//特殊的'
                m = Double.parseDouble(jwd.split("°")[1].split("'")[0]);
            }
            //不同单位的秒，可扩展
            if(jwd.contains("″")){//正常的″
                //有时候没有分 如：112°10.25″
                s = jwd.contains("′")?Double.parseDouble(jwd.split("′")[1].split("″")[0]):Double.parseDouble(jwd.split("°")[1].split("″")[0]);
            }else if(jwd.contains("''")){//特殊的''
                //有时候没有分 如：112°10.25''
                s = jwd.contains("'")?Double.parseDouble(jwd.split("'")[1].split("''")[0]):Double.parseDouble(jwd.split("°")[1].split("''")[0]);
            }
            jwd = String.valueOf(d+m/60+s/60/60);//计算并转换为string
            //使用BigDecimal进行加减乘除
	/*BigDecimal bd = new BigDecimal("60");
	BigDecimal d = new BigDecimal(jwd.contains("°")?jwd.split("°")[0]:"0");
	BigDecimal m = new BigDecimal(jwd.contains("′")?jwd.split("°")[1].split("′")[0]:"0");
	BigDecimal s = new BigDecimal(jwd.contains("″")?jwd.split("′")[1].split("″")[0]:"0");
	//divide相除可能会报错（无限循环小数），要设置保留小数点
	jwd = String.valueOf(d.add(m.divide(bd,6,BigDecimal.ROUND_HALF_UP)
            .add(s.divide(bd.multiply(bd),6,BigDecimal.ROUND_HALF_UP))));*/
        }
        return jwd;
    }


}
