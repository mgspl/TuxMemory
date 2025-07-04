### Tux Memory 

Projeto desenvolvido na disciplina Manutenção de Software 2025.1

![Menu](assets/Screenshot_20250701-174327.Memory.png)
![Jogo](assets/Screenshot_20250701-174340.Memory.png)
![Final](assets/Screenshot_20250701-174754.Memory.png)

### Mudanças realizadas

* Os Tuxes (pinguins) foram alterados e a quantidade foi aumentada
* Foi adicionado um tempo mínimo para realizar jogadas
* Foi adcionado um modo infinito
* Os sons foram alterados
* Foi atualizado a versão do Java para 21
* Foi atualizado a versão do Gradle para 8.13
* Foi adicionado uma Github Action para compilar uma release do APP
* Foi adicionado um nix shell que contém o Android Studio e as depedencias para compilar o APP

### Como rodar (nix)

* Clone o projeto
```bash
  git clone https://github.com/mgspl/TuxMemory.git
```
* Entre no Nix Shell
```bash
nix develop
```
O Android Studio e as depedencias do projeto serão baixadas automaticamente.

### Como rodar
* Clone o projeto
```bash
  git clone https://github.com/mgspl/TuxMemory.git
```
Configure o Android Studio para as versão 35 de API do Android

### Github Action

* Está configurada para rodar a cada push na master
* Gere uma nova keystore.jks de acordo com a documentação do Android
* Configure a Senha, Alias, e Keystore Password para Secrets de acordo com o nome no build.gradle (Caso queira alterar mude também no yaml da action)
* Converta a keystore.jks para base64 (A aba de Secrets do Github só aceita texto)
* Pode ser necessário desinstalar o TuxMemory pelo adb para instalar novamente
  
```bash
  adb uninstall org.androidsoft.games.memory.tux
```

### Problemas conhecidos
* Ao clicar rapidamente algumas vezes na mesma carta e clicar numa carta nova a primeira carta não será mais reconhecida e não será possível terminar o jogo
* Tem dois Batmans

### Créditos 
* Pierre Levy (Projeto original)
* Crystal XP e Tux Factory (Diferentes tuxes) Acessado pelo [link](https://archive.org/details/crystalxp-tux-collection/)
* Universfield (Efeitos sonoros) Acessado pelo [link](https://pixabay.com/users/universfield-28281460/)
