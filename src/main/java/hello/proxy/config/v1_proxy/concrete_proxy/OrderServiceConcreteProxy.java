package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        //프록시 용도이기 때문에 부모를 사용하는것이 아닌 target을 사용한다.
        //따라서 부모클래스의 생성자 파라미터에 문법상 어쩔수없이 null을 넣어준다.
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.request()");

            //target
            target.orderItem(itemId);

            logTrace.end(status);
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
