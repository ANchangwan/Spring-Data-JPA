package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
        Member memberB = new Member("new MemberB", 10);
        Member saveMember = memberRepository.save(memberB);

        Member memberA = new Member("Member A", 20);
        Member saveMemberA = memberRepository.save(memberA);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(memberB.getId());
        assertThat(findMember.getUsername()).isEqualTo(memberB.getUsername());
        assertThat(findMember).isEqualTo(memberB);
    }

    @Test
    void testFindUsername(){
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Member findMemberUsername = memberRepository.findByUsername("memberA");
        assertThat(findMemberUsername.getUsername()).isEqualTo(memberA.getUsername());
    }
    @Test
    void testFindUser(){
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> findMembers = memberRepository.findUser("memberA", 10);
        assertThat(findMembers.get(0).getUsername()).isNotNull();
    }
}