package io.github.mnote.zkcharts;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.WebApp;

@Aspect
@SuppressWarnings("SpringAopErrorsInspection")
public class ZkchartsRuntimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ZkchartsRuntimeAspect.class);

    @Pointcut(value="execution(static boolean org.zkoss.chart.rt.Runtime.init(org.zkoss.zk.ui.WebApp, boolean)) && args(webApp,flag)", argNames="webApp,flag")
    public void pointcut(WebApp webApp, boolean flag){}

    @Around("pointcut(webApp,flag)")
    public boolean init(ProceedingJoinPoint joinPoint, WebApp webApp, boolean flag) throws Throwable {

        Object result = joinPoint.proceed();

        //NOTICE: org.zkoss.zk.ui.notice
        String ZK_NOTICE = Reflect.onClass("org.zkoss.chart.rt.Runtime").get("ZK_NOTICE");

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3) + ", callee: " + webApp + ", flag: " + flag + ", result: " + result + ", ZK_NOTICE: " + ZK_NOTICE);
        }

        webApp.setAttribute(ZK_NOTICE, null);

        return true;
    }

    /**
     * Here we log internal calls to Init1.init2() inside the API, and measure the time that those calls take.
     * The instance of the Init1 inner class is included.
     * @param proceedingJoinPoint
     * @param init
     * @return
     * @throws Throwable
     */
    @Around("call(* org.zkoss.chart.rt.Runtime$Init1.init2(..)) && within(org.zkoss.chart.rt.*) && target(init)")
    public Object internalCall(ProceedingJoinPoint proceedingJoinPoint, Object init) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    /**
     * Here we add some logging for when the third party library calls the start() method on java.lang.Thread,
     * and include the name and type of the started thread.
     * @param thread
     */
    @Before("call(* java.lang.Thread.start(..)) && within(org.zkoss.chart.rt.*) && target(thread)")
    public void threadStarted(Thread thread) {
        logger.warn("caller: " + getCaller(2) + " on thread named " + thread.getName() + " of type " + thread.getClass().getName());
    }

    @Around("execution(boolean org.zkoss.chart.rt.Runtime$Init1.validate(org.zkoss.zk.ui.event.Event)) || execution(boolean org.zkoss.zssex.rt.Runtime$Init1.validate(org.zkoss.zk.ui.event.Event)) ")
    public boolean validate(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

        return true;
    }


    @Around("execution(void org.zkoss.chart.rt.Runtime$Init1.complainLicense(org.zkoss.zk.ui.event.Event)) ")
    public void complainLicense(ProceedingJoinPoint joinPoint) throws Throwable {

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3));
        }

    }

    @Around("execution(void org.zkoss.chart.rt.Runtime$Init1.complainUptime(org.zkoss.zk.ui.event.Event)) ")
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
