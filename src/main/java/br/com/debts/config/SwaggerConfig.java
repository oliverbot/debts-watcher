package br.com.debts.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ConditionalOnClass(Docket.class)
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				   .title("Template Service")
				   .description("Serviço de Provas para o Gabarito CERS")
				   .license("")
				   .termsOfServiceUrl("http://www.cers.com.br")
				   .version(getVersion())
				   .build();
	}
	
	@Bean
	public Docket templateApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				   .groupName("template-api")
				   .select()
				   .apis(RequestHandlerSelectors.basePackage("br.com.debts.controller"))
				   .build()
				   .apiInfo(apiInfo());
	}
	
	private String getVersion() {

        String resourceName = "application.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        String version = Strings.EMPTY;
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
            version = props.getProperty("configuration.version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

}
