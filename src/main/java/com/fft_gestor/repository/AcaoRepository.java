package com.fft_gestor.repository;

import com.fft_gestor.dto.response.AcaoCategoriaResponseDTO;
import com.fft_gestor.dto.response.GastoComCategoriaResponseDTO;
import com.fft_gestor.model.AcaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findByCodigo(Long codigo);

    Optional<AcaoModel> findByIdCompra(Long idCompra);

    @Query("""
        SELECT new com.fft_gestor.dto.response.GastoComCategoriaResponseDTO(
            a.categoria,
            MAX(a.indiceIcon),
            SUM(
                CASE
                    WHEN a.tipoTransacao  = 'entrada' THEN a.valor
                    WHEN a.tipoTransacao = 'saida' THEN -a.valor
                    ELSE 0
                END
            )
        )
        FROM Usuario u
        INNER JOIN u.dias d
        INNER JOIN d.acoes a
        WHERE u.codigo = :codigoUsuario
            AND d.data BETWEEN :dataInicio AND :dataFim
        GROUP BY a.categoria
    """)
    List<GastoComCategoriaResponseDTO> buscarGastosPorCategoriaNoSaldoDeUmUsuario(Long codigoUsuario, LocalDate dataInicio, LocalDate dataFim);

    @Query("""
        SELECT new com.fft_gestor.dto.response.GastoComCategoriaResponseDTO(
            a.categoria,
            MAX(a.indiceIcon),
            SUM(
                CASE
                    WHEN a.tipoTransacao  = 'cartaoCredito' OR  a.tipoTransacao  = 'quitado' THEN a.valor
                    ELSE 0
                END
            )
        )
        FROM Usuario u
        INNER JOIN u.dias d
        INNER JOIN d.acoes a
        WHERE u.codigo = :codigoUsuario
            AND d.data BETWEEN :dataInicio AND :dataFim
        GROUP BY a.categoria
    """)
    List<GastoComCategoriaResponseDTO> buscarGastosPorCategoriaNoCartaoDeUmUsuario(Long codigoUsuario, LocalDate dataInicio, LocalDate dataFim);

    @Query("""
        SELECT new com.fft_gestor.dto.response.AcaoCategoriaResponseDTO(
            a.codigo,
            d.data,
            a.horario,
            a.tipoTransacao,
            a.valor,
            a.apelidoCartao
        )
        FROM Usuario u
        INNER JOIN u.dias d
        INNER JOIN d.acoes a
        WHERE u.codigo = :codigoUsuario
            AND a.categoria = :categoria
            AND d.data BETWEEN :dataInicio AND :dataFim
        ORDER BY d.data DESC
    """)
    List<AcaoCategoriaResponseDTO> buscarAcoesPorCategoriaDeumUsuario(Long codigoUsuario, String categoria, LocalDate dataInicio, LocalDate dataFim);

    @Query("""
        SELECT COALESCE(
            SUM(
                CASE
                    WHEN a.tipoTransacao  = 'saida' OR  a.tipoTransacao  = 'cartaoCredito' OR  a.tipoTransacao  = 'quitado' THEN a.valor
                ELSE 0
                END
            )
            , 0.0
        ) 
        FROM Usuario u 
        INNER JOIN u.dias d 
        INNER JOIN d.acoes a 
        WHERE u.codigo = :codigoUsuario and d.data = :data
    """)
    Double gastosEmUmaData(Long codigoUsuario, LocalDate data);
}
