package com.dominest.dominestbackend.domain.datenotice.repository;

import com.dominest.dominestbackend.domain.datenotice.DateNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateNoticeRepository extends JpaRepository<DateNotice, Long> {
    DateNotice save(DateNotice dateNotice);

    List<DateNotice> findByCreatedBy(String createdBy);

}
