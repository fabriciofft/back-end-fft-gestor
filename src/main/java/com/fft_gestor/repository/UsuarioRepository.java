package com.fft_gestor.repository;

import com.fft_gestor.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    @Query
    Optional<UsuarioModel> findByCodigo(Long codigo);

    @Query
    Optional<UsuarioModel> findByEmail(String email);

    @Query(value = "select u from Usuario u inner join u.categorias c where c.codigo = :codigoCategoria")
    Optional<UsuarioModel> buscarUsuarioPorCodigoDeCategoria(Long codigoCategoria);

    @Query(value = "select u from Usuario u inner join u.despesas d where d.codigo = :codigoDespesa")
    Optional<UsuarioModel> buscarUsuarioPorCodigoDeDespesa(Long codigoDespesa);

    @Query(value = "select u from Usuario u inner join u.dias d where d.codigo = :codigoDia")
    Optional<UsuarioModel> buscarUsuarioDonoDeDeterminadoDia(Long codigoDia);

    @Query(value = "select u from Usuario u inner join u.dias d inner join d.acoes a where a.codigo = :codigoAcao")
    Optional<UsuarioModel> buscarUsuarioPorCodigoDeAcao(Long codigoAcao);
}
