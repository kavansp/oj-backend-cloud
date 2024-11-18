package chan.project.ojbackendquestion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("chan.project.ojbackendquestion.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableFeignClients(basePackages = {"chan.project.ojbackendservice"})
@ComponentScan("chan.project")
public class OjBackendQuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendQuestionApplication.class, args);
    }

}
