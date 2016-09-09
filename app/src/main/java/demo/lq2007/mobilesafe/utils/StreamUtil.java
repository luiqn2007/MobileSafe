package demo.lq2007.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by lq200 on 2016/9/9.
 */
public class StreamUtil {

    /**
     * 将流转化为字符串
     * @param is 要转的流
     * @return 转换成的字符串
     */
    public static String parserStream(InputStream is) throws IOException {
        // 验空
        if(is == null){
            return null;
        }
        // 打开流
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringWriter sw = new StringWriter();
        // 接受
        String str;
        while((str = br.readLine()) != null){
            sw.write(str);
        }
        // 关闭流
        br.close();
        sw.close();
        // 返回
        return sw.toString();
    }
}
