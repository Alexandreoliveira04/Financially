package com.financially.ui.lancamentos

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financially.R
import com.financially.data.Lancamento
import com.financially.data.TipoLancamento
import com.financially.databinding.ItemLancamentoBinding
import com.financially.util.formatarMoedaComSinal

class LancamentoAdapter(
    private val onTogglePago: (Lancamento) -> Unit,
    private val onItemClick: (Lancamento) -> Unit
) : ListAdapter<Lancamento, LancamentoAdapter.LancamentoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LancamentoViewHolder {
        val binding = ItemLancamentoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LancamentoViewHolder(binding, onTogglePago, onItemClick)
    }

    override fun onBindViewHolder(holder: LancamentoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LancamentoViewHolder(
        private val binding: ItemLancamentoBinding,
        private val onTogglePago: (Lancamento) -> Unit,
        private val onItemClick: (Lancamento) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lancamento: Lancamento) {
            val context = binding.root.context

            binding.tvDescricao.text = lancamento.descricao
            binding.tvMetadado.text =
                "${lancamento.categoria.nome.lowercase()} · ${lancamento.conta.nome.lowercase()}"
            binding.iconeCategoria.setImageResource(lancamento.categoria.iconeRes)
            binding.tvValor.text = formatarMoedaComSinal(lancamento.valor, lancamento.tipo)

            val corValor = if (lancamento.tipo == TipoLancamento.RECEITA) {
                R.color.menta
            } else {
                R.color.coral
            }

            if (lancamento.pago) {
                binding.tvDescricao.setTextColor(ContextCompat.getColor(context, R.color.texto_principal))
                binding.tvValor.setTextColor(ContextCompat.getColor(context, corValor))
                binding.bolinhaSituacao.setBackgroundResource(R.drawable.bg_situacao_paga)
                binding.iconeCheck.visibility = View.VISIBLE
            } else {
                binding.tvDescricao.setTextColor(ContextCompat.getColor(context, R.color.texto_apoio))
                binding.tvValor.setTextColor(ContextCompat.getColor(context, R.color.texto_apoio))
                binding.bolinhaSituacao.setBackgroundResource(R.drawable.bg_situacao_vazia)
                binding.iconeCheck.visibility = View.GONE
            }

            val descricaoSituacao = context.getString(
                if (lancamento.pago) R.string.desc_situacao_pago else R.string.desc_situacao_a_pagar
            )
            binding.frameSituacao.contentDescription = descricaoSituacao
            binding.frameCategoria.contentDescription = context.getString(
                R.string.desc_icone_categoria, lancamento.categoria.nome
            )

            val parcelamento = lancamento.parcelamento
            if (parcelamento != null) {
                val (atual, total) = parcelamento
                binding.tvBadgeParcela.text = "$atual/$total"
                binding.tvBadgeParcela.visibility = View.VISIBLE
            } else {
                binding.tvBadgeParcela.visibility = View.GONE
            }

            val corCategoria = ContextCompat.getColor(context, lancamento.categoria.corRes)
            val corComOpacidade = ColorUtils.setAlphaComponent(corCategoria, (0.13f * 255).toInt())
            binding.frameCategoria.background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = context.resources.getDimension(R.dimen.raio_icone_categoria)
                setColor(corComOpacidade)
            }
            binding.iconeCategoria.setColorFilter(corCategoria, PorterDuff.Mode.SRC_IN)

            binding.frameSituacao.setOnClickListener { onTogglePago(lancamento) }
            binding.root.setOnClickListener { onItemClick(lancamento) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Lancamento>() {
            override fun areItemsTheSame(oldItem: Lancamento, newItem: Lancamento): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Lancamento, newItem: Lancamento): Boolean =
                oldItem == newItem
        }
    }
}
