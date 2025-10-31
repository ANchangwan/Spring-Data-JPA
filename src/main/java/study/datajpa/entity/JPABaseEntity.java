package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JPABaseEntity {

    @Column(updatable = false) //데이터가 바꿔도 반영되지 않음
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // 저장 전
    public void PrePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // 업데이트 전
    public void PreUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
