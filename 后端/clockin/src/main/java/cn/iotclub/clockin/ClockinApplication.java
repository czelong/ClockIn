package cn.iotclub.clockin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling//开启基于注解的定时任务
@MapperScan("cn.iotclub.clockin.mapper")//开启mapper接口扫描
@SpringBootApplication
public class ClockinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClockinApplication.class, args);
    }

}
