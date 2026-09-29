package com.financially.ui.lancamentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.financially.R
import com.financially.data.Lancamento
import com.financially.data.MockData
import com.financially.data.TipoLancamento
import com.financially.databinding.FragmentLancamentosBinding
import com.financially.ui.detalhe.DetalheActivity
import com.financially.util.formatarMoeda

private enum class Filtro { TODOS, RECEITAS, DESPESAS }

class LancamentosFragment : Fragment(R.layout.fragment_lancamentos) {

    private var _binding: FragmentLancamentosBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter = LancamentoAdapter(
        onTogglePago = ::alternarPago,
        onItemClick = ::abrirDetalhe
    )

    private var lancamentos: List<Lancamento> = MockData.lancamentos
    private var filtroAtivo: Filtro = Filtro.TODOS

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLancamentosBinding.bind(view)

        binding.rvLancamentos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLancamentos.adapter = adapter

        binding.tabTodos.setOnClickListener { selecionarFiltro(Filtro.TODOS) }
        binding.tabReceitas.setOnClickListener { selecionarFiltro(Filtro.RECEITAS) }
        binding.tabDespesas.setOnClickListener { selecionarFiltro(Filtro.DESPESAS) }

        atualizarTela()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selecionarFiltro(filtro: Filtro) {
        if (filtro == filtroAtivo) return
        filtroAtivo = filtro
        atualizarAbas()
        atualizarLista()
    }

    private fun alternarPago(lancamento: Lancamento) {
        lancamentos = lancamentos.map {
            if (it.id == lancamento.id) it.copy(pago = !it.pago) else it
        }
        atualizarTela()
    }

    private fun abrirDetalhe(lancamento: Lancamento) {
        val intent = Intent(requireContext(), DetalheActivity::class.java).apply {
            putExtra(DetalheActivity.EXTRA_LANCAMENTO, lancamento)
        }
        startActivity(intent)
    }

    private fun atualizarTela() {
        atualizarTotais()
        atualizarLista()
    }

    private fun atualizarLista() {
        val listaFiltrada = when (filtroAtivo) {
            Filtro.TODOS -> lancamentos
            Filtro.RECEITAS -> lancamentos.filter { it.tipo == TipoLancamento.RECEITA }
            Filtro.DESPESAS -> lancamentos.filter { it.tipo == TipoLancamento.DESPESA }
        }
        adapter.submitList(listaFiltrada)
    }

    private fun atualizarTotais() {
        val pagos = lancamentos.filter { it.pago }
        val entradas = pagos.filter { it.tipo == TipoLancamento.RECEITA }.sumOf { it.valor }
        val saidas = pagos.filter { it.tipo == TipoLancamento.DESPESA }.sumOf { it.valor }
        val saldo = entradas - saidas

        binding.tvSaldo.text = formatarMoeda(saldo)
        binding.tvEntradas.text = formatarMoeda(entradas)
        binding.tvSaidas.text = formatarMoeda(saidas)
    }

    private fun atualizarAbas() {
        val corAtiva = ContextCompat.getColor(requireContext(), R.color.lavanda)
        val corInativa = ContextCompat.getColor(requireContext(), R.color.texto_apoio)

        binding.tvTabTodos.setTextColor(if (filtroAtivo == Filtro.TODOS) corAtiva else corInativa)
        binding.underlineTodos.visibility = if (filtroAtivo == Filtro.TODOS) View.VISIBLE else View.INVISIBLE

        binding.tvTabReceitas.setTextColor(if (filtroAtivo == Filtro.RECEITAS) corAtiva else corInativa)
        binding.underlineReceitas.visibility = if (filtroAtivo == Filtro.RECEITAS) View.VISIBLE else View.INVISIBLE

        binding.tvTabDespesas.setTextColor(if (filtroAtivo == Filtro.DESPESAS) corAtiva else corInativa)
        binding.underlineDespesas.visibility = if (filtroAtivo == Filtro.DESPESAS) View.VISIBLE else View.INVISIBLE
    }
}
