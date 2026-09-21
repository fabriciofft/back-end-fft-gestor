package com.fft_gestor.repository;

import com.fft_gestor.model.DespesaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<DespesaModel, Long> {

    @Query
    Optional<DespesaModel> findByCodigo(Long codigo);

    @Query(value = "select d from Usuario u inner join u.despesas d where u.codigo = :codigoUsuario order by d.diaVencimento asc")
    List<DespesaModel> listarDespesasDeUmUsuario(Long codigoUsuario);
}
