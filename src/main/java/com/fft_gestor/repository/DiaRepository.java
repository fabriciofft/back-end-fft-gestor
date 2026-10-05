package com.fft_gestor.repository;

import com.fft_gestor.model.DiaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaRepository extends JpaRepository<DiaModel, Long> {

    @Query
    Optional<DiaModel> findByCodigo(Long codigo);

    @Query(value = "select d from Usuario u inner join u.dias d where d.data = :data and u.codigo = :codigoUsuario")
    Optional<DiaModel> buscarDiaAtualPorDataDeUmUsuario(LocalDate data, Long codigoUsuario);

    @Query(value = "select d from Usuario u inner join u.dias d where u.codigo = :codigoUsuario order by d.data desc")
    Page<DiaModel> listarDiasDeUmUsuario(Long codigoUsuario, Pageable pageable);

    @Query(value = "select d from Usuario u inner join u.dias d where u.codigo = :codigoUsuario and d.data = :data")
    Optional<DiaModel> buscarDataEspecificaNosDiasDeUmUsuario(Long codigoUsuario, LocalDate data);

    @Query(value = "select d from Dia d inner join d.acoes a where a.codigo = :codigoAcao")
    Optional<DiaModel> buscarDiaPorCodigoDeAcao(Long codigoAcao);
}
