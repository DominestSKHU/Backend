package com.dominest.dominestbackend.domain.post.manual.component;

import com.dominest.dominestbackend.domain.post.undeliveredparcel.component.UndeliveredParcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualRepository extends JpaRepository<Manual, Long> {

}