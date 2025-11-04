package org.example.cardealershiprest.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class PerformanceWarningFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(PerformanceWarningFilter.class);
    private static final long THRESHOLD_MS = 20;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        long start = System.currentTimeMillis();
        chain.doFilter(req, res);
        long dur = System.currentTimeMillis() - start;

        if (dur > THRESHOLD_MS && request.getRequestURI().startsWith("/api/")) {
            log.warn("Slow request detected: {} {} took {} ms",
                    request.getMethod(), request.getRequestURI(), dur);
        }
    }
}
