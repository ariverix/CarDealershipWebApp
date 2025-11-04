package org.example.cardealershiprest.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class LoggingAndTracingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingAndTracingFilter.class);
    private static final String HEADER = "X-Request-ID";
    private static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String id = request.getHeader(HEADER);
        if (!StringUtils.hasText(id)) id = UUID.randomUUID().toString();

        MDC.put(MDC_KEY, id);
        response.setHeader(HEADER, id);

        long start = System.currentTimeMillis();
        try {
            if (request.getRequestURI().startsWith("/api/")) {
                log.info("Request started: {} {}", request.getMethod(), request.getRequestURI());
            }

            chain.doFilter(request, response);

        } finally {
            long dur = System.currentTimeMillis() - start;
            if (request.getRequestURI().startsWith("/api/")) {
                log.info("Request finished: {} {} -> {} ({} ms)",
                        request.getMethod(), request.getRequestURI(), response.getStatus(), dur);
            }
            MDC.remove(MDC_KEY);
        }
    }
}
