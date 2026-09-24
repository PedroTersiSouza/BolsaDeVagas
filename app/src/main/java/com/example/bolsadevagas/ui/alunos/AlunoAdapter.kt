package com.example.bolsadevagas.ui.alunos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolsadevagas.databinding.ItemAlunoBinding
import com.example.bolsadevagas.model.AlunoEncaminhado

class AlunoAdapter(
    private val onEditar: (AlunoEncaminhado) -> Unit,
    private val onExcluir: (AlunoEncaminhado) -> Unit
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    private val lista = mutableListOf<AlunoEncaminhado>()

    fun submitLista(novaLista: List<AlunoEncaminhado>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val binding = ItemAlunoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlunoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class AlunoViewHolder(private val binding: ItemAlunoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(aluno: AlunoEncaminhado) {
            binding.textNomeAluno.text = aluno.nomeAluno
            binding.textCursoEmpresa.text = "${aluno.curso} • ${aluno.empresaNome}"
            binding.textStatusAluno.text = aluno.status

            binding.root.setOnClickListener { onEditar(aluno) }
            binding.btnExcluirAluno.setOnClickListener { onExcluir(aluno) }
        }
    }
}
