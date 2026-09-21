package com.fft_gestor.repository;

import com.fft_gestor.model.MetricasUsoCartaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricasUsoCartaoRepository extends JpaRepository<MetricasUsoCartaoModel, Long> {


    @Query(value = "select h from Dia d inner join d.historicoUsoCartoes h where d.codigo = :codigoDia and h.codigoCartao = :codigoCartao")
    Optional<MetricasUsoCartaoModel> buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(Long codigoDia, Long codigoCartao);
}
