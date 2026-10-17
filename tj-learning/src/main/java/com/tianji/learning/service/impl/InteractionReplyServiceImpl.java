package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.enums.UserType;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.domain.dto.ReplyDTO;
import com.tianji.learning.domain.po.InteractionQuestion;
import com.tianji.learning.domain.po.InteractionReply;
import com.tianji.learning.domain.query.ReplyPageQuery;
import com.tianji.learning.domain.vo.ReplyVO;
import com.tianji.learning.enums.QuestionStatus;
import com.tianji.learning.mapper.InteractionQuestionMapper;
import com.tianji.learning.mapper.InteractionReplyMapper;
import com.tianji.learning.service.IInteractionReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractionReplyServiceImpl extends ServiceImpl<InteractionReplyMapper, InteractionReply> implements IInteractionReplyService {

    private final InteractionQuestionMapper questionMapper;
    private final UserClient userClient;

    @Override
    @Transactional
    public void saveReply(ReplyDTO replyDTO) {
        Long userId = UserContext.getUser();
        InteractionReply reply = new InteractionReply();
        reply.setUserId(userId);
        reply.setCreateTime(LocalDateTime.now());
        reply.setUpdateTime(LocalDateTime.now());

        if (replyDTO.getIsStudent()) {
            // 回答：设置基本字段
            reply.setAnonymity(replyDTO.getAnonymity());
            reply.setContent(replyDTO.getContent());
            reply.setQuestionId(replyDTO.getQuestionId());
            save(reply);

            // 更新互动问题表：记录最新回答id、回答数量+1、状态设为未查看
            questionMapper.update(null, new LambdaUpdateWrapper<InteractionQuestion>()
                    .eq(InteractionQuestion::getId, replyDTO.getQuestionId())
                    .set(InteractionQuestion::getLatestAnswerId, reply.getId())
                    .setSql("answer_times = answer_times + 1")
                    .set(InteractionQuestion::getStatus, QuestionStatus.UN_CHECK));
        } else {
            // 评论：设置基本字段 + 关联字段
            reply.setAnonymity(replyDTO.getAnonymity());
            reply.setContent(replyDTO.getContent());
            reply.setQuestionId(replyDTO.getQuestionId());
            reply.setTargetUserId(replyDTO.getTargetUserId());
            reply.setTargetReplyId(replyDTO.getTargetReplyId());
            reply.setAnswerId(replyDTO.getAnswerId());
            save(reply);

            // 递增父回复的评论数量
            if (replyDTO.getAnswerId() != null) {
                baseMapper.update(null, new LambdaUpdateWrapper<InteractionReply>()
                        .eq(InteractionReply::getId, replyDTO.getAnswerId())
                        .setSql("reply_times = reply_times + 1"));
            }

            // 如果评论用户是学生，标记问题状态为未查看
            Integer userType = userClient.queryUserType(userId);
            if (Integer.valueOf(UserType.STUDENT.getValue()).equals(userType)) {
                questionMapper.update(null, new LambdaUpdateWrapper<InteractionQuestion>()
                        .eq(InteractionQuestion::getId, replyDTO.getQuestionId())
                        .set(InteractionQuestion::getStatus, QuestionStatus.UN_CHECK));
            }
        }
    }

    @Override
    public PageDTO<ReplyVO> replyPageQuery(ReplyPageQuery query) {
        // 1. 构建查询条件
        LambdaQueryWrapper<InteractionReply> wrapper = new LambdaQueryWrapper<>();
        // 问题id是必传的
        wrapper.eq(InteractionReply::getQuestionId, query.getQuestionId());

        if (query.getAnswerId() == null) {
            // 回答id为空 → 查询问题下的回答（answer_id 为 null）
            wrapper.isNull(InteractionReply::getAnswerId);
        } else {
            // 回答id不为空 → 查询该回答下的评论
            wrapper.eq(InteractionReply::getAnswerId, query.getAnswerId());
        }

        // 过滤被隐藏的回复
        wrapper.eq(InteractionReply::getHidden, false);

        // 2. 分页查询（toMpPageDefaultSortByCreateTimeDesc 已包含排序，无需重复添加）
        Page<InteractionReply> page = page(query.toMpPageDefaultSortByCreateTimeDesc(), wrapper);
        List<InteractionReply> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 3. 收集所有用户id（回复者id + 目标用户id）
        Set<Long> userIds = new HashSet<>();
        for (InteractionReply reply : records) {
            userIds.add(reply.getUserId());
            if (reply.getTargetUserId() != null) {
                userIds.add(reply.getTargetUserId());
            }
        }

        // 4. 批量查询用户信息
        List<UserDTO> users = userClient.queryUserByIds(userIds);
        Map<Long, UserDTO> userMap = CollUtils.isEmpty(users) ? Collections.emptyMap() :
                users.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));

        // 5. 组装 VO
        List<ReplyVO> voList = new ArrayList<>(records.size());
        for (InteractionReply reply : records) {
            ReplyVO vo = BeanUtils.copyBean(reply, ReplyVO.class);
            // 设置回复者信息
            UserDTO user = userMap.get(reply.getUserId());
            if (user != null) {
                // 如果是匿名，不显示用户名和头像
                if (Boolean.TRUE.equals(reply.getAnonymity())) {
                    vo.setUserName("匿名用户");
                    vo.setUserIcon(null);
                } else {
                    vo.setUserName(user.getName());
                    vo.setUserIcon(user.getIcon());
                }
                vo.setUserType(user.getType());
            }
            // 设置目标用户信息（评论时才有）
            if (reply.getTargetUserId() != null) {
                UserDTO targetUser = userMap.get(reply.getTargetUserId());
                if (targetUser != null) {
                    vo.setTargetUserName(targetUser.getName());
                }
            }
            voList.add(vo);
        }

        return PageDTO.of(page, voList);
    }

    @Override
    public PageDTO<ReplyVO> adminPageQueryReply(ReplyPageQuery query) {
                // 1. 构建查询条件
        LambdaQueryWrapper<InteractionReply> wrapper = new LambdaQueryWrapper<>();
        // 问题id是必传的
        wrapper.eq(InteractionReply::getQuestionId, query.getQuestionId());

        if (query.getAnswerId() == null) {
            // 回答id为空 → 查询问题下的回答（answer_id 为 null）
            wrapper.isNull(InteractionReply::getAnswerId);
        } else {
            // 回答id不为空 → 查询该回答下的评论
            wrapper.eq(InteractionReply::getAnswerId, query.getAnswerId());
        }

        // 2. 分页查询（toMpPageDefaultSortByCreateTimeDesc 已包含排序，无需重复添加）
        Page<InteractionReply> page = page(query.toMpPageDefaultSortByCreateTimeDesc(), wrapper);
        List<InteractionReply> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 3. 收集所有用户id（回复者id + 目标用户id）
        Set<Long> userIds = new HashSet<>();
        for (InteractionReply reply : records) {
            userIds.add(reply.getUserId());
            if (reply.getTargetUserId() != null) {
                userIds.add(reply.getTargetUserId());
            }
        }

        // 4. 批量查询用户信息
        List<UserDTO> users = userClient.queryUserByIds(userIds);
        Map<Long, UserDTO> userMap = CollUtils.isEmpty(users) ? Collections.emptyMap() :
                users.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));

        // 5. 组装 VO
        List<ReplyVO> voList = new ArrayList<>(records.size());
        for (InteractionReply reply : records) {
            ReplyVO vo = BeanUtils.copyBean(reply, ReplyVO.class);
            // 设置回复者信息
            UserDTO user = userMap.get(reply.getUserId());
            if (user != null) {
                // 如果是匿名，不显示用户名和头像
                    vo.setUserName(user.getName());
                    vo.setUserIcon(user.getIcon());
                vo.setUserType(user.getType());
            }
            // 设置目标用户信息（评论时才有）
            if (reply.getTargetUserId() != null) {
                UserDTO targetUser = userMap.get(reply.getTargetUserId());
                if (targetUser != null) {
                    vo.setTargetUserName(targetUser.getName());
                }
            }
            voList.add(vo);
        }
        return PageDTO.of(page, voList);
    }

    @Transactional
    public void updateReplyHidden(Long id, Boolean hidden) {
        // 1. 更新目标回复/评论的 hidden 字段
        baseMapper.update(null, new LambdaUpdateWrapper<InteractionReply>()
                .eq(InteractionReply::getId, id)
                .set(InteractionReply::getHidden, hidden));

        // 2. 如果隐藏的是回答（answer_id 为 null），则该回答下的所有评论也要隐藏
        InteractionReply reply = getById(id);
        if (reply != null && reply.getAnswerId() == null) {
            baseMapper.update(null, new LambdaUpdateWrapper<InteractionReply>()
                    .eq(InteractionReply::getAnswerId, id)
                    .set(InteractionReply::getHidden, hidden));
        }
    }
    
}
