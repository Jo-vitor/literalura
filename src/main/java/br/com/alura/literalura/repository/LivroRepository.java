package br.com.alura.literalura.repository;

import br.com.alura.literalura.models.EIdioma;
import br.com.alura.literalura.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    public List<Livro> findByIdioma(EIdioma idioma);

    public List<Livro> findTop10ByOrderByTotalDownloadsDesc();
}
