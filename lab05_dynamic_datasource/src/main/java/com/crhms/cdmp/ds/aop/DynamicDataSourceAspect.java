package com.crhms.cdmp.ds.aop;

import com.crhms.cdmp.ds.manager.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author wxg
 * @since 2025/2/5
 */
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(ds)")
    public void dataSourcePointCut(DS ds) {
    }

    @Around("dataSourcePointCut(ds)")
    public Object around(ProceedingJoinPoint point, DS ds) throws Throwable {
        DynamicDataSourceContextHolder.setDataSourceKey(ds.value());
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }
}

