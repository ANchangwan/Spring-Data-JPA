package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import org.springframework.data.domain.Pageable;


import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
        // :username을 @Param에 받은 매개변수를 username에 대입
    Member findByUsername(@Param("username") String username);

    @Override
    Page<Member> findAll(Pageable pageable);

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, m.age, m.team.name) from Member m left join m.team t")
    Page<MemberDto> findMemberDto(Pageable zpageable);





}

