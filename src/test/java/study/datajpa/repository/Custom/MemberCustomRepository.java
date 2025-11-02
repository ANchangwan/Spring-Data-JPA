package study.datajpa.repository.Custom;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findCustomByUsername(String username);
}
