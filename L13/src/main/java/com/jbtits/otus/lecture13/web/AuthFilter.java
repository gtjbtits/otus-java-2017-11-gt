package com.jbtits.otus.lecture13.web;

import com.jbtits.otus.lecture13.dbService.DBService;
import com.jbtits.otus.lecture13.services.AuthServiceSingleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthFilter implements Filter {
    private final AuthService authService = AuthServiceSingleton.get();
    private static final String AUTHENTICATION_HEADER = "Authorization";

    private Credentials parseCredentials(String basicAuthHeader) {
        if (basicAuthHeader == null) {
            return null;
        }
        Credentials credentials = new Credentials();
        String decoded = new String(Base64.getDecoder().decode(basicAuthHeader.substring(6)), StandardCharsets.UTF_8);
        String parts[] = decoded.split(":");
        if (parts.length != 2) {
            return null;
        }
        credentials.login = parts[0];
        credentials.password = parts[1];
        return credentials;
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
    public void init(FilterConfig arg0) throws ServletException {
    }

    class Credentials {
        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "Credentials{" +
                    "login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
