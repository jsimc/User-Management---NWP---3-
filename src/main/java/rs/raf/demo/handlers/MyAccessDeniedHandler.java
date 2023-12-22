package rs.raf.demo.handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String name
                = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = "User: " + name
                + " attempted to access the protected URL: "
                + request.getRequestURI();
        response.setStatus(403);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}
