/*
 * Copyright 2002-2016 XianYu Game Co. Ltd, The Inuyasha Project
 */

package tank.event.common;

import com.google.common.eventbus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import tank.common.ThreadPoolManager;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件发布器，用于派发同步或异步事件
 *
 * @version 1.0
 */
@Component
public class EventPublisher implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);
    private static final SubscriberExceptionHandler exceptionHandler = new LoggingSubscriberExceptionHandler();

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;

    private ApplicationContext context;


    @PostConstruct
    public void init() {
        eventBus = new EventBus(exceptionHandler);
        asyncEventBus = new AsyncEventBus(ThreadPoolManager.getExecutorService(), exceptionHandler);

        Collection<Object> subscribers = getSubscriberBeans();

        // 注册所有的“事件处理订阅者”
        for (Object subscriber : subscribers) {
            eventBus.register(subscriber);
            asyncEventBus.register(subscriber);
        }

        eventBus.register(new Object() {
            @Subscribe
            public void lister(DeadEvent event) {
                LOGGER.warn("事件没有任何监听:{},{}", event.getSource().getClass(), event.getEvent());
            }
        });
    }

    private Collection<Object> getSubscriberBeans() {
        Map<String, Object> beansMap = new HashMap<>();

        Map<String, Object> map = context.getBeansWithAnnotation(Service.class);
        if (map != null && !map.isEmpty()) {
            beansMap.putAll(map);
        }

        map = context.getBeansWithAnnotation(Controller.class);
        if (map != null && !map.isEmpty()) {
            beansMap.putAll(map);
        }

        map = context.getBeansWithAnnotation(Component.class);
        if (map != null && !map.isEmpty()) {
            beansMap.putAll(map);
        }

        return beansMap.values();
    }

    public void fireEvent(Object event) {
        LOGGER.debug("正在触发**同步**事件：{}", event.getClass().getSimpleName());
        eventBus.post(event);
    }

    public void fireAsyncEvent(Object event) {
        fireAsyncEvent(event, true);
    }

    public void fireAsyncEvent(final Object event, boolean keepInSameThread) {

        LOGGER.debug("正在触发**异步**事件：{}", event.getClass().getSimpleName());
        if (keepInSameThread) {
            ThreadPoolManager.getExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    eventBus.post(event);
                }
            });
        } else {
            asyncEventBus.post(event);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private static final class LoggingSubscriberExceptionHandler implements SubscriberExceptionHandler {
        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            LOGGER.error("处理事件{}时发生了异常：", context.getEvent().getClass().getSimpleName(), exception);
        }
    }
}
