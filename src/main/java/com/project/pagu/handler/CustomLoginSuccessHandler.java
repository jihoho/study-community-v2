package com.project.pagu.handler;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-10 Time: 오전 12:06
 */
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {


        // 디폴트 URI
        String uri = "/";

        /* 강제 인터셉트 당했을 경우의 데이터 get */
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        /* 로그인 버튼 눌러 접속했을 경우의 데이터 get */
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        // null이 아니라면 강제 인터셉트 당했다는 것
        if (savedRequest != null) {
            uri = savedRequest.getRedirectUrl();

            // ""가 아니라면 직접 로그인 페이지로 접속한 것
        } else if (prevPage != null && !prevPage.equals("")) {
            uri = prevPage;
        }

        // 세 가지 케이스에 따른 URI 주소로 리다이렉트
        response.sendRedirect(uri);

    }
}
