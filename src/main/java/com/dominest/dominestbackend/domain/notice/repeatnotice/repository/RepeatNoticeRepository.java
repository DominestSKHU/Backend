package com.dominest.dominestbackend.domain.notice.repeatnotice.repository;

import com.dominest.dominestbackend.domain.notice.repeatnotice.RepeatNotice;
import com.dominest.dominestbackend.domain.notice.repeatschedule.RepeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatNoticeRepository extends JpaRepository<RepeatNotice, Long> {

    List<RepeatNotice> findByCreatedBy(String createdBy);

    List<RepeatNotice> findAllByRepeatSchedule(RepeatSchedule repeatSchedule);

}
