package com.example.bolsadevagas.model

data class AlunoEncaminhado(
    var id: String = "",
    var empresaId: String = "",
    var empresaNome: String = "",
    var nomeAluno: String = "",
    var curso: String = "",
    var matricula: String = "",
    var dataEncaminhamento: String = "",
    var status: String = "Em andamento"
)
