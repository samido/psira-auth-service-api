package com.psira.repository;

import com.psira.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SessionRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void persist(Session session) {
        entityManager.persist(session);
    }

    @Transactional
    public void deleteByToken(String token) {
        Session session = entityManager.createQuery("SELECT s FROM Session s WHERE s.token = :token", Session.class)
                .setParameter("token", token)
                .getSingleResult();
        if (session != null) {
            entityManager.remove(session);
        }
    }

    public Session findByToken(String token) {
        return entityManager.createQuery("SELECT s FROM Session s WHERE s.token = :token", Session.class)
                .setParameter("token", token)
                .getSingleResult();
    }
}
