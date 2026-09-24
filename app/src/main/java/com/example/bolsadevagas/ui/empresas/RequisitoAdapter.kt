package com.example.bolsadevagas.ui.empresas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolsadevagas.databinding.ItemRequisitoBinding
import com.example.bolsadevagas.model.RequisitoTecnico

class RequisitoAdapter(
    private val onEditar: (RequisitoTecnico) -> Unit,
    private val onExcluir: (RequisitoTecnico) -> Unit
) : RecyclerView.Adapter<RequisitoAdapter.RequisitoViewHolder>() {

    private val lista = mutableListOf<RequisitoTecnico>()

    fun submitLista(novaLista: List<RequisitoTecnico>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequisitoViewHolder {
        val binding = ItemRequisitoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequisitoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequisitoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class RequisitoViewHolder(private val binding: ItemRequisitoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requisito: RequisitoTecnico) {
            binding.textDescricaoRequisito.text = requisito.descricao
            binding.textNivelRequisito.text = requisito.nivel
            binding.root.setOnClickListener { onEditar(requisito) }
            binding.btnExcluirRequisito.setOnClickListener { onExcluir(requisito) }
        }
    }
}
