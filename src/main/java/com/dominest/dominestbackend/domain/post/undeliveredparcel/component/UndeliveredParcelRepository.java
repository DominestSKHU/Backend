package com.dominest.dominestbackend.domain.post.undeliveredparcel.component;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UndeliveredParcelRepository extends JpaRepository<UndeliveredParcel, Long> {
}