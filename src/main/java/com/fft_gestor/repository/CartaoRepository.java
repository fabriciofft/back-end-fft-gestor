package com.fft_gestor.repository;

import com.fft_gestor.dto.response.BuscaLimitesResponseDTO;
import com.fft_gestor.model.CartaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<CartaoModel, Long> {

    @Query
    Optional<CartaoModel> findByCodigo(Long codigo);

    @Query(value = "select c from Cartao c inner join c.periodos p inner join p.compras cp where cp.codigo = :codigoCompra")
    Optional<CartaoModel> buscarCartaoPorCodigoDeCompra(Long codigoCompra);

    @Query(value = "select c from Usuario u inner join u.cartoes c where u.codigo = :codigoUsuario and c.apelido = :apelido")
    Optional<CartaoModel> buscarCartaoDeUmUsuarioPeloApelido(Long codigoUsuario, String apelido);

    @Query(value = "select c from Usuario u inner join u.cartoes c where u.codigo = :codigoUsuario and c.codigo = :codigoCartao")
    Optional<CartaoModel> buscarCartaoDeUmUsuario(Long codigoUsuario, Long codigoCartao);

    @Query(value = "select new com.fft_gestor.dto.response.BuscaLimitesResponseDTO(SUM(c.limiteUtilizado),SUM(c.limiteTotal)) from Usuario u inner join u.cartoes c where u.codigo = :codigoUsuario")
    BuscaLimitesResponseDTO buscarTotalDeLimiteDeCreditoUtilizadoPorUmUsuario(Long codigoUsuario);
}
