package io.github.mnote.zkoss.keikai;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@SuppressWarnings("SpringAopErrorsInspection")
public class KeikaiexRuntimeLicenseManagerAspect {

    private static final Logger logger = LoggerFactory.getLogger(KeikaiexRuntimeLicenseManagerAspect.class);

    @Pointcut(value="execution(void io.keikaiex.rt.RuntimeLicenseManager.startScheduler())")
    public void pointcut1(){}

    @Pointcut(value="execution(void io.keikaiex.rt.RuntimeLicenseManager.check())")
    public void pointcut2(){}


    @Around("pointcut1()")
    public void startScheduler(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3) );
        }

    }

    @Around("pointcut2()")
    public void check(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3) );
        }

    }


    static {
        Reflect.onClass("io.keikaiex.rt.RuntimeLicenseManager").set("HOUR", 365*24*60*60*1000);
    }

    /** Utility method to get StackTraceElement of caller */
    private static StackTraceElement getCaller(int levels) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[levels + 1];
    }
}
