package com.fft_gestor.service;

import com.fft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.fft_gestor.dto.request.LancarCompraNoCartaoRequestDTO;
import com.fft_gestor.exception.RequestException;
import com.fft_gestor.model.*;
import com.fft_gestor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AcaoService {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private MetricasUsoCartaoRepository metricasUsoCartaoRepository;

    @Autowired
    private CartaoService cartaoService;


    public AcaoModel adicionarAcaoEmUmDia(AdicionarAcaoRequestDTO adicionarAcaoRequestDTO){
        AcaoModel acao = new AcaoModel();

        DiaModel dia = buscarDiaPeloCodigo(adicionarAcaoRequestDTO.getCodigoDia());
        UsuarioModel usuario = buscarUsuarioDonoDeDeterminadoDia(dia);

        DateTimeFormatter formatarHorario = DateTimeFormatter.ofPattern("HH:mm");
        Long idCompra = null;

        switch (adicionarAcaoRequestDTO.getTipoTransacao()){
            case "cartaoCredito":{
                CartaoModel cartao = buscarCartaoPeloCodigo(adicionarAcaoRequestDTO.getCodigoCartao());

                LancarCompraNoCartaoRequestDTO lancarCompraNoCartaoRequest = new LancarCompraNoCartaoRequestDTO(
                    cartao.getCodigo(),
                    adicionarAcaoRequestDTO.getCategoria(),
                    adicionarAcaoRequestDTO.getValor()
                );

                if(metricasUsoCartaoRepository.buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(dia.getCodigo(), cartao.getCodigo()).isEmpty()){
                    dia.getHistoricoUsoCartoes().add(
                        new MetricasUsoCartaoModel(
                            null,
                            cartao.getCodigo(),
                            cartao.getApelido(),
                            cartao.getLimiteUtilizado(),
                            (cartao.getLimiteUtilizado() + lancarCompraNoCartaoRequest.getValor()),
                            cartao.getLimiteTotal()
                        )
                    );
                }
                else{
                    MetricasUsoCartaoModel metricasUsoCartao = metricasUsoCartaoRepository.buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(dia.getCodigo(), cartao.getCodigo()).get();
                    metricasUsoCartao.setLimiteUsadoAtual(metricasUsoCartao.getLimiteUsadoAtual() + lancarCompraNoCartaoRequest.getValor());
                };

                idCompra =  cartaoService.lancarCompraNoCartao(lancarCompraNoCartaoRequest);
                break;
            }
            case "quitacaoCartao":{
                CartaoModel cartao = buscarCartaoPeloCodigo(adicionarAcaoRequestDTO.getCodigoCartao());

                if(metricasUsoCartaoRepository.buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(dia.getCodigo(), cartao.getCodigo()).isEmpty()){
                    dia.getHistoricoUsoCartoes().add(
                            new MetricasUsoCartaoModel(
                            null,
                            cartao.getCodigo(),
                            cartao.getApelido(),
                            cartao.getLimiteUtilizado(),
                            (cartao.getLimiteUtilizado() - adicionarAcaoRequestDTO.getValor()),
                            cartao.getLimiteTotal()
                        )
                    );
                }
                else{
                    MetricasUsoCartaoModel metricasUsoCartao = metricasUsoCartaoRepository.buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(dia.getCodigo(), cartao.getCodigo()).get();
                    metricasUsoCartao.setLimiteUsadoAtual(metricasUsoCartao.getLimiteUsadoAtual() - adicionarAcaoRequestDTO.getValor());
                };

                acao = buscarAcaoPorIdCompra(adicionarAcaoRequestDTO.getIdCompra());
                acao.setTipoTransacao("quitacaoCartao");
                break;
            }
            case "saida":{
                dia.setSaldoAtual(usuario.getSaldoAtual() - adicionarAcaoRequestDTO.getValor());
                usuario.setSaldoAtual(usuario.getSaldoAtual() - adicionarAcaoRequestDTO.getValor());
                break;
            }
            case "entrada":{
                dia.setSaldoAtual(usuario.getSaldoAtual() + adicionarAcaoRequestDTO.getValor());
                usuario.setSaldoAtual(usuario.getSaldoAtual() + adicionarAcaoRequestDTO.getValor());
                break;
            }
            default:
                throw new RequestException("Tipo de transação inválido!");
        }

        if(!adicionarAcaoRequestDTO.getTipoTransacao().equals("quitacaoCartao")){
            acao = new AcaoModel(
                null,
                adicionarAcaoRequestDTO.getCategoria(),
                formatarHorario.format(LocalDateTime.now()),
                adicionarAcaoRequestDTO.getTipoTransacao(),
                adicionarAcaoRequestDTO.getIndiceIcon(),
                adicionarAcaoRequestDTO.getValor(),
                adicionarAcaoRequestDTO.getApelidoCartao(),
                idCompra
            );

            dia.getAcoes().add(acao);
        }

        usuarioRepository.save(usuario);
        return acao;
    }

    public List<DiaModel> reverterAcao(Long codigo){
        AcaoModel acao = buscarAcaoPeloCodigo(codigo);

        CartaoModel cartao = new CartaoModel();

        UsuarioModel usuario = usuarioRepository.buscarUsuarioPorCodigoDeAcao(acao.getCodigo())
            .orElseThrow(() -> new RequestException("Usuário inexistente!"));
        DiaModel dia = diaRepository.buscarDiaPorCodigoDeAcao(acao.getCodigo())
            .orElseThrow(() -> new RequestException("Dia inexistente!"));

        switch (acao.getTipoTransacao()){
            case "saida":{
                dia.setSaldoAtual(usuario.getSaldoAtual() + acao.getValor());
                usuario.setSaldoAtual(usuario.getSaldoAtual() + acao.getValor());
                break;
            }
            case "entrada":{
                dia.setSaldoAtual(usuario.getSaldoAtual() - acao.getValor());
                usuario.setSaldoAtual(usuario.getSaldoAtual() - acao.getValor());
                break;
            }
            case "cartaoCredito":{
                CompraModel compra = buscarCompraPeloIdCompra(acao.getIdCompra());

                cartao = buscarCartaoPorCodigoDeCompra(compra.getCodigo());
                PeriodoModel periodo = buscarPeriodoPorCodigoDeCompra(compra.getCodigo());

                cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() - compra.getValor());
                periodo.getCompras().remove(compra);

                if(periodo.getCompras().isEmpty()){
                    cartao.getPeriodos().remove(periodo);
                }

                MetricasUsoCartaoModel metricasUsoCartao = buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(dia.getCodigo(), cartao.getCodigo());
                metricasUsoCartao.setLimiteUsadoAtual(metricasUsoCartao.getLimiteUsadoAtual() - compra.getValor());

                if(metricasUsoCartao.getLimiteUsadoAtual().equals(metricasUsoCartao.getLimiteUsadoInicial())){
                    dia.getHistoricoUsoCartoes().remove(metricasUsoCartao);
                }

                break;
            }
            default: {
                throw new RequestException("Não é possível desfazer uma quitação!");
            }
        }

        dia.getAcoes().remove(acao);

        usuarioRepository.save(usuario);
        return diaRepository.listarDiasDeUmUsuario(usuario.getCodigo());
    }


    //Métodos privados
    private AcaoModel buscarAcaoPeloCodigo(long codigo){
        return acaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Ação inexistente!"));
    }

    private AcaoModel buscarAcaoPorIdCompra(long idCompra){
        return acaoRepository.findByIdCompra(idCompra)
                .orElseThrow(() -> new RequestException("Ação inexistente!"));
    }

    private DiaModel buscarDiaPeloCodigo(long codigo){
        return diaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Dia inexistente!"));
    }

    private CartaoModel buscarCartaoPeloCodigo(long codigo){
        return cartaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    private CartaoModel buscarCartaoPorCodigoDeCompra(Long codigoCompra){
        return cartaoRepository.buscarCartaoPorCodigoDeCompra(codigoCompra)
            .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    private PeriodoModel buscarPeriodoPorCodigoDeCompra(Long codigoCompra){
        return periodoRepository.buscarPeriodoPorCodigoDeCompra(codigoCompra)
            .orElseThrow(() -> new RequestException("Periodo inexistente!"));
    }

    private CompraModel buscarCompraPeloIdCompra(long idCompra){
        return compraRepository.findByIdCompra(idCompra)
            .orElseThrow(() -> new RequestException("Compra inexistente!"));
    }

    private MetricasUsoCartaoModel buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(Long codigoDia, Long codigoCartao) {
        return  metricasUsoCartaoRepository.buscarDataPorCodigoDeCartaoDoHistoricoDeUsoDeCartoes(codigoDia, codigoCartao)
            .orElseThrow(() -> new RequestException("Metrica inexistente!"));
    }

    private UsuarioModel buscarUsuarioDonoDeDeterminadoDia(DiaModel dia){
        return usuarioRepository.buscarUsuarioDonoDeDeterminadoDia(dia.getCodigo())
                .orElseThrow(() -> new RequestException("Usuário inexitente!"));
    }
}
