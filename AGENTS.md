# Orientações para agentes

- Aplique KISS: prefira a solução mais simples que resolva o pedido atual.
- Aplique SOLID de forma proporcional: responsabilidades claras, sem criar
  interfaces ou camadas apenas para seguir um padrão.
- Aplique YAGNI: adicione funcionalidades, módulos e dependências somente
  quando necessários. Não introduza MVVM, DI ou navegação automaticamente.
- Aplique DRY: compartilhe UI e lógica em `composeApp/src/commonMain`;
  mantenha APIs de plataforma em `androidMain` e `desktopMain`.
- Preserve as convenções do template JetBrains: `composeApp`, `App`,
  `MainActivity`, `main.kt` e source sets oficiais.
- Centralize versões em `gradle/libs.versions.toml` e o design em `Theme.kt`.
- Use Material 3 Expressive real; restrinja opt-ins às APIs Expressive que
  os exigem e confira sua disponibilidade antes de alterar versões.
- Use `kotlin.test` em `commonTest` para lógica compartilhada relevante.
- Valide com `./gradlew :composeApp:assembleDebug :composeApp:desktopJar :composeApp:testDebugUnitTest :composeApp:desktopTest` e `git diff --check`.
  Registre comandos, resultados e impedimentos; nunca declare uma verificação
  como executada sem tê-la executado.
- Mantenha o Gradle Wrapper completo no Git, incluindo o JAR e `gradlew`
  executável. Atualize a documentação em português junto com o projeto.
