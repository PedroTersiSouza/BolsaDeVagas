package com.example.bolsadevagas.ui.alunos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.ActivityAlunoFormBinding
import com.example.bolsadevagas.model.AlunoEncaminhado
import com.example.bolsadevagas.model.Empresa
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AlunoFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlunoFormBinding
    private var alunoId: String = ""
    private var empresas: List<Empresa> = emptyList()
    private val statusOpcoes = listOf("Em andamento", "Aprovado", "Reprovado", "Desistente")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlunoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alunoId = intent.getStringExtra("alunoId") ?: ""
        binding.toolbarAlunoForm.title =
            if (alunoId.isBlank()) "Novo aluno encaminhado" else "Editar encaminhamento"
        binding.toolbarAlunoForm.setNavigationOnClickListener { finish() }

        binding.spinnerStatus.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOpcoes)

        binding.editData.setOnClickListener { abrirSeletorData() }

        carregarEmpresas()
        binding.btnSalvarAluno.setOnClickListener { salvar() }
    }

    private fun abrirSeletorData() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, ano, mes, dia ->
            binding.editData.setText(String.format("%02d/%02d/%04d", dia, mes + 1, ano))
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun carregarEmpresas() {
        FirestoreRepository.buscarEmpresasUmaVez { lista ->
            empresas = lista
            val nomes = lista.map { it.nome }
            binding.spinnerEmpresa.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomes)

            if (alunoId.isNotBlank()) carregarAluno()
        }
    }

    private fun carregarAluno() {
        FirebaseFirestore.getInstance().collection("alunos_encaminhados").document(alunoId).get()
            .addOnSuccessListener { doc ->
                val aluno = doc.toObject(AlunoEncaminhado::class.java) ?: return@addOnSuccessListener
                binding.editNomeAluno.setText(aluno.nomeAluno)
                binding.editCurso.setText(aluno.curso)
                binding.editMatricula.setText(aluno.matricula)
                binding.editData.setText(aluno.dataEncaminhamento)

                val posEmpresa = empresas.indexOfFirst { it.id == aluno.empresaId }
                if (posEmpresa >= 0) binding.spinnerEmpresa.setSelection(posEmpresa)

                val posStatus = statusOpcoes.indexOf(aluno.status)
                if (posStatus >= 0) binding.spinnerStatus.setSelection(posStatus)
            }
    }

    private fun salvar() {
        val nome = binding.editNomeAluno.text.toString().trim()
        if (nome.isBlank()) {
            binding.editNomeAluno.error = "Informe o nome do aluno"
            return
        }
        if (empresas.isEmpty()) {
            Toast.makeText(this, "Cadastre uma empresa antes de encaminhar um aluno", Toast.LENGTH_SHORT).show()
            return
        }

        val empresaSelecionada = empresas[binding.spinnerEmpresa.selectedItemPosition]

        val aluno = AlunoEncaminhado(
            id = alunoId,
            empresaId = empresaSelecionada.id,
            empresaNome = empresaSelecionada.nome,
            nomeAluno = nome,
            curso = binding.editCurso.text.toString().trim(),
            matricula = binding.editMatricula.text.toString().trim(),
            dataEncaminhamento = binding.editData.text.toString().trim(),
            status = statusOpcoes[binding.spinnerStatus.selectedItemPosition]
        )

        binding.btnSalvarAluno.isEnabled = false
        FirestoreRepository.salvarAluno(aluno) { sucesso ->
            binding.btnSalvarAluno.isEnabled = true
            if (sucesso) {
                Toast.makeText(this, "Encaminhamento salvo", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
