package com.fft_gestor.repository;

import com.fft_gestor.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    @Query
    Optional<CategoriaModel> findByCodigo(Long codigo);

    @Query(value = "select c from Usuario u inner join u.categorias c where c.nome = LOWER(:nome) and u.codigo = :codigoUsuario")
    Optional<CategoriaModel> buscarCategoriaEmListaDeCategoriasDeUmUsuario(String nome, Long codigoUsuario);

}
