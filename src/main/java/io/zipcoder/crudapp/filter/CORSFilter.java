package io.zipcoder.crudapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter extends OncePerRequestFilter {

    private final Logger LOG = LoggerFactory.getLogger((CORSFilter.class));

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    LOG.info("Adding CORS Headers ........................");
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
    httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
