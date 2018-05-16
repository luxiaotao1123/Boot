package com.cool.boot.config;


import com.cool.boot.enums.RabbitTypeEnum;
import com.cool.boot.message.DemoSubscriber;
import com.cool.boot.message.TaskSubscriber;
import com.cool.boot.message.TransferData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * rabbitMq的配置
 * @Auth Vincent
 */
@Configuration
public class RabbitMqConfig {

    private final String DEMO_QUEUE = "queue.fanout.demo";
    private final String DEMO_EXCHANGE = "exchange.fanout.demo";
    private final String DEMO_ROUTINGKEY = "routingKey.fanout.demo";

    private final String TASK_QUEUE = "wuai.queue.fanout.task";
    private final String TASK_EXCHANGE = "wuai.exchange.fanout.task";
    private final String TASK_ROUTINGKEY = "wuai.routingKey.fanout.task";


    @Resource
    private ConnectionFactory factory;

    @Bean("factory")
    public ConnectionFactory createConnectionFactory(@Value("${rabbit.host}") String host, @Value("${rabbit.name}") String name, @Value("${rabbit.pass}") String pass) {

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(name);
        factory.setPassword(pass);
        return factory;

    }

    /*
     *=====================================================demo========================================================
     * */
    @Resource
    private Queue demoQueue;

    @Bean("demoQueue")
    public Queue createTrystQueue() {

        return new Queue(DEMO_QUEUE);
    }

    //交换机
    @Resource
    private FanoutExchange demoExchange;

    @Bean("demoExchange")
    public FanoutExchange createDemoExchange() {

        return new FanoutExchange(DEMO_EXCHANGE, Boolean.TRUE, false);
    }

    //绑定
    @Bean
    public Binding createDemoBind() {

        return BindingBuilder.bind(demoQueue).to(demoExchange);
    }

    //监听
    @Resource
    private MessageListenerAdapter demoAdaptor;

    @Bean("demoAdaptor")
    public MessageListenerAdapter createDemoAdaptor(DemoSubscriber demoSubscriber) {

        return createAdaptor(demoSubscriber, new TransferData());
    }


    @Bean(name = "demoListener")
    public SimpleMessageListenerContainer createDemoMessageListenerContainer() {

        return createContainer(RabbitTypeEnum.DEMO);
    }


    @Bean("demoRabbitTemplate")
    public RabbitTemplate createDemoRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.DEMO);

    }


    /*
      *=====================================================task========================================================
      * */
    @Resource
    private Queue taskQueue;

    @Bean("taskQueue")
    public Queue createTaskQueue() {

        return new Queue(TASK_QUEUE);
    }

    @Resource
    private FanoutExchange taskExchange;

    @Bean("taskExchange")
    public FanoutExchange createTaskExchange() {

        return new FanoutExchange(TASK_EXCHANGE, true, false);
    }


    @Bean
    public Binding createTaskBind() {

        return BindingBuilder.bind(taskQueue).to(taskExchange);
    }

    @Resource
    private MessageListenerAdapter taskAdaptor;

    @Bean("taskAdaptor")
    public MessageListenerAdapter createTaskAdaptor(TaskSubscriber taskSubscriber) {


        return createAdaptor(taskSubscriber, new TransferData());
    }


    @Bean(name = "taskListener")
    public SimpleMessageListenerContainer createTaskMessageListenerContainer() {
        return createContainer(RabbitTypeEnum.TASK);
    }


    @Bean("taskRabbitTemplate")
    public RabbitTemplate createTaskRabbitTemplate() {

        return createRabbitTemplate(RabbitTypeEnum.TASK);

    }



    private MessageListenerAdapter createAdaptor(Object object, TransferData data) {

        MessageListenerAdapter adapter = new MessageListenerAdapter(object, "subscribe");


        adapter.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {


                return null;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {

                String msg = null;
                try {
                    msg = new String(message.getBody(), "utf-8");
                } catch (UnsupportedEncodingException e) {

                }
                if (StringUtils.isEmpty(msg)) {
                    return null;
                }
                data.setData(msg);
                return data;
            }
        });
        return adapter;
    }

    private SimpleMessageListenerContainer createContainer(RabbitTypeEnum RabbitTypeEnum) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        switch (RabbitTypeEnum) {
            case DEMO:
                container.setQueues(demoQueue);
                container.setMessageListener(demoAdaptor);
                break;
            case TASK:
                container.setQueues(taskQueue);
                container.setMessageListener(taskAdaptor);
                break;
            default:
                return container;
        }
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        return container;
    }

    private RabbitTemplate createRabbitTemplate(RabbitTypeEnum RabbitTypeEnum) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(factory);
        switch (RabbitTypeEnum) {
            case DEMO:
                rabbitTemplate.setExchange(DEMO_EXCHANGE);
                rabbitTemplate.setQueue(DEMO_QUEUE);
                rabbitTemplate.setRoutingKey(DEMO_ROUTINGKEY);
                break;
            case TASK:
                rabbitTemplate.setExchange(TASK_EXCHANGE);
                rabbitTemplate.setQueue(TASK_QUEUE);
                rabbitTemplate.setRoutingKey(TASK_ROUTINGKEY);
                break;
            default:
                return rabbitTemplate;
        }


        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.afterPropertiesSet();
        return rabbitTemplate;
    }


}
