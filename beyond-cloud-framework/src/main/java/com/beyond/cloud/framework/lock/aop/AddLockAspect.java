package com.beyond.cloud.framework.lock.aop;

import com.beyond.cloud.framework.lock.DistributedLock;
import com.beyond.cloud.framework.lock.annotation.Lock;
import com.beyond.cloud.framework.utils.SpelUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AddLockAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DistributedLock distributedLock;

    public AddLockAspect(final DistributedLock distributedLock) {this.distributedLock = distributedLock;}


    @Pointcut("@annotation(com.beyond.cloud.framework.lock.annotation.Lock)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object addKeyMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        //定义返回值
        Object proceed;
        //获取方法名称
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Lock lock = AnnotationUtils.findAnnotation(method, Lock.class);
        String logInfo = getLogInfo(joinPoint);
        //前置方法 开始
        String redisKey = getRedisKey(joinPoint);
        logger.info("{}获取锁={}", logInfo, redisKey);
        // 获取请求ID;保证 加锁者 和释放者 是同一个。
        String requestId = java.util.UUID.randomUUID().toString();
        boolean lockReleased = false;
        try {
            boolean locked = distributedLock.lock(requestId, redisKey, lock.expire());
            if (!locked) {
                throw new RuntimeException(logInfo + ":加锁失败");
            }
            // 目标方法执行
            proceed = joinPoint.proceed();
            boolean releaseLock = distributedLock.releaseLock(redisKey, requestId);
            lockReleased = true;
            if (releaseLock) {
                throw new RuntimeException(logInfo + ":释放锁失败");
            }
            return proceed;
        } catch (Exception exception) {
            logger.error("{}执行异常,key = {},message={}, exception = {}", logInfo, redisKey, exception.getMessage(), exception);
            throw exception;
        } finally {
            if (!lockReleased) {
                logger.info("{}异常终止释放锁={}", logInfo, redisKey);
                boolean releaseLock = distributedLock.releaseLock(redisKey, requestId);
                logger.info("releaseLock=" + releaseLock);
            }
        }
    }

    /**
     * 获取 指定 log info
     * 需要接口方法声明处 添加 AddLock 注解
     * 并且 需要填写 log info
     *
     * @param joinPoint 切入点
     * @return logInfo
     */
    private String getLogInfo(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Lock annotation = AnnotationUtils.findAnnotation(method, Lock.class);
        if (annotation == null) {
            return methodSignature.getName();
        }
        return annotation.logInfo();
    }

    /**
     * 获取拦截到的请求方法
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        Lock annotation = AnnotationUtils.findAnnotation(targetMethod, Lock.class);
        String spel = null;
        if (annotation != null) {
            spel = annotation.spel();
        }
        return SpelUtil.parse(target, spel, targetMethod, arguments);
    }

}
