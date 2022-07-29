package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

//        Object result = method.invoke(target, args);
        Object result = invocation.proceed();   //프록시 팩토리를 생성하는단계에서 이미 target과 args정보를 전달받는다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime-startTime;
        log.info("TimeProxy 종료 resultTime={}",resultTime);
        return result;
    }
}
