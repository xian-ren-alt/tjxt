package com.tianji.learning.mq;

import com.tianji.common.constants.MqConstants;
import com.tianji.learning.domain.dto.LearningRecordFormDTO;
import com.tianji.learning.domain.po.LearningRecord;
import com.tianji.learning.service.ILearningRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LearningRecordDelayListener {

    private final StringRedisTemplate redisTemplate;
    private final ILearningRecordService recordService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "learning.record.delay.queue", durable = "true"),
            exchange = @Exchange(name = MqConstants.Exchange.LEARNING_DELAY_EXCHANGE,
                    delayed = "true", type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.LEARNING_RECORD_DELAY_KEY
    ))
    public void onMessage(LearningRecordFormDTO dto) {
        log.info("收到延迟消息：lessonId={}, sectionId={}, moment={}",
                dto.getLessonId(), dto.getSectionId(), dto.getMoment());


        //存 learning_record 表（你刚写的那段）
        recordService.saveLearningRecord(dto);

        // 3. 更新课表
        recordService.handlecomitLearningLesson(dto);

        // 1. 从 Redis 读最新进度
        Object current = redisTemplate.opsForHash().get(
                "record:lesson:" + dto.getLessonId(),
                dto.getSectionId().toString()
        );
        if (current == null) {
            return;
        }

        // 2. 对比
        int currentMoment = Integer.parseInt(current.toString());
        if (currentMoment != dto.getMoment()) {
            log.debug("进度变了，用户还在看，不落库");
            return;
        }

        // 3. 一致 → 落库
        recordService.handlecomitLearningLesson(dto);

        // 4. 删 Redis
        redisTemplate.opsForHash().delete(
                "record:lesson:" + dto.getLessonId(),
                dto.getSectionId().toString()
        );
    }
}

