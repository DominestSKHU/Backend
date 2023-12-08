package com.dominest.dominestbackend.domain.dateNotice.repository;

import com.dominest.dominestbackend.domain.dateNotice.DateNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateNoticeRepository extends JpaRepository<DateNotice, Long> {
    DateNotice save(DateNotice dateNotice);

    List<DateNotice> findByCreatedBy(String createdBy);

}
