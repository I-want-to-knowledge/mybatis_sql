package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.example.demo.model.UserState;

/**
 * 映射
 *
 * @author YanZhen
 * @since 2019-02-15 14:47
 */
@Mapper // 映射层的标志
public interface UserStateMapper {
    int insert(@Param("userState") UserState userState);

    int insertSelective(@Param("userState") UserState userState);

    int insertList(@Param("userStates") List<UserState> userStates);

    int updateByPrimaryKeySelective(@Param("userState") UserState userState);
}
