# Financially

## Objetivo do aplicativo

Financially é um aplicativo Android nativo de controle financeiro pessoal. O problema que ele resolve é simples: a maioria das pessoas perde o controle do próprio dinheiro não por falta de renda, mas por falta de visibilidade — não sabe quanto já pagou no mês, quanto ainda falta pagar, nem quanto sobra depois das contas fixas. O app organiza os lançamentos (receitas e despesas) de um mês, mostra o saldo real (considerando apenas o que já foi pago) e permite marcar contas como pagas com um toque, atualizando os totais na hora.

Cada lançamento carrega os dados que importam no dia a dia: valor, categoria, conta de origem, se já foi pago, e se faz parte de uma compra parcelada — nesse caso o app mostra em qual parcela o usuário está e quanto falta. Isso resolve uma dor comum de compras no cartão de crédito, onde é fácil perder a noção de quantas parcelas já foram pagas de cada compra.

Esta entrega cobre o núcleo do fluxo diário: ver os lançamentos do mês, filtrar por tipo, marcar como pago, e abrir o detalhe de um lançamento específico para ver todas as informações, incluindo o progresso do parcelamento.

## Como rodar localmente

**Pré-requisitos:**
- Android Studio (Ladybug ou mais recente) com Android SDK 37 (compileSdk/targetSdk) instalado via SDK Manager
- JDK 11 ou superior (o JBR embutido no Android Studio já resolve isso automaticamente)
- minSdk 26 (Android 8.0) — qualquer emulador ou aparelho físico a partir dessa versão roda o app

**Passo a passo:**
1. Clone o repositório.
2. Abra a pasta clonada no Android Studio (`File > Open`).
3. Aguarde o Gradle sincronizar (baixa as dependências automaticamente na primeira vez).
4. Selecione um emulador ou conecte um aparelho físico com depuração USB habilitada.
5. Clique em **Run ▶** (ou `Shift+F10`).

Não é necessário nenhum arquivo de configuração extra, chave de API, `local.properties` customizado ou variável de ambiente. O app não acessa rede nem serviços externos — todos os dados são mockados em memória (`data/MockData.kt`). Também é possível compilar via linha de comando com `./gradlew assembleDebug` (Linux/macOS) ou `gradlew.bat assembleDebug` (Windows), a partir da raiz do projeto.

## Bibliotecas externas

- **AndroidX Core KTX** — extensões Kotlin para APIs do Android.
- **AndroidX AppCompat** — `AppCompatActivity` e compatibilidade de tema/recursos em versões antigas do Android.
- **AndroidX Lifecycle Runtime KTX** — suporte a ciclo de vida com coroutines/KTX.
- **AndroidX ConstraintLayout** — layout usado no item da lista e nas telas para posicionamento flexível.
- **AndroidX RecyclerView** — lista de lançamentos com `ListAdapter` e `DiffUtil`.
- **Material Components for Android** — `MaterialToolbar`, tema `Theme.Material3.Dark.NoActionBar` e demais componentes visuais.
- **JUnit / AndroidX Test (Espresso, JUnit Ext)** — dependências de teste geradas pelo template padrão do Android Studio, usadas apenas nos testes de exemplo.

Não há Firebase, Room, Retrofit, Hilt/Dagger/Koin ou qualquer biblioteca de rede — o app funciona 100% offline com dados simulados.

### Ícones das categorias

