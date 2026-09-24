package com.example.bolsadevagas.ui.empresas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bolsadevagas.R
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.ActivityEmpresaDetailBinding
import com.example.bolsadevagas.model.RequisitoTecnico
import com.google.firebase.firestore.ListenerRegistration

class EmpresaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmpresaDetailBinding
    private lateinit var adapter: RequisitoAdapter
    private var listener: ListenerRegistration? = null
    private var empresaId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        empresaId = intent.getStringExtra("empresaId") ?: ""
        val empresaNome = intent.getStringExtra("empresaNome") ?: ""

        binding.toolbarDetail.title = empresaNome
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        adapter = RequisitoAdapter(
            onEditar = { requisito -> abrirDialogRequisito(requisito) },
            onExcluir = { requisito -> FirestoreRepository.excluirRequisito(requisito.id) {} }
        )

        binding.recyclerRequisitos.layoutManager = LinearLayoutManager(this)
        binding.recyclerRequisitos.adapter = adapter

        binding.fabAddRequisito.setOnClickListener { abrirDialogRequisito(null) }

        binding.btnEditarEmpresa.setOnClickListener {
            val intent = Intent(this, EmpresaFormActivity::class.java)
            intent.putExtra("empresaId", empresaId)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        listener = FirestoreRepository.listenRequisitos(empresaId) { lista ->
            adapter.submitLista(lista)
        }
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
    }

    private fun abrirDialogRequisito(requisito: RequisitoTecnico?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_requisito, null)
        val editDescricao = view.findViewById<EditText>(R.id.editDescricaoRequisito)
        val spinnerNivel = view.findViewById<Spinner>(R.id.spinnerNivel)

        val niveis = listOf("Básico", "Intermediário", "Avançado")
        spinnerNivel.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, niveis)

        requisito?.let {
            editDescricao.setText(it.descricao)
            spinnerNivel.setSelection(niveis.indexOf(it.nivel).coerceAtLeast(0))
        }

        AlertDialog.Builder(this)
            .setTitle(if (requisito == null) "Novo requisito" else "Editar requisito")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                val descricao = editDescricao.text.toString().trim()
                if (descricao.isBlank()) return@setPositiveButton
                val novo = RequisitoTecnico(
                    id = requisito?.id ?: "",
                    empresaId = empresaId,
                    descricao = descricao,
                    nivel = niveis[spinnerNivel.selectedItemPosition]
                )
                FirestoreRepository.salvarRequisito(novo) {}
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
