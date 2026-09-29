package com.fft_gestor.repository;

import com.fft_gestor.dto.response.BuscaLimitesResponseDTO;
import com.fft_gestor.dto.response.GastoComCategoriaResponseDTO;
import com.fft_gestor.model.AcaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findByCodigo(Long codigo);

    Optional<AcaoModel> findByIdCompra(Long idCompra);

    @Query("""
        SELECT new com.fft_gestor.dto.response.GastoComCategoriaResponseDTO(
            a.categoria,
            SUM(
                CASE
                    WHEN a.tipoTransacao  = 'entrada' THEN a.valor
                    WHEN a.tipoTransacao = 'saida' THEN -a.valor
                    ELSE 0
                END
            ),
            SUM(
                CASE
                    WHEN a.tipoTransacao  = 'quitacaoCartao' THEN a.valor
                    WHEN a.tipoTransacao = 'cartaoCredito' THEN -a.valor
                    ELSE 0
                END
            )
        )
        FROM Usuario u
        INNER JOIN u.dias d
        INNER JOIN d.acoes a
        WHERE u.codigo = :codigoUsuario
        GROUP BY a.categoria
    """)
    List<GastoComCategoriaResponseDTO> buscarGatosPorCategoriaDeUmUsuario(Long codigoUsuario);
}
