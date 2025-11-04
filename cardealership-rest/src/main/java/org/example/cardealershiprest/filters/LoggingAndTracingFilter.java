package org.example.cardealershiprest.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Adds a correlation identifier to REST requests and writes concise begin/end logs for tracing.
 * The filter is only executed for resources under the /api/ prefix to avoid polluting other endpoints.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingAndTracingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingAndTracingFilter.class);
    private static final String HEADER = "X-Request-ID";
    private static final String MDC_KEY = "correlationId";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String id = request.getHeader(HEADER);
        if (!StringUtils.hasText(id)) {
            id = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, id);
        response.setHeader(HEADER, id);

        long start = System.currentTimeMillis();
        try {
            log.info("Request started: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Request finished: {} {} -> {} ({} ms)",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
            MDC.remove(MDC_KEY);
        }
    }
}
