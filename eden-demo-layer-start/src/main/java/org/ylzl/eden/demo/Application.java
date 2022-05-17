package org.ylzl.eden.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ylzl.eden.spring.framework.bootstrap.SpringBootApplicationTemplate;
import org.ylzl.eden.spring.framework.cola.catchlog.annotation.EnableCatchLog;
import org.ylzl.eden.spring.framework.error.annotation.EnableRestExceptionResolver;

/**
 * Spring Boot 引导类
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@EnableRestExceptionResolver
@EnableCatchLog
@MapperScan(basePackages = "org.ylzl.eden.demo.dao", annotationClass = Mapper.class)
@EnableDubbo(scanBasePackages = "org.ylzl.eden.demo.service")
@EnableDiscoveryClient
@EnableTransactionManagement
@Slf4j
@SpringBootApplication
public class Application extends SpringBootApplicationTemplate {

	/**
	 * 启动入口
	 *
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		run(Application.class, args, WebApplicationType.SERVLET);
	}
}
