# Na volta nóis compra

Fundação mínima Kotlin Multiplatform para **Android** e **Linux Desktop JVM**,
com UI compartilhada em Compose Multiplatform e **Material 3 Expressive**.

A única tela demonstra título, alternância entre claro e escuro e um botão
Expressive que conta cliques. A largura do conteúdo é limitada no desktop,
preenche telas menores e permite rolagem em janelas baixas. O estado usa
`rememberSaveable`; não há persistência entre execuções do aplicativo.

## Versões

| Componente | Versão |
| --- | --- |
| Kotlin Multiplatform e Compose Compiler | 2.2.21 |
| Compose Multiplatform | 1.10.3 |
| Material 3 Multiplatform | 1.10.0-alpha05 |
| Material 3 Android, selecionado pelo artefato multiplataforma | 1.5.0-alpha08 |
| Android Gradle Plugin | 8.11.1 |
| Gradle Wrapper | 8.13 |
| JDK e bytecode JVM | 17 |
| Android compileSdk / targetSdk / minSdk | 36 / 36 / 24 |
| AndroidX Activity Compose | 1.10.1 |
| kotlinx-coroutines-swing | 1.10.2 |

As versões das dependências ficam em `gradle/libs.versions.toml`; a distribuição
Gradle e seu SHA-256 ficam em `gradle/wrapper/gradle-wrapper.properties`.

### Compatibilidade e Expressive

- Kotlin 2.2.21 suporta AGP até 8.11.1 e Gradle até 8.14, conforme a
  [matriz oficial](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-compatibility-guide.html#version-compatibility).
- AGP 8.11.1 suporta API 36 e requer Gradle 8.13 e JDK 17:
  [notas oficiais do Android](https://developer.android.com/build/releases/past-releases/agp-8-11-0-release-notes).
- Compose 1.10.3 e Material 3 1.10.0-alpha05 aparecem juntos nas
  [notas oficiais do Compose](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.10.3).
  O [POM do Material 3](https://repo.maven.apache.org/maven2/org/jetbrains/compose/material3/material3/1.10.0-alpha05/material3-1.10.0-alpha05.pom)
  declara Kotlin 2.2.20, atendido pelo Kotlin 2.2.21 escolhido.
- A exceção ao uso de versões estáveis é Material 3: na versão estável 1.9.0,
  `MaterialExpressiveTheme` é interno. A linha alpha escolhida publica esse
  tema e o botão com `shapes`. Não há substituição por Material 3 convencional.
- `Theme.kt` centraliza esquemas de cores, `Typography`, `Shapes` e
  `MotionScheme.expressive()`. `App.kt` usa `ButtonDefaults.shapes()` para a
  transformação de forma ao pressionar. Os únicos opt-ins são
  `ExperimentalMaterial3ExpressiveApi`, localizados nos usos necessários.

## Estrutura e decisões

```text
.
├── AGENTS.md
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── composeApp/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/com/carlaun12/navoltanoiscompra/
│       │   ├── App.kt
│       │   ├── Counter.kt
│       │   └── Theme.kt
│       ├── commonTest/kotlin/com/carlaun12/navoltanoiscompra/CounterTest.kt
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/com/carlaun12/navoltanoiscompra/MainActivity.kt
│       └── desktopMain/kotlin/com/carlaun12/navoltanoiscompra/main.kt
└── docs/
    ├── AGENTS.md
    └── VALIDACAO.md
```

Um único módulo `composeApp` contém os targets `androidTarget()` e
`jvm("desktop")`. A referência é o
[template do tutorial oficial JetBrains](https://github.com/kotlin-hands-on/get-started-with-cm/tree/e5346eac6a6093cafe2701c13696f85785209380),
que utiliza `composeApp`, `App()`, `MainActivity` e `desktopMain/main.kt`.
Foi mantida essa organização de módulo único com AGP 8.x, suportada pelas
versões fixadas, removendo targets e dependências que esta base não utiliza.
O template atual com AGP 9 separa entry points em módulos; uma atualização
para essa linha deve tratar a migração explicitamente.

- **KISS / YAGNI:** estado local Compose, sem MVVM, DI, navegação ou camadas.
- **SOLID:** UI em `App.kt`, design em `Theme.kt` e uma função pura de incremento
  em `Counter.kt`, separada somente para permitir o teste.
- **DRY:** UI e lógica em `commonMain`; Android e desktop só conectam `App()`
  às APIs de Activity e janela JVM. Não há `expect`/`actual` sem necessidade.
- `commonTest` usa `kotlin.test` e é executado pelos dois targets JVM.
- `kotlinx-coroutines-swing` fornece o dispatcher de UI do desktop, conforme
  o template. Não há frameworks adicionais de arquitetura.
- A licença existente está preservada em `LICENSE`.

## Preparar o ambiente

Use JDK 17, conexão para baixar dependências e um Android SDK configurado.
No Linux, use uma sessão gráfica e as bibliotecas de sistema exigidas pelo
Compose Desktop (por exemplo, Ubuntu 20.04+ x86-64 ou arm64 com OpenGL).

```bash
java -version
export ANDROID_HOME="$HOME/Android/Sdk"
sdkmanager --licenses
sdkmanager "platforms;android-36" "build-tools;35.0.0" "platform-tools"
```

`sdkmanager` pertence ao Android SDK Command-line Tools. Ajuste `JAVA_HOME`
para o JDK 17 e `ANDROID_HOME` para seu SDK. Como alternativa à variável do
SDK, crie `local.properties` com `sdk.dir=/caminho/absoluto/do/sdk`; esse
arquivo é ignorado pelo Git. O SDK é necessário também para a configuração
Gradle do módulo único ao executar tarefas desktop.

## Executar Android

No Android Studio, abra a raiz, sincronize o Gradle e execute `composeApp`
em um dispositivo/emulador com Android 7.0 (API 24) ou superior.

Pelo terminal, com um dispositivo conectado:

```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug
"$ANDROID_HOME/platform-tools/adb" shell am start -n \
  com.carlaun12.navoltanoiscompra/.MainActivity
```

APK: `composeApp/build/outputs/apk/debug/composeApp-debug.apk`.

## Executar Linux Desktop

```bash
./gradlew :composeApp:run
```

O entry point é `com.carlaun12.navoltanoiscompra.MainKt`. Esse comando abre
uma janela JVM independente de dispositivo ou emulador Android.

Para compilar sem abrir uma janela:

```bash
./gradlew :composeApp:desktopJar
```

O JAR é um artefato de compilação; o comando `run` configura o classpath e
as bibliotecas nativas necessárias para executar a aplicação.

## Validar

```bash
./gradlew :composeApp:assembleDebug :composeApp:desktopJar \
  :composeApp:testDebugUnitTest :composeApp:desktopTest
git diff --check
```

Os testes locais dos dois targets reutilizam `CounterTest` de `commonTest`.
Não exigem emulador nem uma sessão gráfica. Relatórios:

- `composeApp/build/reports/tests/testDebugUnitTest/index.html`
- `composeApp/build/reports/tests/desktopTest/index.html`

Após compilar, confira manualmente os dois temas, o contador, rotação Android
e redimensionamento da janela desktop. Os resultados realmente obtidos na
criação desta base estão em [docs/VALIDACAO.md](docs/VALIDACAO.md).
