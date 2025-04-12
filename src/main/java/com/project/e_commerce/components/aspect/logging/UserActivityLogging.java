package com.project.e_commerce.components.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;



@Component
@Aspect
public class UserActivityLogging {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserActivityLogging.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    //named pointcut
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods() && execution(* com.project.e_commerce.controllers.AuthController.*(..))")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Map<String, Object> logData = new HashMap<>();

        logData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        logData.put("httpMethod", request.getMethod());
        logData.put("endpoint", request.getRequestURI());
        logData.put("clientIp", request.getRemoteAddr());


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            logData.put("userId", authentication.getName());
        } else {
            logData.put("userId", "anonymous");
        }

        // Log before method execution
        logger.info("API Request: {}",objectMapper.writeValueAsString(logData));


        // Execute the method
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            // Log after method execution
            long executionTime = System.currentTimeMillis() - startTime;
            logData.put("executionTimeMs", executionTime);

            // Add response status if available
            if (result instanceof ResponseEntity) {
                logData.put("responseStatus", ((ResponseEntity<?>) result).getStatusCodeValue());
            }

            logger.info("API Response: {}", objectMapper.writeValueAsString(logData));
        }
    }
}
