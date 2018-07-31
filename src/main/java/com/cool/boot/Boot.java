package com.cool.boot;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.*;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class Boot {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Boot.class, args);
        CoolBean bean = (CoolBean) context.getBean("cool");
        bean.cool();
    }















    class CoolEvent extends ApplicationEvent {

        private String msg;
        /**
         * Create a new ApplicationEvent.
         *
         * @param source the object on which the event initially occurred (never {@code null})
         */
        public CoolEvent(Object source, String msg) {
            super(source);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }




    @Component
    class CoolListener implements ApplicationListener<ApplicationEvent> {


        @Override
        public void onApplicationEvent(ApplicationEvent event) {

            if (!(event instanceof CoolEvent)){
                return;
            }

            String msg = ((CoolEvent) event).getMsg();

            System.out.println(msg);

        }
    }




    @Component("cool")
    class CoolBean implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

            this.applicationContext = applicationContext;
        }

        public void cool(){

            CoolEvent event = new CoolEvent(applicationContext, "OK!!!");

            applicationContext.publishEvent(event);
        }

    }
}
