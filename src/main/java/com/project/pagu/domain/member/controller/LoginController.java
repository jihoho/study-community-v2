package com.project.pagu.domain.member.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by yhh1056
 * Date: 2021/06/01 Time: 10:50 오후
 */

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Principal principal) {
        if (principal != null) {
            return "redirect:/error";
        }

        String referrer = request.getHeader("Referer");

        if (referrer != null && referrer.contains("/login")) {
            request.getSession().setAttribute("prevPage", referrer);
        }

        return "login";
    }

    /** 다른 소셜로그인 추가시 커스텀 로그인 페이지 */
//    private static final String authorizationRequestBaseUri = "oauth2/authorization";
//    private final Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
//    private final ClientRegistrationRepository clientRegistrationRepository;

//    @SuppressWarnings("unchecked")
//    @GetMapping("/auth-login")
//    public String getLoginPage(Model model) throws Exception {
//        Iterable<ClientRegistration> clientRegistrations = null;
//        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
//                .as(Iterable.class);
//        if (type != ResolvableType.NONE &&
//                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
//            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
//        }
//        assert clientRegistrations != null;
//        clientRegistrations.forEach(registration ->
//                oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
//        model.addAttribute("urls", oauth2AuthenticationUrls);
//
//        return "auth/oauth-login";
//    }

}