Os ícones de categoria (mercado, moradia, transporte, etc.) vêm do [Icons8](https://icons8.com.br/), estilo `ios-filled`, baixados como PNG em `res/drawable-nodpi/` e recoloridos em tempo de execução (`setColorFilter`) com a cor de cada categoria. Foram obtidos pelo CDN público do Icons8, no formato:

```
https://img.icons8.com/ios-filled/192/FFFFFF/<nome-do-icone>.png
```

Esse CDN é de uso livre sem necessidade de conta ou chave de API, mas está sob a licença gratuita **Linkware** do Icons8: o uso é permitido desde que haja um link visível para icons8.com em algum lugar acessível a partir do produto (é isso que a linha abaixo cumpre). Para remover essa exigência de atribuição, o Icons8 vende planos pagos (a "Universal Multimedia Licensing") com acesso à API oficial e aos arquivos SVG/vetoriais originais — sem eles, o download direto de `.svg` retorna 403, só o `.png` do CDN público é servido sem autenticação.

Ícones usados: `briefcase` (Salário), `shopping-cart` (Mercado), `home` (Moradia), `restaurant` (Alimentação), `bus` (Transporte), `iphone` (Assinaturas), `laptop` (Eletrônicos), `clapperboard` (Lazer) — todos por [Icons8](https://icons8.com.br/).

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

## Escopo desta entrega

Esta é a entrega **Parcial**, que exige Android Views com layouts XML (sem Jetpack Compose). Ela implementa duas telas — **Lançamentos do mês** e **Detalhe do lançamento** — conectadas por Intent explícita com passagem de um objeto `Parcelable`, usando exclusivamente ViewBinding (sem `findViewById`), com dados simulados em memória (`MockData`), sem API e sem banco de dados. A tela de lançamentos roda dentro de um Fragment com ciclo de vida próprio. Uma etapa futura reescreverá o aplicativo em Jetpack Compose.

### Desvios e decisões técnicas

- **`kotlin-parcelize` não pôde ser usado.** O template gerado pelo Android Studio para este projeto vem com uma versão do Android Gradle Plugin (9.3.2) que usa o **suporte nativo a Kotlin embutido no próprio AGP** (não aplica o plugin clássico `org.jetbrains.kotlin.android`). O plugin `kotlin-parcelize` depende da infraestrutura do Kotlin Gradle Plugin clássico para se registrar como compiler plugin, e essa combinação não compilou neste ambiente (testado e confirmado: aplicar `org.jetbrains.kotlin.android` junto com o suporte nativo do AGP gera conflito de configuração; desabilitar o suporte nativo via `android.builtInKotlin=false` também falha, pois o `kotlin-android` 2.2.10 não é compatível com as novas APIs de extensão do AGP 9.3.2). Diante disso, os três modelos `Parcelable` (`Categoria`, `Conta`, `Lancamento`) foram implementados manualmente (`writeToParcel`/`CREATOR`), sem alterar o restante do contrato pedido (ainda são `data class` imutáveis com tratamento correto de opcionais).
- **Fontes Outfit e JetBrains Mono** foram baixadas diretamente do Google Fonts (pesos 400/500/600) e cada peso foi registrado como uma família separada em `res/font` (ex.: `familia_outfit_semibold.xml`), em vez de uma única família com múltiplos pesos selecionados por `android:fontWeight` — essa seleção por peso só é confiável a partir da API 28, e o projeto declara `minSdk 26`.
- **`texto_apoio` foi ajustado de `#6C6A7C` para `#827F95`** (mesma tonalidade, mais claro). O valor original, medido contra `superficie` (#17171D), resulta em contraste de ~3,39:1 — abaixo dos 4,5:1 exigidos pela Tarefa 6 para texto pequeno (`Metadado`, 10.5sp, e `RotuloMono`, 10sp, usam essa cor). O novo valor mede ~4,61:1 contra `superficie` e ~4,95:1 contra `fundo`, atendendo à exigência de contraste sem mudar a paleta perceptualmente.
- **Toolbar da tela de detalhe usa `fitsSystemWindows`** para não desenhar sob a barra de status, já que o `targetSdk 37` força o layout edge-to-edge por padrão a partir do Android 15.
- O `FragmentContainerView` de `MainActivity` referencia `LancamentosFragment` diretamente via `android:name`, dispensando código adicional de transação de fragmento na Activity.
- **Emojis de categoria trocados por ícones do Icons8.** O enunciado original dizia para manter os emojis provisórios até uma etapa futura com ícones vetoriais; a pedido do desenvolvedor, essa troca foi antecipada para esta entrega. `Categoria.emoji: String` virou `Categoria.iconeRes: Int` (referência a um drawable), sem mudar o restante do contrato do modelo. Ver a seção "Ícones das categorias" acima para a origem e a licença dos ícones.

### O que não foi implementado (fora do escopo desta entrega)

Telas como Início, Contas, Categorias, Orçamento e o fluxo de "Novo lançamento" aparecem na referência visual completa do projeto, mas pertencem a entregas futuras — esta entrega cobre apenas as duas telas exigidas pelo enunciado (Lançamentos do mês e Detalhe do lançamento).
