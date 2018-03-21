package com.jbtits.otus.lecture13.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private AuthService authService;

    private static final String AUTHENTICATION_HEADER = "Authorization";

    private Credentials parseCredentials(String basicAuthHeader) {
        if (basicAuthHeader == null) {
            return null;
        }
        String decoded = new String(Base64.getDecoder().decode(basicAuthHeader.substring(6)), StandardCharsets.UTF_8);
        String parts[] = decoded.split(":");
        if (parts.length != 2) {
            return null;
        }
        return new Credentials(parts[0], parts[1]);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
        throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            filter.doFilter(request, response);
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            Credentials credentials = parseCredentials(httpRequest.getHeader(AUTHENTICATION_HEADER));
            if (authService.authorize(credentials)) {
                filter.doFilter(request, response);
            } else {
                httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"" + "Jetty realm" + "\"");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }
}
