package com.fft_gestor.repository;

import com.fft_gestor.dto.response.PeriodoSimplificadoResponseDTO;
import com.fft_gestor.model.PeriodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoModel,Long> {



    @Query
    Optional<PeriodoModel> findByCodigo(Long codigo);

    @Query(value = "select p from Cartao c inner join c.periodos p where c.codigo = :codigoCartao and p.mes = :mes and p.ano = :ano")
    Optional<PeriodoModel> buscarPeriodoEmDeterminadoCartao(Long codigoCartao, Integer mes, Integer ano);

    @Query(value = """
        SELECT new com.fft_gestor.dto.response.PeriodoSimplificadoResponseDTO(
            p.codigo,
            c.diaFechamento,
            p.mes,
            p.ano
        ) 
        FROM Cartao c
        INNER JOIN c.periodos p
        WHERE c.codigo = :codigoCartao
        ORDER by p.mes, p.ano
    """)
    List<PeriodoSimplificadoResponseDTO> buscarPeriodosDeUmCartao(Long codigoCartao);

    @Query(value = "select p from Periodo p inner join p.compras cp where cp.codigo = :codigoCompra")
    Optional<PeriodoModel> buscarPeriodoPorCodigoDeCompra(Long codigoCompra);

    @Query(value = "select sum(cp.valor) from Periodo p inner join p.compras cp where p.codigo = :codigo and cp.quitado = false")
    Double buscarValorDeComprasPendentesDeUmPeriodo(Long codigo);
}
