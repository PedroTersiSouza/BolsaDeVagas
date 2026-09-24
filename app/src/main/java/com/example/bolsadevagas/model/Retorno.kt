package com.example.bolsadevagas.model

data class Retorno(
    var id: String = "",
    var alunoId: String = "",
    var alunoNome: String = "",
    var empresaId: String = "",
    var empresaNome: String = "",
    var resultado: String = "Aguardando",
    var observacao: String = "",
    var data: String = ""
)
