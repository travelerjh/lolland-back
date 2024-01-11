package com.example.lollandback.member.mapper;

import com.example.lollandback.member.domain.Member;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Insert("""
        INSERT INTO member (
            member_login_id, 
            member_password, 
            member_name, 
            member_phone_number, 
            member_email, 
            member_type
        )
        VALUES (
            #{member_login_id},
            #{member_password},
            #{member_name},
            #{member_phone_number},
            #{member_email},
            #{member_type}
        )
        """)
    void insertUser(Member member);

    @Select("""
        SELECT *
        FROM member 
        WHERE member_login_id = #{memberLoginId} 
    """)
    Member selectById(String memberLoginId);

    @Delete("""
        DELETE FROM member
        WHERE member_login_id = #{memberLoginId}
    """)
    int deleteById(String memberLoginId);
}
