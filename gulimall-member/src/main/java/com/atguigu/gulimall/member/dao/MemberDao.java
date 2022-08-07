package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author test
 * @email test@gmail.com
 * @date 2022-08-06 23:41:38
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
