package com.example.lollandback.member.mapper;

import com.example.lollandback.gameBoard.domain.GameBoard;
import com.example.lollandback.member.domain.EditMember;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.dto.MemberDto;
import com.example.lollandback.member.dto.SetRandomPasswordDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Insert("""
        INSERT INTO member (
            member_login_id, 
            member_password, 
            member_name, 
            member_phone_number, 
            member_email, 
            member_type,
            member_introduce
        )
        VALUES (
            #{member_login_id},
            #{member_password},
            #{member_name},
            #{member_phone_number},
            #{member_email},
            #{member_type},
            #{member_introduce}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(Member member);

    @Select("""
        SELECT *
        FROM member 
        WHERE member_login_id = #{memberLoginId} 
    """)
    Member selectById(String memberLoginId);

    @Delete("""
        DELETE FROM member
        WHERE id = #{id}
    """)
    int deleteById(Long id);

    @Select("""
        SELECT member_login_id
        FROM member
        WHERE member_login_id =#{memberLoginId}
        """)
    MemberDto selectByLoginId(String memberLoginId);

    @Select("""
        SELECT * FROM member
        WHERE member_login_id = #{memberLoginId} AND member_password = #{password}
        """)
    String selectByLoginIdAndPassword(String memberLoginId, String password);

    @Select("""
        SELECT id, member_login_id, member_name, member_phone_number, member_email, member_type,reg_time, member_introduce FROM member
        WHERE member_login_id = #{memberLoginId}
    """)
    MemberDto selectByMemberId(String memberLoginId);

    @Select("""
        SELECT member_login_id FROM member
        WHERE member_login_id = #{memberLoginId}
    """)
    String checkUserId(String memberLoginId);

    @Select("""
        SELECT member_login_id 
        FROM member 
        WHERE member_name = #{memberName} 
        AND member_email = #{memberEmail}
    """)
    String findIdByNameAndEmail(String memberName, String memberEmail);

    @Select("""
        SELECT COUNT(*) 
        FROM member
        WHERE member_login_id = #{memberLoginId}
        AND member_email = #{memberEmail}
    """)
    Integer findUserByIdAndEmail(String memberLoginId, String memberEmail);

    @Update("""
        UPDATE member 
        SET member_password = #{member_password} 
        WHERE member_login_id = #{member_login_id}
    """)
    void setPasswordByLoginId(SetRandomPasswordDto setRandomPasswordDto);

    @Update("""
        UPDATE member 
        SET 
        member_login_id = #{member_login_id},
        member_name = #{member_name},
        member_phone_number = #{member_phone_number},
        member_email = #{member_email},
        member_introduce = #{member_introduce}
        WHERE id = #{id}
    """)
    boolean editMember(EditMember member);

    @Update("""
        UPDATE member
        SET
        member_password = #{memberPassword}
        WHERE id = #{id} 
    """)
    void editPasswordById(Long id, String memberPassword);

    // user 인 회원 10명씩 조회
    @Select("""
        <script>
        SELECT * FROM member WHERE member_type = 'user'
        <if test="loginId != null and loginId != ''">
            AND member_login_id LIKE CONCAT('%', #{loginId}, '%')
        </if>
        <if test="name != null and name != ''">
            AND member_name LIKE CONCAT('%', #{name}, '%')
        </if>
        LIMIT #{from}, 10;
        </script>
    """)
    List<MemberDto> getAllMember(Integer from, String loginId, String name);


    // user 인 회원 숫 알기
    @Select("""
        <script>
        SELECT COUNT(*) FROM member WHERE member_type = 'user'
        <if test="loginId != null and loginId != ''">
            AND member_login_id = #{loginId}
        </if>
        <if test="name != null and name != ''">
            AND member_name = #{name}
        </if>
        </script>
    """)
    int countAllMember(String loginId, String name);


    @Select("""
        SELECT COUNT(*) 
        FROM member 
        WHERE member_email = #{memberEmail} 
    """)
    int checkUserEmail(String memberEmail);

    @Select("""
        <script>
            SELECT g.id, g.category, g.title, g.board_content
            FROM gameboard  g 
            JOIN gameboardlike gl 
            ON g.id = gl.game_board_id 
            WHERE gl.member_id = #{memberLoginId} 
            <if test='categoryType != \"전체\"'>
                AND g.category = #{categoryType}
            </if>
            LIMIT #{from}, 10
        </script>
    """)
    List<GameBoard> getGameBoardLikeByLoginId(String memberLoginId, Integer from, String categoryType);

    @Select("""
        <script>
        SELECT COUNT(*) 
        FROM gameboard  g 
        JOIN gameboardlike gl 
        ON g.id = gl.game_board_id 
        WHERE gl.member_id = #{memberLoginId}
        <if test='categoryType != \"전체\"'> 
            AND g.category = #{categoryType}
        </if>
        </script>
    """)
    int countAllGameBoardLikeByLoginId(String memberLoginId, String categoryType);

    // 삭제 되는 유저의 티입으 deleted 로 변경
    // 이메일, 핸드폰 번호, 탈퇴 유저 처리
    @Update("""
        UPDATE member
        SET
        member_type = 'deleted',
        member_email = '***@***',
        member_phone_number = '***-****-****',
        member_introduce = '탈퇴한 회원 입니다.'
        WHERE id = #{id}
    """)
    void deleteMemberInfoEditById(Long id);

    // 탈퇴한 유저인지 회원 로그인 id로 찾기
    @Select("""
        SELECT COUNT(*) FROM member
        WHERE member_login_id = #{memberLoginId}
        AND member_type = 'deleted'
    """)
    int findDeletedMember(String memberLoginId);


    // 회원 id번호로 로그인ID 불러오기
    @Select("""
        SELECT member_login_id 
        FROM member
        WHERE id = #{id}
    """)
    String getMemberLoginIdById(Long id);

    // 회원 id번호로 이메일 불러오기
    @Select("""
        SELECT member_email 
        FROM member
        WHERE id = #{id}
    """)
    String getMemberEmailById(Long id);
}
