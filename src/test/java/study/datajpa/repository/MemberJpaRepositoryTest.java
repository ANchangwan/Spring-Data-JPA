package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA",30);
        Member memberA = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(memberA.getId());

        assertThat(findMember.getId()).isEqualTo(memberA.getId());
        assertThat(findMember.getUsername()).isEqualTo(memberA.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    void basicCRUD(){
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        Member findMemberA = memberJpaRepository.findById(memberA.getId()).get();
        Member findmemberB = memberJpaRepository.findById(memberB.getId()).get();

        assertThat(findMemberA.getId()).isEqualTo(memberA.getId());
        assertThat(findmemberB.getId()).isEqualTo(memberB.getId());

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members).hasSize(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제
        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);

        long count1 = memberJpaRepository.count();

        assertThat(count1).isEqualTo(0);

    }

    @Test
    void usernameAndAge(){
        Member memberA = new Member("memberA", 40);
        Member memberB = new Member("memberB", 50);


        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("memberB", 10);

        System.out.println(members.get(0).getUsername());


        assertThat(members).isNotEmpty();

        // 모든 멤버가 조건을 만족하는지 검증
        boolean allMatch = members.stream()
                .allMatch(m -> m.getUsername().equals("memberB") && m.getAge() > 10);

        members.stream().forEach(m -> System.out.println(m.getUsername()+" : "+m.getAge()));

        assertThat(allMatch).isFalse();


    }

    @Test
    void testPaging(){
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);

        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(members).isNotEmpty();
        assertThat(totalCount).isEqualTo(3);


    }

}
