package com.desafio.nttdata.servicoemprestimo.repository;

import com.desafio.nttdata.servicoemprestimo.entity.EmprestimoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoEntity, Long> {
}
