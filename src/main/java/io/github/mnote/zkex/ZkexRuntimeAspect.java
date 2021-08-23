package io.github.mnote.zkex;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.WebApp;

import java.util.Date;

@Aspect
public class ZkexRuntimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ZkexRuntimeAspect.class);

    //private long uptime = (new Date()).getTime() + (long) (3600000 * (72 / Calendar.getInstance().get(7)));

    private long uptime = (new Date()).getTime() +  365 * 24 * 60 * 60 * 1000L;

    @Pointcut(value="execution(static boolean org.zkoss.zkex.rt.Runtime.init(org.zkoss.zk.ui.WebApp, boolean)) && args(webApp,flag)", argNames="webApp,flag")
    public void pointcut1(WebApp webApp, boolean flag){}

    @Pointcut(value="execution(static java.lang.Object org.zkoss.zkex.rt.Runtime.init(java.lang.Object)) && args(obj)", argNames="obj")
    public void pointcut2(Object obj){}


    @Around("pointcut1(webApp,flag)")
    public boolean init(ProceedingJoinPoint joinPoint, WebApp webApp, boolean flag) throws Throwable {

        Long uptime = Reflect.on("org.zkoss.zkex.rt.Runtime").get("_uptime");

        Reflect.on("org.zkoss.zkex.rt.Runtime").set("_uptime", this.uptime);

        Object result = joinPoint.proceed();

        if(logger.isInfoEnabled()) {
            logger.info("caller: " + getCaller(3) + ", flag: " + flag + ", result: " + result + ", uptime: " + uptime + ", " + (uptime != null ? new Date(uptime) : ""));
        }

       // webApp.setAttribute(EVAL_ONLY, Boolean.FALSE);

        webApp.removeAttribute("org.zkoss.zk.ui.notice");

        Reflect.on("org.zkoss.zkex.rt.Runtime").set("_uptime", this.uptime);

        return true;
    }

    @Around("pointcut2(obj)")
    public Object init(ProceedingJoinPoint joinPoint, Object obj) throws Throwable {

        Long uptime = Reflect.on("org.zkoss.zkex.rt.Runtime").get("_uptime");

        Reflect.on("org.zkoss.zkex.rt.Runtime").set("_uptime", this.uptime);

        Object result = joinPoint.proceed();

        if(logger.isDebugEnabled()) {
            logger.debug("caller: " + getCaller(3) + ", result: " + result + ", uptime: " + uptime + ", " + (uptime != null ? new Date(uptime) : ""));
        }

        return result;
    }

    /** Utility method to get StackTraceElement of caller */
    private static StackTraceElement getCaller(int levels) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[levels + 1];
    }
}
