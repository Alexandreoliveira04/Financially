package com.financially.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

private inline fun <reified T : Parcelable> Parcel.lerParcelable(): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readParcelable(T::class.java.classLoader, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        readParcelable(T::class.java.classLoader)
    }
}

enum class TipoLancamento { RECEITA, DESPESA }

data class Categoria(
    val id: String,
    val nome: String,
    val iconeRes: Int,
    val corRes: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = requireNotNull(parcel.readString()),
        nome = requireNotNull(parcel.readString()),
        iconeRes = parcel.readInt(),
        corRes = parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(nome)
        dest.writeInt(iconeRes)
        dest.writeInt(corRes)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Categoria> {
        override fun createFromParcel(parcel: Parcel) = Categoria(parcel)
        override fun newArray(size: Int): Array<Categoria?> = arrayOfNulls(size)
    }
}

data class Conta(
    val id: String,
    val nome: String,
    val tipo: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = requireNotNull(parcel.readString()),
        nome = requireNotNull(parcel.readString()),
        tipo = requireNotNull(parcel.readString())
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(nome)
        dest.writeString(tipo)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Conta> {
        override fun createFromParcel(parcel: Parcel) = Conta(parcel)
        override fun newArray(size: Int): Array<Conta?> = arrayOfNulls(size)
    }
}

data class Lancamento(
    val id: String,
    val descricao: String,
    val valor: Double,
    val tipo: TipoLancamento,
    val data: LocalDate,
    val categoria: Categoria,
    val conta: Conta,
    val pago: Boolean,
    val observacao: String? = null,
    val parcelaAtual: Int? = null,
    val totalParcelas: Int? = null
) : Parcelable {

    val parcelamento: Pair<Int, Int>?
        get() = if (parcelaAtual != null && totalParcelas != null) {
            parcelaAtual to totalParcelas
        } else {
            null
        }

    constructor(parcel: Parcel) : this(
        id = requireNotNull(parcel.readString()),
        descricao = requireNotNull(parcel.readString()),
        valor = parcel.readDouble(),
        tipo = TipoLancamento.valueOf(requireNotNull(parcel.readString())),
        data = LocalDate.ofEpochDay(parcel.readLong()),
        categoria = requireNotNull(parcel.lerParcelable()),
        conta = requireNotNull(parcel.lerParcelable()),
        pago = parcel.readByte() != 0.toByte(),
        observacao = parcel.readString(),
        parcelaAtual = parcel.readValue(Int::class.java.classLoader) as? Int,
        totalParcelas = parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(descricao)
        dest.writeDouble(valor)
        dest.writeString(tipo.name)
        dest.writeLong(data.toEpochDay())
        dest.writeParcelable(categoria, flags)
        dest.writeParcelable(conta, flags)
        dest.writeByte(if (pago) 1 else 0)
        dest.writeString(observacao)
        dest.writeValue(parcelaAtual)
        dest.writeValue(totalParcelas)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Lancamento> {
        override fun createFromParcel(parcel: Parcel) = Lancamento(parcel)
        override fun newArray(size: Int): Array<Lancamento?> = arrayOfNulls(size)
    }
}
