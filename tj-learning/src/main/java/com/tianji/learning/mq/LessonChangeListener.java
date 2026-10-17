package com.tianji.learning.mq;

import com.tianji.api.dto.trade.OrderBasicDTO;
import com.tianji.common.constants.MqConstants;
import com.tianji.learning.service.impl.LearningLessonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LessonChangeListener {
    private final LearningLessonServiceImpl lessonService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "lesson.change.queue", durable = "true"),
            exchange = @Exchange(value = MqConstants.Exchange.ORDER_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.ORDER_PAY_KEY
    ))

    public void listenLessonPay(OrderBasicDTO order){
        if(order == null|| order.getCourseIds() == null|| order.getUserId()== null){
            log.error("订单支付，异常消息，信息未空");
            return;
        }
        log.info("用户：{}，支付了课程：{}", order.getUserId(), order.getCourseIds());
        lessonService.addUserLessons(order.getUserId(), order.getCourseIds());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "lesson.change.queue", durable = "true"),
            exchange = @Exchange(value = MqConstants.Exchange.ORDER_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.ORDER_REFUND_KEY
    ))

    public void listenLessonNoPay(OrderBasicDTO order){
        if(order == null|| order.getCourseIds() == null|| order.getUserId()== null){
            log.error("订单支付，异常消息，信息未空");
            return;
        }
        log.info("用户：{}，支付了课程：{}", order.getUserId(), order.getCourseIds());
        lessonService.deleteLearningCourse(order.getUserId(), order.getCourseIds());
    }
}
