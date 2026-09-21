package com.fft_gestor.repository;

import com.fft_gestor.model.AcaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findByCodigo(Long codigo);

    Optional<AcaoModel> findByIdCompra(Long idCompra);
}
