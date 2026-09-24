package com.example.bolsadevagas.ui.retornos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolsadevagas.databinding.ItemRetornoBinding
import com.example.bolsadevagas.model.Retorno

class RetornoAdapter(
    private val onEditar: (Retorno) -> Unit,
    private val onExcluir: (Retorno) -> Unit
) : RecyclerView.Adapter<RetornoAdapter.RetornoViewHolder>() {

    private val lista = mutableListOf<Retorno>()

    fun submitLista(novaLista: List<Retorno>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetornoViewHolder {
        val binding = ItemRetornoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RetornoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RetornoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class RetornoViewHolder(private val binding: ItemRetornoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(retorno: Retorno) {
            binding.textAlunoEmpresaRetorno.text = "${retorno.alunoNome} • ${retorno.empresaNome}"
            binding.textResultadoRetorno.text = retorno.resultado
            binding.textObservacaoRetorno.text = retorno.observacao
            binding.textDataRetorno.text = retorno.data

            binding.root.setOnClickListener { onEditar(retorno) }
            binding.btnExcluirRetorno.setOnClickListener { onExcluir(retorno) }
        }
    }
}
