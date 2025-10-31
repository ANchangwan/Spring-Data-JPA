package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
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
    void testFindUser() {
        Member memberA = new Member("memberA", 10);
        Member memberB = new Member("memberB", 20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> findMembers = memberRepository.findUser("memberA", 10);
        assertThat(findMembers.get(0).getUsername()).isNotNull();
    }

    @Test
    void testFindUserUsePagable() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        PageRequest pageRequest =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> pageMember = memberRepository.findByAge(10, pageRequest);

        List<Member> content = pageMember.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(pageMember.getTotalElements()).isEqualTo(5);
        assertThat(pageMember.getNumber()).isEqualTo(0);
        assertThat(pageMember.getTotalPages()).isEqualTo(2);
        assertThat(pageMember.isFirst()).isTrue();
        assertThat(pageMember.hasNext()).isTrue();

    }

    @Test
    void testUpdateMember() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 50));

        int resultCount = memberRepository.bulkeAgePlus(20);


        List<Member> memberList = memberRepository.findByUsername("member5");
        Member member5 = memberList.get(0);
        System.out.println("member5 : " + member5);

//
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void testFindMemberLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamB));

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        System.out.println("members : " + members);

        for (Member member : members) {
            System.out.println("member : " + member.getUsername() + ", team : " + member.getTeam().getName());
        }

    }

    @Test
    public void callCustom() throws Exception{
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        memberRepository.save(member); //@prePersist

        Thread.sleep(1000);
        member.setUsername("member2");

        em.flush(); //@preUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("findMember : " + findMember.getUsername());
        System.out.println("findMember.createdDate = " +
                findMember.getCreatedDate());
        System.out.println("findMember.updatedDate = " +
                findMember.getLastModifiedDate());

        System.out.println("findMember.createdBy = " + findMember.getCreatedBy());
        System.out.println("findMember.updatedBy = " + findMember.getLastModifiedBy());

    }


}