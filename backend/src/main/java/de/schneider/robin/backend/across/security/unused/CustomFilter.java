//package de.schneider.robin.backend.across.security.unused;
//
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CustomFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse resp = (HttpServletResponse) response;
//        String cookie = resp.getHeader("Set-Cookie");
//        if (cookie != null) {
//            resp.setHeader("Set-Cookie", cookie + "; SameSite=strict");
//        }
//        chain.doFilter(request, response);
//    }
//}
