package com.atguigu.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }
    @Resource
    OSSClient ossClient;
    @Test
    public void aliyunTest2(){
        InputStream filePath = null;
        try {
            filePath = new FileInputStream("C:\\Users\\miniMon\\Desktop\\test\\greenSea.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject("gulimall-elio","sea3.jpg",filePath);
    }

}
