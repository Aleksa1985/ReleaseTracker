package com.lexsoft.releasetracker.tracing.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CorrelationIdFilter implements Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {

            final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String currentCorrId = httpServletRequest.getHeader(RequestCorrelation.CORRELATION_ID);

            if (!currentRequestIsAsyncDispatcher(httpServletRequest)) {
                currentCorrId = Optional.ofNullable(currentCorrId)
                        .orElse(UUID.randomUUID().toString());
                RequestCorrelation.setId(currentCorrId);
            }
            filterChain.doFilter(httpServletRequest, servletResponse);
        }

        private boolean currentRequestIsAsyncDispatcher(HttpServletRequest httpServletRequest) {
            return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
        }
}
