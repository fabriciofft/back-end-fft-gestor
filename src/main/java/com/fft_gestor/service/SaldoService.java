package com.fft_gestor.service;

import com.fft_gestor.dto.request.DefinirSaldoRequestDTO;
import com.fft_gestor.dto.response.BuscaLimitesResponseDTO;
import com.fft_gestor.dto.response.PeriodosSeparadosResponseDTO;
import com.fft_gestor.dto.response.SaldoReponseDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.CartaoModel;
import com.fft_gestor.model.CompraModel;
import com.fft_gestor.model.PeriodoModel;
import com.fft_gestor.model.UsuarioModel;
import com.fft_gestor.repository.CartaoRepository;
import com.fft_gestor.repository.PeriodoRepository;
import com.fft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaldoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;



    public SaldoReponseDTO buscarSaldoDeUmUsuarioPeloCodigo(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        BuscaLimitesResponseDTO buscaLimites = cartaoRepository.buscarTotalDeLimiteDeCreditoUtilizadoPorUmUsuario(usuario.getCodigo());

        Double totalLimitesFaturasPassadasUtilizado = 0.0;
        Double totalLimitesFaturasAtuaisUtilizado = 0.0;

        PeriodosSeparadosResponseDTO periodosSeparados = buscarPeriodosPassadosEAtuais(usuario.getCartoes());

        for(PeriodoModel periodo: periodosSeparados.getPeriodosPassados()){
            totalLimitesFaturasPassadasUtilizado += periodoRepository.buscarValorDeComprasPendentesDeUmPeriodo(periodo.getCodigo());
        }

        for(PeriodoModel periodo: periodosSeparados.getPeriodosAtuais()){
            totalLimitesFaturasAtuaisUtilizado += periodoRepository.buscarValorDeComprasPendentesDeUmPeriodo(periodo.getCodigo());
        }

        Double totalLimitesUtilizado = buscaLimites.getLimiteUtilizado();

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            totalLimitesFaturasPassadasUtilizado,
            totalLimitesFaturasAtuaisUtilizado,
            (totalLimitesUtilizado - totalLimitesFaturasAtuaisUtilizado - totalLimitesFaturasPassadasUtilizado),
            totalLimitesUtilizado,
            buscaLimites.getLimiteTotal()
        );
    }

    public SaldoReponseDTO definirSaldoInicial(DefinirSaldoRequestDTO definirSaldoRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(definirSaldoRequestDTO.getCodigoUsuario());

        usuario.setSaldoInicial(definirSaldoRequestDTO.getSaldoInicial());
        usuario.setSaldoAtual(definirSaldoRequestDTO.getSaldoInicial());

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            0.0,
            0.0,
            0.0,
            0.0,
            0.0
        );
    }

    public SaldoReponseDTO reiniciarGestaoDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        usuario.setSaldoInicial(0.0);
        usuario.setSaldoAtual(0.0);

        usuario.getDespesas().clear();;
        usuario.getDias().clear();
        usuario.getCategorias().clear();
        usuario.getCartoes().clear();

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
        );
    }

    //Métodos privado
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexitente!"));
    }

    private PeriodosSeparadosResponseDTO buscarPeriodosPassadosEAtuais(List<CartaoModel> cartoes){
        List<PeriodoModel> periodosAtuais = new ArrayList<>();
        List<PeriodoModel> periodosPassados = new ArrayList<>();

        YearMonth mesAtual = YearMonth.now();
        int diaAtual = LocalDate.now().getDayOfMonth();

        for (CartaoModel cartao: cartoes) {

            YearMonth mesDaFatura;

            if (diaAtual < cartao.getDiaFechamento()) {
                mesDaFatura = mesAtual.minusMonths(1);
            } else {
                mesDaFatura = mesAtual;
            }

            for (PeriodoModel periodo : cartao.getPeriodos()) {

                if (periodo.getMes().equals(mesDaFatura.getMonthValue())
                    && periodo.getAno().equals(mesDaFatura.getYear())
                ) {
                  periodosAtuais.add(periodo);
                }
                else if(periodo.getMes() < mesDaFatura.getMonthValue()
                        && periodo.getAno() <= (mesDaFatura.getYear())
                ){
                    periodosPassados.add(periodo);
                }

            }
        }

        return new PeriodosSeparadosResponseDTO(
            periodosAtuais,
            periodosPassados
        );
    }
}
