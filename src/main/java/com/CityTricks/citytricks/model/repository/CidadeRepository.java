package com.CityTricks.citytricks.model.repository;

import com.CityTricks.citytricks.model.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    @PersistenceContext
    public EntityManager entityManager = null;

    public static void saveCidade(Cidade cidade) {

        if (cidade.getId() == null) {
            entityManager.persist(cidade);
        } else {
            entityManager.merge(cidade);
        }
    }

    Optional<Cidade> findById(Long id);
}