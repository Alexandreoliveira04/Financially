package com.financially.ui.detalhe

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.financially.R
import com.financially.data.Lancamento
import com.financially.data.TipoLancamento
import com.financially.databinding.ActivityDetalheBinding
import com.financially.databinding.ViewLinhaChaveValorBinding
import com.financially.databinding.ViewPilulaBinding
import com.financially.util.formatarData
import com.financially.util.formatarMoedaComSinal

class DetalheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lancamento = obterLancamentoExtra()
        if (lancamento == null) {
            finish()
            return
        }

        binding = ActivityDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        preencherCabecalho(lancamento)
        preencherPilulas(lancamento)
        preencherDetalhes(lancamento)
        preencherParcelamento(lancamento)
    }

    private fun obterLancamentoExtra(): Lancamento? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_LANCAMENTO, Lancamento::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LANCAMENTO)
        }
    }

    private fun preencherCabecalho(lancamento: Lancamento) {
        binding.iconeCategoria.setImageResource(lancamento.categoria.iconeRes)
        binding.tvDescricao.text = lancamento.descricao
        binding.tvValor.text = formatarMoedaComSinal(lancamento.valor, lancamento.tipo)

        val corValor = if (lancamento.tipo == TipoLancamento.RECEITA) R.color.menta else R.color.coral
        binding.tvValor.setTextColor(ContextCompat.getColor(this, corValor))

        val corCategoria = ContextCompat.getColor(this, lancamento.categoria.corRes)
        val corComOpacidade = ColorUtils.setAlphaComponent(corCategoria, (0.13f * 255).toInt())
        binding.frameCategoria.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.raio_icone_categoria_grande)
            setColor(corComOpacidade)
        }
        binding.iconeCategoria.setColorFilter(corCategoria, PorterDuff.Mode.SRC_IN)
        binding.frameCategoria.contentDescription = getString(
            R.string.desc_icone_categoria, lancamento.categoria.nome
        )
    }

    private fun preencherPilulas(lancamento: Lancamento) {
        binding.containerPilulas.removeAllViews()
        val inflater = LayoutInflater.from(this)

        val tipoTexto = getString(
            if (lancamento.tipo == TipoLancamento.RECEITA) R.string.receita_label else R.string.despesa_label
        )
        adicionarPilula(inflater, tipoTexto, destaque = false)
        adicionarPilula(inflater, lancamento.categoria.nome, destaque = false)

        lancamento.parcelamento?.let { (atual, total) ->
            adicionarPilula(inflater, getString(R.string.pilula_parcela_formato, atual, total), destaque = true)
        }
    }

    private fun adicionarPilula(inflater: LayoutInflater, texto: String, destaque: Boolean) {
        val pilulaBinding = ViewPilulaBinding.inflate(inflater, binding.containerPilulas, false)
        pilulaBinding.tvPilula.text = texto
        if (destaque) {
            pilulaBinding.tvPilula.setBackgroundResource(R.drawable.bg_pilula_lavanda)
            pilulaBinding.tvPilula.setTextColor(ContextCompat.getColor(this, R.color.lavanda))
        }
        binding.containerPilulas.addView(pilulaBinding.root)
    }

    private fun preencherDetalhes(lancamento: Lancamento) {
        binding.containerKv.removeAllViews()
        val inflater = LayoutInflater.from(this)

        adicionarLinhaChaveValor(inflater, getString(R.string.chave_conta), lancamento.conta.nome)
        adicionarLinhaChaveValor(inflater, getString(R.string.chave_data), formatarData(lancamento.data))

        val situacaoTexto = getString(
            if (lancamento.pago) R.string.situacao_paga else R.string.situacao_a_pagar
        )
        val corSituacao = if (lancamento.pago) R.color.menta else R.color.texto_secundario
        adicionarLinhaChaveValor(
            inflater, getString(R.string.chave_situacao), situacaoTexto, ContextCompat.getColor(this, corSituacao)
        )

        if (!lancamento.observacao.isNullOrBlank()) {
            adicionarLinhaChaveValor(inflater, getString(R.string.chave_observacao), lancamento.observacao)
        }
    }

    private fun adicionarLinhaChaveValor(
        inflater: LayoutInflater,
        chave: String,
        valor: String,
        corValor: Int? = null
    ) {
        val linhaBinding = ViewLinhaChaveValorBinding.inflate(inflater, binding.containerKv, false)
        linhaBinding.tvChave.text = chave
        linhaBinding.tvValor.text = valor
        if (corValor != null) {
            linhaBinding.tvValor.setTextColor(corValor)
        }
        binding.containerKv.addView(linhaBinding.root)
    }

    private fun preencherParcelamento(lancamento: Lancamento) {
        val parcelamento = lancamento.parcelamento
        if (parcelamento == null) {
            binding.cardParcelamento.visibility = View.GONE
            return
        }

        val (atual, total) = parcelamento
        binding.cardParcelamento.visibility = View.VISIBLE
        binding.progressParcelamento.max = total
        binding.progressParcelamento.progress = atual
        binding.tvParcelasPagas.text = getString(R.string.parcelas_pagas_formato, atual, total)
    }

    companion object {
        const val EXTRA_LANCAMENTO = "extra_lancamento"
    }
}
