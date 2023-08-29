package com.dominest.dominestbackend.domain.post.component.category.categorygenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class CategoryPositionGenerator {

    private final EntityManager entityManager;

    @Autowired
    public CategoryPositionGenerator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public synchronized int getNextPosition() {
        Query query = entityManager.createQuery("SELECT MAX(c.position) FROM Category c");
        Integer maxPosition = (Integer) query.getSingleResult();
        return (maxPosition != null) ? maxPosition + 1 : 1;
    }
}