package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long Id){
        return em.find(Member.class, Id);
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        // 전체 조회는 JPQL를 사용해야됨
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(em.find(Member.class, id));
        }

    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member findByUserAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m" +
                " order by m.age desc" ,Member.class)
                .setMaxResults(1)
                .getSingleResult();
    }

}
