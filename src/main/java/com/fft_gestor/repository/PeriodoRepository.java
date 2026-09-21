package com.fft_gestor.repository;

import com.fft_gestor.model.PeriodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoModel,Long> {



    @Query
    Optional<PeriodoModel> findByCodigo(Long codigo);

    @Query(value = "select p from Cartao c inner join c.periodos p where c.codigo = :codigoCartao and p.mes = :mes and p.ano = :ano")
    Optional<PeriodoModel> buscarPeriodoEmDeterminadoCartao(Long codigoCartao, Integer mes, Integer ano);

    @Query(value = "select p from Periodo p inner join p.compras cp where cp.codigo = :codigoCompra")
    Optional<PeriodoModel> buscarPeriodoPorCodigoDeCompra(Long codigoCompra);
}
