package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

//따로 설정하지않는이상 인터페이스 기반이면 JDK동적프록시, 구체클래스 기반이면 CGLIB를 사용한다.
@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();

        //프록시 팩토시 생성자에 파라미터로 넘겨준 인스턴스 정보를 기반으로 동적 프록시를 생성한다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}",target.getClass());
        log.info("proxyClass={}",proxy.getClass());

        proxy.save();

        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }
    @Test
    @DisplayName("구체클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();

        //프록시 팩토시 생성자에 파라미터로 넘겨준 인스턴스 정보를 기반으로 동적 프록시를 생성한다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass={}",target.getClass());
        log.info("proxyClass={}",proxy.getClass());

        proxy.call();

        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("proxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);     //인터페이스 기반이더라도 항상 CGLIB를 사용하여 프록시를 생성한다.(*중요)
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}",target.getClass());
        log.info("proxyClass={}",proxy.getClass());

        proxy.save();

        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
