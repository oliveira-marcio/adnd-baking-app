# Udacity Android Developer Nanodegree - Projeto 4 (Baking App)

Este é um app que consulta uma [API de receitas da Udacity](http://go.udacity.com/android-baking-app-json) para exibir uma lista de receitas e a video-lista de passos necessários para preparar a receita selecionada.

Características do app:
- Layout responsivo
  - Celulares - usa o fluxo padrão Master/Detail (primeiro a lista de receitas depois os detalhes)
  - Tablets - usa 2 painéis (lista de receitas à esquerda e detalhes à direita)
- `AppWidget` que exibe a lista de ingredientes da última receita aberta no app
- Reprodução de vídeos por _streaming_ usando a biblioteca [ExoPlayer](https://github.com/google/ExoPlayer) com a possibilidade de exibí-los em tela cheia
- Implementação de alguns testes [Espresso](https://developer.android.com/training/testing/espresso/).

O app também utiliza a biblioteca [Glide](https://bumptech.github.io/glide/) para exibição e cache de _thumbnails_ dos vídeos.

## Instalação:
- Faça um clone do repositório
- Importe a pasta como um novo projeto no [Android Studio](https://developer.android.com/studio/)
- Configure um [emulador](https://developer.android.com/studio/run/emulator) ou conecte um [celular com USB debug ativado](https://developer.android.com/studio/run/device)
- Execute apartir do menu "Run"

## Copyright

Esse projeto foi desenvolvido por Márcio Souza de Oliveira em 13/07/2017.
