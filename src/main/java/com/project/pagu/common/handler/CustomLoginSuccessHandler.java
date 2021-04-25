package com.project.pagu.common.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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

        HttpSession session = request.getSession();
        if (session == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        String redirectUrl = (String) session.getAttribute("prevPage");
        if (redirectUrl == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        session.removeAttribute("prevPage");
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}
