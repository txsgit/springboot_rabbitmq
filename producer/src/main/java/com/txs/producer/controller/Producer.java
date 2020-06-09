package com.txs.producer.controller;

import com.txs.entity.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息并确认返回
 */
@RestController
public class Producer {

    @Autowired
    RabbitTemplate rabbitTemplate;

//    final成员变量表示常量，只能被赋值一次，赋值后值不再改变。
//    当final修饰一个基本数据类型时，表示该基本数据类型的值一旦在初始化后便不能发生变化；如果final修饰一个引用类型时，
//    则在对其初始化之后便不能再让其指向其他对象了，但该引用所指向的对象的内容是可以发生变化的。
//    本质上是一回事，因为引用的值是一个地址，final要求值，即地址的值不发生变化。
    //定义确认消息送达
    final RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData:" + correlationData);
            System.out.println("ack:" + ack);
            //ack为false说明异常
            if (!ack) {
                System.out.println("异常处理");
            }
        }
    };
    //确认消息返回
    final RabbitTemplate.ReturnCallback returnCallback= new RabbitTemplate.ReturnCallback(){

        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            //找不到就返回发送的消息
            System.out.println("return message: "+message);
            System.out.println("return replyCode: "+replyCode);
            System.out.println("return replyText: "+replyText);
            System.out.println("return exchange: "+exchange);
            System.out.println("return routingKey: "+routingKey);
        }
    };


    @RequestMapping("/send/{msg}")
    public Object send(@PathVariable String msg){

     //添加确认消息返回监听,用来监听消息是否发送到rabbitmq服务器
        rabbitTemplate.setConfirmCallback(confirmCallback);


        //添加消息返回监听  消息发送到指定的routingkey就返回成功，如果找不到就失败
        rabbitTemplate.setReturnCallback(returnCallback);


        //发送消息,指定exchange routingkey
        rabbitTemplate.convertAndSend("exchange-1","springboot","hello springboot.");

        return msg;
    }


    @RequestMapping("/sendUser")
    public Object send(){

        //添加确认消息返回监听,用来监听消息是否发送到rabbitmq服务器
        rabbitTemplate.setConfirmCallback(confirmCallback);


        //添加消息返回监听  消息发送到指定的routingkey就返回成功，如果找不到就失败
        rabbitTemplate.setReturnCallback(returnCallback);

        User u=new User(11l,"ttt","1个用户");

        //发送消息,指定exchange routingkey
        rabbitTemplate.convertAndSend("exchange-1","user.ss",u);

        return u;
    }
}
