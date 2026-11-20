# Xbox Control

Aplicativo Android experimental com painel flutuante e envio de gestos de toque pelo Serviço de Acessibilidade.

## Requisitos
- Android 8.0 (API 26) ou superior.
- Permissão de sobreposição.
- Serviço Xbox Control ativado em Configurações > Acessibilidade.

## Compilar
Use JDK 17 e Gradle 8.7 ou superior. Execute gradle assembleDebug.
O GitHub Actions publica o artefato Xbox-Control, contendo o arquivo Xbox-Control.apk.

## Testar
1. Instale o APK.
2. Abra Xbox Control e conceda a permissão de sobreposição.
3. Ative o serviço em Configurações > Acessibilidade.
4. Inicie o controle flutuante.
5. Teste primeiro em um ambiente seguro. Os pontos de toque são posições predefinidas nesta versão.

## Limitações
- Não é um controle Bluetooth, HID ou XInput.
- Os botões enviam toques em posições fixas da tela; esta versão ainda não oferece calibração visual.
- O painel ocupa sua própria área e pode interceptar toques nessa região.
- Roblox, Android e fabricantes podem bloquear ou limitar gestos de acessibilidade em jogos. Compatibilidade não é garantida.
- Uma compilação bem-sucedida confirma apenas que o código compilou; o teste físico precisa ser feito no dispositivo.

Use somente em aplicativos e jogos nos quais esse tipo de automação seja permitido.
