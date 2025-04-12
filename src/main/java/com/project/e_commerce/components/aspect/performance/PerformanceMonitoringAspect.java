package com.project.e_commerce.components.aspect.performance;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final long SLOW_EXECUTION_THRESHOLD_MS = 500;


    @Pointcut("within(com.project.e_commerce.services..*)")
    public void serviceMethods() {}

    @Pointcut("within(com.project.e_commerce.repositories..*)")
    public void repositoryMethods() {}

    @Pointcut("serviceMethods() || repositoryMethods()")
    public void monitoredMethods() {}

    @Around("monitoredMethods()")
    public Object logMethodPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getName() + "." + signature.getName();

        // Prepare log data
        Map<String, Object> logData = new HashMap<>();
        logData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        logData.put("method", methodName);


        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        if (paramNames != null && paramValues != null && paramNames.length > 0) {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];

                // Sanitize sensitive data
                if (paramName.toLowerCase().contains("password") ||
                        paramName.toLowerCase().contains("token") ||
                        paramName.toLowerCase().contains("secret")) {
                    params.put(paramName, "******");
                } else {
                    // For complex objects, just log the class name to avoid excessive logging
                    if (paramValue != null && !isPrimitiveOrString(paramValue.getClass())) {
                        params.put(paramName, paramValue.getClass().getSimpleName());
                    } else {
                        params.put(paramName, paramValue);
                    }
                }
            }
            logData.put("parameters", params);
        }

        // Execute method and measure performance
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            logData.put("executionTimeMs", executionTime);

            // Log at appropriate level based on execution time
            String logMessage = "Method Performance: " + objectMapper.writeValueAsString(logData);

            if (executionTime > SLOW_EXECUTION_THRESHOLD_MS) {
                logger.warn(logMessage); // Warn for slow methods
            } else {
                logger.debug(logMessage); // Debug for normal methods
            }
        }
    }

    private boolean isPrimitiveOrString(Class<?> type) {
        return type.isPrimitive() || type == String.class ||
                Number.class.isAssignableFrom(type) ||
                Boolean.class == type ||
                Character.class == type;
    }
}
