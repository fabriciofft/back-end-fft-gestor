package com.fft_gestor.repository;

import com.fft_gestor.model.CodigoConfirmacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodigoConfirmacaoRepository extends JpaRepository<CodigoConfirmacaoModel, Long> {

    @Query
    Optional<CodigoConfirmacaoModel> findByEmail(String email);
}
