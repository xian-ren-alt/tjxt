package com.tianji.learning.constans;

import java.time.format.DateTimeFormatter;

public interface RedisConstants {

    public static final DateTimeFormatter SING_DATE_SUFFIX_FORMATTER = DateTimeFormatter.ofPattern(":yyyyMM");

    public static final DateTimeFormatter POINTS_BOARD_SUFFIX_FORMATTER = DateTimeFormatter.ofPattern(":yyyyMM");

    //签到记录的前缀
    String  SING_RECORD_KEY_PREFIX = "sing:uid：";
    //积分排行榜前缀
    String  POINTS_BOARD_KEY_PREFIX = "boards：";
}
