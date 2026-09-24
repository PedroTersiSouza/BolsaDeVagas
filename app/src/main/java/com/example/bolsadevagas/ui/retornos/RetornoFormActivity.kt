package com.example.bolsadevagas.ui.retornos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.ActivityRetornoFormBinding
import com.example.bolsadevagas.model.AlunoEncaminhado
import com.example.bolsadevagas.model.Retorno
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RetornoFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetornoFormBinding
    private var retornoId: String = ""
    private var alunos: List<AlunoEncaminhado> = emptyList()
    private val resultadoOpcoes = listOf("Aguardando", "Aprovado", "Reprovado", "Desistiu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetornoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retornoId = intent.getStringExtra("retornoId") ?: ""
        binding.toolbarRetornoForm.title = if (retornoId.isBlank()) "Novo retorno" else "Editar retorno"
        binding.toolbarRetornoForm.setNavigationOnClickListener { finish() }

        binding.spinnerResultado.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resultadoOpcoes)
        binding.editDataRetorno.setOnClickListener { abrirSeletorData() }

        carregarAlunos()
        binding.btnSalvarRetorno.setOnClickListener { salvar() }
    }

    private fun abrirSeletorData() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, ano, mes, dia ->
            binding.editDataRetorno.setText(String.format("%02d/%02d/%04d", dia, mes + 1, ano))
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun carregarAlunos() {
        FirestoreRepository.buscarAlunosUmaVez { lista ->
            alunos = lista
            val nomes = lista.map { "${it.nomeAluno} - ${it.empresaNome}" }
            binding.spinnerAluno.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomes)

            if (retornoId.isNotBlank()) carregarRetorno()
        }
    }

    private fun carregarRetorno() {
        FirebaseFirestore.getInstance().collection("retornos").document(retornoId).get()
            .addOnSuccessListener { doc ->
                val retorno = doc.toObject(Retorno::class.java) ?: return@addOnSuccessListener
                binding.editObservacao.setText(retorno.observacao)
                binding.editDataRetorno.setText(retorno.data)

                val posAluno = alunos.indexOfFirst { it.id == retorno.alunoId }
                if (posAluno >= 0) binding.spinnerAluno.setSelection(posAluno)

                val posResultado = resultadoOpcoes.indexOf(retorno.resultado)
                if (posResultado >= 0) binding.spinnerResultado.setSelection(posResultado)
            }
    }

    private fun salvar() {
        if (alunos.isEmpty()) {
            Toast.makeText(this, "Encaminhe um aluno antes de registrar um retorno", Toast.LENGTH_SHORT).show()
            return
        }

        val alunoSelecionado = alunos[binding.spinnerAluno.selectedItemPosition]

        val retorno = Retorno(
            id = retornoId,
            alunoId = alunoSelecionado.id,
            alunoNome = alunoSelecionado.nomeAluno,
            empresaId = alunoSelecionado.empresaId,
            empresaNome = alunoSelecionado.empresaNome,
            resultado = resultadoOpcoes[binding.spinnerResultado.selectedItemPosition],
            observacao = binding.editObservacao.text.toString().trim(),
            data = binding.editDataRetorno.text.toString().trim()
        )

        binding.btnSalvarRetorno.isEnabled = false
        FirestoreRepository.salvarRetorno(retorno) { sucesso ->
            binding.btnSalvarRetorno.isEnabled = true
            if (sucesso) {
                Toast.makeText(this, "Retorno salvo", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
