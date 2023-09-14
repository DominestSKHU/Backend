package com.dominest.dominestbackend.domain.post.sanitationcheck.floor;

import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FloorService {
    private final FloorRepository floorRepository;

    @Transactional
    public List<Floor> create(List<Floor> floors) {
        try {
            return floorRepository.saveAll(floors);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Floor 저장 실패, 중복 혹은 값의 누락을 확인해주세요"
                    , HttpStatus.BAD_REQUEST);
        }
    }

    public List<Floor> getAllByPostId(Long postId) {
        return null;
    }
}
