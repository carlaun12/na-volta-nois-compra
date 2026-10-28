# Validação da fundação

Data: 27/10/2026.

## Ambiente

- Host: Android/Termux, Linux aarch64. Não é uma sessão Linux Desktop com glibc.
- O ambiente inicialmente não tinha JDK nem Android SDK.
- OpenJDK 17.0.21 e AAPT2 nativo do Termux (`2.20-android-16.0.0_r4`)
  foram instalados para executar as validações.
- Android SDK Command-line Tools 19.0, plataforma 36 revisão 2 e Build Tools
  35.0.0 foram instalados com sucesso.
- SDK e caches são locais ao ambiente e não fazem parte do repositório.

## Compatibilidade das APIs Expressive

Foram baixados e inspecionados os arquivos de fontes publicados:

- [Material 3 Desktop 1.9.0](https://repo.maven.apache.org/maven2/org/jetbrains/compose/material3/material3-desktop/1.9.0/material3-desktop-1.9.0-sources.jar):
  `MaterialExpressiveTheme` é `internal`, portanto não atende ao projeto.
- [Material 3 Desktop 1.10.0-alpha05](https://repo.maven.apache.org/maven2/org/jetbrains/compose/material3/material3-desktop/1.10.0-alpha05/material3-desktop-1.10.0-alpha05-sources.jar)
  e [Material 3 Android 1.5.0-alpha08](https://dl.google.com/dl/android/maven2/androidx/compose/material3/material3-android/1.5.0-alpha08/material3-android-1.5.0-alpha08-sources.jar):
  ambos publicam as APIs utilizadas, com `ExperimentalMaterial3ExpressiveApi`.

Declarações conferidas em ambos os artefatos:

| Arquivo da biblioteca | API |
| --- | --- |
| `MaterialTheme.kt` | `MaterialExpressiveTheme` |
| `ColorScheme.kt` | `expressiveLightColorScheme` |
| `MotionScheme.kt` | `MotionScheme.expressive` |
| `Button.kt` | `Button(onClick, shapes, …)` e `ButtonDefaults.shapes` |
| `Button.kt` | `MediumContainerHeight`, `contentPaddingFor`, `textStyleFor` |

As notas de versão e a matriz de compatibilidade usadas para escolher as
versões estão referenciadas no `README.md`. Inspecionar fontes comprova a
presença das APIs; os resultados de compilação são registrados separadamente.

## Verificações já executadas

- `bash -n gradlew`: passou.
- SHA-256 de `gradle-wrapper.jar`: coincide com o checksum oficial do Gradle 8.13,
  `81a82aaea5abcc8ff68b3dfcb58b3c3c429378efd98e7433460610fecd7ae45f`.
- SHA-256 da distribuição foi consultado na origem oficial e fixado no wrapper.
- Catálogo TOML e referências de versão: leitura e verificação por Python,
  sem erros.
- Manifest Android: XML analisado por Python, sem erros.
- Permissão executável de `gradlew`: conferida.
- `./gradlew --version --no-daemon --console=plain`: passou; baixou a
  distribuição com verificação de checksum e iniciou Gradle 8.13 no JDK 17.0.21.
- Links locais da documentação e classe principal do JAR do wrapper: conferidos.
- `git diff --check` e inspeção de whitespace dos novos arquivos de texto:
  sem erros.

## Gradle

Comando iniciado para compilar os dois targets e executar seus testes:

```bash
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export ANDROID_HOME=/data/data/com.termux/files/home/.local/share/android-sdk
./gradlew :composeApp:assembleDebug :composeApp:desktopJar \
  :composeApp:testDebugUnitTest :composeApp:desktopTest \
  --continue --no-daemon --console=plain --max-workers=1 \
  '-Dorg.gradle.jvmargs=-Xmx1024m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8' \
  -Pkotlin.compiler.execution.strategy=in-process \
  -Pandroid.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2
```

O override do AAPT2 utiliza o executável Android/aarch64 do Termux porque
o binário Linux distribuído pelo Google é x86-64/glibc. Esse caminho é apenas
um argumento local de validação, não uma configuração fixa do projeto.
Os limites de workers e memória também são ajustes locais deste host.

O resultado da tentativa será registrado após sua conclusão.

## Validações manuais

A abertura dos aplicativos, a interação visual, a rotação Android e o
redimensionamento em uma sessão Linux Desktop ainda não foram validados.
