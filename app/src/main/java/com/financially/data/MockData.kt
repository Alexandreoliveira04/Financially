package com.financially.data

import com.financially.R
import java.time.LocalDate

object MockData {

    private val catSalario = Categoria("cat_salario", "Salário", R.drawable.ic_cat_briefcase, R.color.cat_menta)
    private val catMercado = Categoria("cat_mercado", "Mercado", R.drawable.ic_cat_shopping_cart, R.color.cat_menta)
    private val catMoradia = Categoria("cat_moradia", "Moradia", R.drawable.ic_cat_home, R.color.cat_azul)
    private val catAlimentacao = Categoria("cat_alimentacao", "Alimentação", R.drawable.ic_cat_restaurant, R.color.cat_ambar)
    private val catTransporte = Categoria("cat_transporte", "Transporte", R.drawable.ic_cat_bus, R.color.cat_violeta)
    private val catAssinaturas = Categoria("cat_assinaturas", "Assinaturas", R.drawable.ic_cat_iphone, R.color.cat_azul)
    private val catEletronicos = Categoria("cat_eletronicos", "Eletrônicos", R.drawable.ic_cat_laptop, R.color.cat_turquesa)
    private val catLazer = Categoria("cat_lazer", "Lazer", R.drawable.ic_cat_clapperboard, R.color.cat_orquidea)

    private val contaItau = Conta("conta_itau", "Itaú", "conta corrente")
    private val contaNubank = Conta("conta_nubank", "Nubank", "conta corrente")
    private val contaCarteira = Conta("conta_carteira", "Carteira", "dinheiro")
    private val contaCartaoInter = Conta("conta_inter", "Cartão Inter", "cartão de crédito")

    val lancamentos: List<Lancamento> = listOf(
        Lancamento(
            id = "l01",
            descricao = "Salário",
            valor = 5200.00,
            tipo = TipoLancamento.RECEITA,
            data = LocalDate.of(2026, 9, 5),
            categoria = catSalario,
            conta = contaItau,
            pago = true
        ),
        Lancamento(
            id = "l02",
            descricao = "Recarga do bilhete",
            valor = 80.00,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 6),
            categoria = catTransporte,
            conta = contaCarteira,
            pago = true
        ),
        Lancamento(
            id = "l03",
            descricao = "Mercado do mês",
            valor = 412.60,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 8),
            categoria = catMercado,
            conta = contaItau,
            pago = true,
            observacao = "Compra grande do mês, incluindo produtos de limpeza"
        ),
        Lancamento(
            id = "l04",
            descricao = "Celular",
            valor = 109.90,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 9),
            categoria = catAssinaturas,
            conta = contaCartaoInter,
            pago = true,
            parcelaAtual = 3,
            totalParcelas = 12
        ),
        Lancamento(
            id = "l05",
            descricao = "Conta de energia",
            valor = 100.00,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 10),
            categoria = catMoradia,
            conta = contaItau,
            pago = false
        ),
        Lancamento(
            id = "l06",
            descricao = "Almoço",
            valor = 38.90,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 13),
            categoria = catAlimentacao,
            conta = contaNubank,
            pago = true
        ),
        Lancamento(
            id = "l07",
            descricao = "Notebook",
            valor = 198.00,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 13),
            categoria = catEletronicos,
            conta = contaCartaoInter,
            pago = true,
            parcelaAtual = 3,
            totalParcelas = 6
        ),
        Lancamento(
            id = "l08",
            descricao = "Cinema",
            valor = 45.00,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 14),
            categoria = catLazer,
            conta = contaNubank,
            pago = false
        ),
        Lancamento(
            id = "l09",
            descricao = "Freelance de design",
            valor = 850.00,
            tipo = TipoLancamento.RECEITA,
            data = LocalDate.of(2026, 9, 15),
            categoria = catSalario,
            conta = contaNubank,
            pago = true
        ),
        Lancamento(
            id = "l10",
            descricao = "Assinatura de streaming",
            valor = 39.90,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 16),
            categoria = catAssinaturas,
            conta = contaCartaoInter,
            pago = true
        ),
        Lancamento(
            id = "l11",
            descricao = "Café",
            valor = 8.50,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 17),
            categoria = catAlimentacao,
            conta = contaCarteira,
            pago = false
        ),
        Lancamento(
            id = "l12",
            descricao = "Aluguel",
            valor = 1350.00,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 20),
            categoria = catMoradia,
            conta = contaItau,
            pago = true,
            observacao = "Já inclui condomínio"
        ),
        Lancamento(
            id = "l13",
            descricao = "Uber",
            valor = 27.30,
            tipo = TipoLancamento.DESPESA,
            data = LocalDate.of(2026, 9, 22),
            categoria = catTransporte,
            conta = contaNubank,
            pago = true
        )
    )
}
