package com.project.pagu.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-08 Time: 오후 9:55
 */
@Configuration
public class SecurityMessageConfig {
    @Bean
    public MessageSource messageSource(){
        Locale.setDefault(Locale.KOREA);
        Locale.setDefault(Locale.KOREA); // 위치 한국으로 설정
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setDefaultEncoding("UTF-8"); // 인코딩 설정
        messageSource.setBasenames("classpath:message/security_message", "classpath:org/springframework/security/messages"); // 커스텀한 properties 파일, security properties 파일 순서대로 설정
        return messageSource;
    }
}
