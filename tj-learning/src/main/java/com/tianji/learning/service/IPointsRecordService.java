package com.tianji.learning.service;

import com.tianji.learning.domain.po.PointsRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.learning.enums.PointsRecordType;

/**
 * <p>
 * 学习积分记录 服务类
 * </p>
 *
 * @author author
 * @since 2026-10-27
 */
public interface IPointsRecordService extends IService<PointsRecord> {

    void addPointsRecord(Long userId, Integer points, PointsRecordType pointsRecordType);
}
