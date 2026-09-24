# Bolsa de Vagas para Estágio

Aplicativo Android (Kotlin) desenvolvido para a disciplina de Qualidade e Teste de Software (Turma 3 DS-AMS).

O app gerencia o processo de encaminhamento de alunos para estágio: cadastro de **empresas parceiras**, seus **requisitos técnicos**, os **alunos encaminhados** para cada empresa e o **retorno/resultado** de cada encaminhamento. Todas as informações são gravadas em tempo real no **Firebase Firestore**.

## Funcionalidades (CRUD completo)

| Entidade | Criar | Ler | Atualizar | Excluir |
|---|---|---|---|---|
| Empresas parceiras | ✅ | ✅ (lista em tempo real) | ✅ | ✅ |
| Requisitos técnicos (por empresa) | ✅ | ✅ | ✅ | ✅ |
| Alunos encaminhados | ✅ | ✅ | ✅ | ✅ |
| Retornos das empresas | ✅ | ✅ | ✅ | ✅ |

- Tela **Empresas**: lista as empresas parceiras. Ao tocar em uma empresa, abre o detalhe com os requisitos técnicos exigidos por ela (CRUD independente).
- Tela **Alunos**: lista os alunos encaminhados, vinculados a uma empresa, com curso, matrícula, data e status.
- Tela **Retornos**: lista os retornos recebidos das empresas para cada aluno encaminhado (aprovado, reprovado, aguardando, etc).

## Identidade visual

Paleta em tons de azul corporativo (`#1A237E`) com laranja de destaque (`#FF6F00`), remetendo a um ambiente profissional/institucional de estágios, com cards, chips de status coloridos e ícones de maleta, formatura e "check" nas abas de navegação inferior.

## Tecnologias

- Kotlin
- Android Views + ViewBinding
- Navegação por `BottomNavigationView` + `Fragment`
- `RecyclerView` com `MaterialCardView`
- **Firebase Firestore** (`addSnapshotListener` para listas em tempo real)
- Material Components 3

## Estrutura das coleções no Firestore

```
empresas
  ├─ nome, cnpj, area, contato, telefone, email, endereco

requisitos
  ├─ empresaId, descricao, nivel

alunos_encaminhados
  ├─ empresaId, empresaNome, nomeAluno, curso, matricula, dataEncaminhamento, status

retornos
  ├─ alunoId, alunoNome, empresaId, empresaNome, resultado, observacao, data
```

## Como configurar o Firebase (passo a passo)

1. Acesse https://console.firebase.google.com e crie um novo projeto (ex: `BolsaDeVagas`).
2. Dentro do projeto, clique em **Adicionar app > Android**.
3. Informe o package name exatamente como no projeto: `com.example.bolsadevagas`.
4. Baixe o arquivo **`google-services.json`** gerado e coloque-o na pasta `app/` do projeto (ao lado de `build.gradle.kts` do módulo app).
5. No menu lateral do Firebase, vá em **Firestore Database > Criar banco de dados** e escolha **Iniciar em modo de teste** (permite leitura/escrita livre durante o desenvolvimento).
6. Sincronize o projeto no Android Studio (`File > Sync Project with Gradle Files`).
7. Rode o app em um emulador ou dispositivo físico — os dados aparecerão em **Firestore Database** no console conforme você cadastra empresas, alunos e retornos.

> As dependências do Firebase (BOM + Firestore + plugin `google-services`) já estão configuradas nos arquivos `build.gradle.kts`. Só falta o `google-services.json` do seu próprio projeto Firebase.

## Como rodar

1. Extraia o `.zip` deste projeto substituindo os arquivos do projeto criado no Android Studio (`New Project > Empty Views Activity`, Kotlin, package `com.example.bolsadevagas`, API 24).
2. Adicione o `google-services.json` (passo acima).
3. `Sync Gradle` e execute (`Run > Run 'app'`).

## Entrega da atividade

- [ ] Link do repositório GitHub (com este README)
- [ ] Link do vídeo mostrando: criação do projeto, telas do app funcionando (CRUD de empresas, requisitos, alunos e retornos) e o banco de dados no Firebase Firestore atualizando em tempo real

## Autor

Pedro Tersi — Turma 3 DS-AMS — Qualidade e Teste de Software
