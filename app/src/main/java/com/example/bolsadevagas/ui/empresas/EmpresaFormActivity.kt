package com.example.bolsadevagas.ui.empresas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.ActivityEmpresaFormBinding
import com.example.bolsadevagas.model.Empresa
import com.google.firebase.firestore.FirebaseFirestore

class EmpresaFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmpresaFormBinding
    private var empresaId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresaFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        empresaId = intent.getStringExtra("empresaId") ?: ""

        binding.toolbarForm.title = if (empresaId.isBlank()) "Nova empresa" else "Editar empresa"
        binding.toolbarForm.setNavigationOnClickListener { finish() }

        if (empresaId.isNotBlank()) {
            carregarEmpresa()
        }

        binding.btnSalvar.setOnClickListener { salvar() }
    }

    private fun carregarEmpresa() {
        FirebaseFirestore.getInstance().collection("empresas").document(empresaId).get()
            .addOnSuccessListener { doc ->
                val empresa = doc.toObject(Empresa::class.java) ?: return@addOnSuccessListener
                binding.editNome.setText(empresa.nome)
                binding.editCnpj.setText(empresa.cnpj)
                binding.editArea.setText(empresa.area)
                binding.editContato.setText(empresa.contato)
                binding.editTelefone.setText(empresa.telefone)
                binding.editEmail.setText(empresa.email)
                binding.editEndereco.setText(empresa.endereco)
            }
    }

    private fun salvar() {
        val nome = binding.editNome.text.toString().trim()
        if (nome.isBlank()) {
            binding.editNome.error = "Informe o nome da empresa"
            return
        }

        val empresa = Empresa(
            id = empresaId,
            nome = nome,
            cnpj = binding.editCnpj.text.toString().trim(),
            area = binding.editArea.text.toString().trim(),
            contato = binding.editContato.text.toString().trim(),
            telefone = binding.editTelefone.text.toString().trim(),
            email = binding.editEmail.text.toString().trim(),
            endereco = binding.editEndereco.text.toString().trim()
        )

        binding.btnSalvar.isEnabled = false
        FirestoreRepository.salvarEmpresa(empresa) { sucesso ->
            binding.btnSalvar.isEnabled = true
            if (sucesso) {
                Toast.makeText(this, "Empresa salva com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao salvar empresa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
