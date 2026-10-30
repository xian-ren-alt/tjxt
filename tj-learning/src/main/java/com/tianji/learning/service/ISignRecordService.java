package com.tianji.learning.service;

import com.tianji.learning.domain.po.SignRecord;
import com.tianji.learning.domain.vo.SignRecordVO;
import com.tianji.learning.domain.vo.SignResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 签到记录表 服务类
 * </p>
 *
 * @author author
 * @since 2026-10-27
 */
public interface ISignRecordService extends IService<SignRecord> {

    /**
     * 今日签到
     */
    SignResultVO addSignRecords();

    /**
     * 查询当月签到日历
     */
    SignRecordVO querySignCalendar();
}
