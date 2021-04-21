package com.project.pagu.member.mockMember;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * Created by IntelliJ IDEA
 * User: yhh1056@naver.com
 * Date: 2021/04/21 Time: 12:21 오후
 */

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMemberSecurityContextFactory.class)
public @interface WithMember {

    String value() default "tester";
}
