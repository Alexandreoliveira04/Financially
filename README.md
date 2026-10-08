# Financially

App Android nativo de controle financeiro pessoal. Mostra os lançamentos do mês, o saldo real (considerando só o que já foi pago) e o andamento de compras parceladas.

## Funcionalidades

- Lista de receitas e despesas do mês, com filtro por tipo
- Marcar um lançamento como pago com um toque, com atualização imediata dos totais
- Tela de detalhe com valor, categoria, conta, situação e parcela atual
## Como rodar

**Requisitos**

- Android Studio Ladybug ou mais recente
- Android SDK 37 (instalado pelo SDK Manager)
- JDK 11+ (o JBR do Android Studio já atende)
- Emulador ou aparelho com Android 8.0+ (API 26)

**Passos**

1. Clone o repositório e abra a pasta no Android Studio (`File > Open`).
2. Aguarde o Gradle sincronizar.
3. Selecione um emulador ou aparelho e clique em **Run ▶**.


Não é preciso chave de API nem configuração extra. Os dados ficam em `data/MockData.kt`.

## Tecnologias

- Kotlin
- AndroidX: Core KTX, AppCompat, Lifecycle, ConstraintLayout, RecyclerView
- Material Components (tema Material 3 Dark)
- JUnit e Espresso (testes padrão do template)


## Estrutura do projeto

```
app/src/main/java/com/financially/
├── MainActivity.kt              # Hospeda o LancamentosFragment num FragmentContainerView
├── data/
│   ├── Lancamento.kt             # Modelos imutáveis (data class): Lancamento, Categoria, Conta, TipoLancamento
│   └── MockData.kt               # 13 lançamentos simulados de setembro/2026
├── ui/
│   ├── lancamentos/
│   │   ├── LancamentosFragment.kt  # Tela 1: lista, filtros, totais do cabeçalho
│   │   └── LancamentoAdapter.kt    # ListAdapter + DiffUtil, bind dos itens
│   └── detalhe/
│       └── DetalheActivity.kt      # Tela 2: detalhe do lançamento, recebido via Intent
└── util/
    └── Formatadores.kt           # Formatação de moeda (pt-BR) e data

app/src/main/res/
├── layout/                       # XMLs das telas e componentes reutilizáveis
│   ├── activity_main.xml
│   ├── fragment_lancamentos.xml
│   ├── item_lancamento.xml
│   ├── activity_detalhe.xml
│   ├── view_pilula.xml           # Componente reutilizável, inflado programaticamente
│   └── view_linha_chave_valor.xml
├── values/                       # colors.xml, dimens.xml, styles.xml (tokens do design system)
├── drawable/                     # Formas (cards, bolinha de situação, pílulas, barra de progresso)
├── drawable-nodpi/               # Ícones de categoria (Icons8), PNG recolorido em tempo de execução
└── font/                         # Outfit e JetBrains Mono (Google Fonts), baixadas em .ttf
```

## Referências

- Ícones de categoria por [Icons8](https://icons8.com.br/)
- Fontes Outfit e JetBrains Mono, do [Google Fonts](https://fonts.google.com/)