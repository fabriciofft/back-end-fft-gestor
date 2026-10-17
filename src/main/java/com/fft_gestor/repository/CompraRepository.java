package com.fft_gestor.repository;

import com.fft_gestor.model.CompraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<CompraModel, Long> {

    @Query
    Optional<CompraModel> findByCodigo(Long codigo);

    @Query
    Optional<CompraModel> findByIdCompra(Long idCompra);

    @Query(value = "Select cp from Compra cp where cp.idCompra = :idCompra")
    List<CompraModel> listarParcelasPeloIdDeCompra(Long idCompra);
}
