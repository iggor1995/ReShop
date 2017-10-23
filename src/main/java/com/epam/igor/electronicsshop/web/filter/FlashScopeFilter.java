package com.epam.igor.electronicsshop.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Class-filter for work with specific scope attribute that can live through redirect.
 *
 * @author Igor Lapin
 */

public class FlashScopeFilter implements Filter {
    private static final String FLASH_SESSION_KEY = "FLASH_SESSION_KEY";
    private static final String FLASH = "flash.";

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                Map<String, Object> flashParams = (Map<String, Object>)
                        session.getAttribute(FLASH_SESSION_KEY);
                if (flashParams != null) {
                    removeAttributes(flashParams, session, (HttpServletRequest) request);
                }
            }
        }
        chain.doFilter(request, response);

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Map<String, Object> flashParams = new HashMap();
            Enumeration e = httpRequest.getAttributeNames();
            while (e.hasMoreElements()) {
                String paramName = (String) e.nextElement();
                if (paramName.startsWith(FLASH)) {
                    Object value = request.getAttribute(paramName);
                    paramName = paramName.substring(6, paramName.length());
                    flashParams.put(paramName, value);
                }
            }
            if (flashParams.size() > 0) {
                HttpSession session = httpRequest.getSession(false);
                session.setAttribute(FLASH_SESSION_KEY, flashParams);
            }
        }
    }

    private void removeAttributes( Map<String, Object> flashParams, HttpSession session, HttpServletRequest request){
        for (Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
            request.setAttribute(flashEntry.getKey(), flashEntry.getValue());
        }
        session.removeAttribute(FLASH_SESSION_KEY);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        //no-op
    }

    public void destroy() {
        //no-op
    }
}
