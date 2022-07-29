package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {
    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();
        Enhancer enhancer = new Enhancer();

        //프록시가 상속받을(실제객체) 구체클래스
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));

        //동적으로 ConcreteService를 상속받은 프록시가 생성된다.
        //따라서 생성된 프록시는 ConcreteService로 형변환이 가능하다.
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
}
