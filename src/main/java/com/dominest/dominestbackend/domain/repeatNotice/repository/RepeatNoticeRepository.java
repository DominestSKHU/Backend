package com.dominest.dominestbackend.domain.repeatNotice.repository;

import com.dominest.dominestbackend.domain.repeatNotice.RepeatNotice;
import com.dominest.dominestbackend.domain.repeatSchedule.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatNoticeRepository extends JpaRepository<RepeatNotice, Long> {

    List<RepeatNotice> findByCreatedBy(String createdBy);

    List<RepeatNotice> findAllByRepeatSchedule(RepeatSchedule repeatSchedule);

}
