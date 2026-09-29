package com.financially.util

import com.financially.data.TipoLancamento
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

private val localePtBr: Locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()
private val formatoMoeda: NumberFormat = NumberFormat.getCurrencyInstance(localePtBr)
private val formatoData: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun formatarMoeda(valor: Double): String = formatoMoeda.format(valor)

fun formatarMoedaComSinal(valor: Double, tipo: TipoLancamento): String {
    val sinal = if (tipo == TipoLancamento.RECEITA) "+" else "-"
    return sinal + formatoMoeda.format(abs(valor))
}

fun formatarData(data: LocalDate): String = data.format(formatoData)
