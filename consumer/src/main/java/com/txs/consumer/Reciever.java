package com.txs.consumer;

import com.rabbitmq.client.Channel;
import com.txs.entity.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 消费者
 */
@Component
public class Reciever {

    //接受普通消息
    @RabbitListener(bindings = {
           @QueueBinding(
             value = @Queue(value = "queue-1",durable = "true"),
              exchange = @Exchange(value = "exchange-1",type = "topic",durable = "true"),
                   key = "springboot.#"
           )
    })
    @RabbitHandler
    public void recievemsg(String msg, Channel channel, Message message) throws IOException {
        System.out.println("消费者收到消息1："+msg);

        //手动应该
        //获取消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    //接受普通消息
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "queue-1",durable = "true"),
                    exchange = @Exchange(value = "exchange-1",type = "topic",durable = "true"),
                    key = "springboot.*"
            )
    })
    @RabbitHandler
    public void recievemsg2(String msg, Channel channel, Message message) throws IOException {
        System.out.println("消费者收到消息2："+msg);

        //手动应该
        //获取消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    //接受普通消息
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "queue-2",durable = "true"),
                    exchange = @Exchange(value = "exchange-2",type = "topic",durable = "true"),
                    key = "springboot.user.*"
            )
    })
    @RabbitHandler
    public void recieveUser(@Payload com.txs.entity.User user, Channel channel, Message message) throws IOException {
        System.out.println("body："+user);

        //手动应该
        //获取消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

    }
}
