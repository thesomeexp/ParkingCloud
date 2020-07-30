import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FilterPathTest {

    @Test
    public void FilterListTest(){
        List<String> ls = getFilterUriList();
        for (String str : ls){
            System.out.println(str);
        }
    }


    private List<String> getFilterUriList() {
        String[] arryUri = new String[]{"/login", "/register"};
        return Arrays.asList(arryUri);
    }


}
