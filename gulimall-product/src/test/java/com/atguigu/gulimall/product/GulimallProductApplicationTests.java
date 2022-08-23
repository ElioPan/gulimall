package com.atguigu.gulimall.product;

//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.service.SpuInfoDescService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {
    @Resource
    SpuInfoDescService spuInfoDescService;
    @Test
    public void testSaveSpuDesc(){
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(19L);
        spuInfoDescEntity.setDecript("测试>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        spuInfoDescService.save(spuInfoDescEntity);
    }

    @Resource
    BrandService brandService;
//    @Resource
//    OSSClient ossClient;
    @Resource
    CategoryService categoryService;
    @Test
    public void getParentPathTest(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
       log.info("数据是:{}", Arrays.asList(catelogPath));

    }
    @Test
    public void aliyunTest2(){
        InputStream filePath = null;
        try {
            filePath = new FileInputStream("C:\\Users\\miniMon\\Desktop\\test\\greenSea.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        ossClient.putObject("gulimall-elio","sea2.jpg",filePath);
    }

    @Test
    public void aliyunTest() {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tP1JzVPNPsHwTBmFUr4";
        String accessKeySecret = "mlJHOlMmOw9HWCNsh9DSo4QIDkRqDR";

// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 简单上传
        String bucketName = "gulimall-elio";
        String filePath = "C:\\Users\\miniMon\\Desktop\\test\\sea.jpg";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 创建PutObject请求。
//        ossClient.putObject(bucketName, "sea-online.jpg", inputStream);

// 关闭OSSClient。
//        ossClient.shutdown();
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(2L);
        brandEntity.setLogo("EYO");
        brandEntity.setName("T1");
        brandService.save(brandEntity);
    }

    @Test
    void test1() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setLogo("YXY");
        brandEntity.setDescript("test");
        brandService.updateById(brandEntity);
    }

    @Test
    void test2() {
        List<BrandEntity> brand_id = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        brand_id.forEach(System.out::println);
    }

}
