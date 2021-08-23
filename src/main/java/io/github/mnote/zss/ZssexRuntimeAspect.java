package io.github.mnote.zss;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.WebApp;

@Aspect
public class ZssexRuntimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ZssexRuntimeAspect.class);

    @Pointcut(value="execution(static boolean org.zkoss.zssex.rt.Runtime.init(org.zkoss.zk.ui.WebApp, boolean)) && args(webApp,flag)", argNames="webApp,flag")
    public void pointcut(WebApp webApp, boolean flag){}

    @Around("pointcut(webApp,flag)")
    public boolean init(ProceedingJoinPoint joinPoint, WebApp webApp, boolean flag) throws Throwable {

        String ZK_NOTICE = Reflect.onClass("org.zkoss.zssex.rt.Runtime").get("ZK_NOTICE");
        String EVAL_ONLY = Reflect.onClass("org.zkoss.zssex.rt.Runtime").get("EVAL_ONLY");

        logger.info("ZK_NOTICE({}): {}, EVAL_ONLY({}): {}", ZK_NOTICE, webApp.getAttribute(ZK_NOTICE), EVAL_ONLY, webApp.getAttribute(EVAL_ONLY));

        Object result = joinPoint.proceed();

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3) + ", flag: " + flag + ", result: " + result);
        }

        logger.info("ZK_NOTICE({}): {}, EVAL_ONLY({}): {}", ZK_NOTICE, webApp.getAttribute(ZK_NOTICE), EVAL_ONLY, webApp.getAttribute(EVAL_ONLY));

        webApp.setAttribute(ZK_NOTICE, null);
        webApp.setAttribute(EVAL_ONLY, Boolean.FALSE);

        return true;
    }

    @Around("execution(boolean org.zkoss.zssex.rt.Runtime$Init1.validate(org.zkoss.zk.ui.event.Event)) ")
    public boolean validate(ProceedingJoinPoint joinPoint) throws Throwable {

        //"callee: " + joinPoint.getThis()
        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

        return true;
    }

    @Around("execution(boolean org.zkoss.zssex.rt.Runtime$Init1.validUptime(org.zkoss.zk.ui.event.Event)) ")
    public boolean validateUptime(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

        return true;
    }

    @Around("execution(void org.zkoss.zssex.rt.Runtime$Init1.showWin(..)) ")
    public void showWin(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

    }

    @Around("execution(void org.zkoss.zssex.rt.Runtime$Init1.maskSheetSession(..)) ")
    public void maskSheetSession(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

    }

    @Around("execution(void org.zkoss.zssex.rt.Runtime$Init1.complainLicense(org.zkoss.zk.ui.event.Event)) ")
    public void complainLicense(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

    }

    @Around("execution(void org.zkoss.zssex.rt.Runtime$Init1.complainUptime(org.zkoss.zk.ui.event.Event)) ")
    public void complainUptime(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

    }

    /** Utility method to get StackTraceElement of caller */
    private static StackTraceElement getCaller(int levels) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[levels + 1];
    }
}
