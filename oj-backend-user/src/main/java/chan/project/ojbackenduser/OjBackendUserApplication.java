package chan.project.ojbackenduser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("chan.project.ojbackenduser.mapper")
@EnableFeignClients(basePackages = {"chan.project.ojbackendservice"})
@EnableDiscoveryClient
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("chan.project")
public class OjBackendUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendUserApplication.class, args);
    }

}
