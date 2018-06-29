package com.cool.boot.config;

import com.cool.boot.enums.ExecutorsEnum;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vincent
 * <p>
 * 总线程池
 */
@Component("executors")
@Order(0)
public class ExecutorsConfig implements DisposableBean {

    @Value("${application.name}")
    private String applicationName;

    private static final EnumSet<ExecutorsEnum> allCategory = EnumSet.allOf(ExecutorsEnum.class);

    private static AtomicInteger count = new AtomicInteger();

    private EnumMap<ExecutorsEnum, ExecutorService> executors;


    public ExecutorService getSingle(ExecutorsEnum executorsEnum) {

        if (executors == null) {

            executors = new EnumMap<>(ExecutorsEnum.class);
            for (ExecutorsEnum param : allCategory) {
                executors.put(param, java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
                    Thread thread = new Thread(r);
                    thread.setName(applicationName + count.getAndIncrement());
                    if (thread.isDaemon()) {
                        thread.setDaemon(Boolean.FALSE);
                    }

                    thread.setPriority(Thread.NORM_PRIORITY);

                    return thread;
                }));
            }
        }

        return executors.get(executorsEnum);
    }


    @Override
    public void destroy() {

        if (executors != null) {
            for (ExecutorService executorService : executors.values()) {
                executorService.shutdown();
            }
        }
    }
}
