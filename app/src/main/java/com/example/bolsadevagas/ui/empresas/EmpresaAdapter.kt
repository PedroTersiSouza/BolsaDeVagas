package com.example.bolsadevagas.ui.empresas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolsadevagas.databinding.ItemEmpresaBinding
import com.example.bolsadevagas.model.Empresa

class EmpresaAdapter(
    private val onClick: (Empresa) -> Unit,
    private val onEditar: (Empresa) -> Unit,
    private val onExcluir: (Empresa) -> Unit
) : RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder>() {

    private val lista = mutableListOf<Empresa>()

    fun submitLista(novaLista: List<Empresa>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val binding = ItemEmpresaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmpresaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class EmpresaViewHolder(private val binding: ItemEmpresaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(empresa: Empresa) {
            binding.textNome.text = empresa.nome
            binding.textArea.text = empresa.area
            binding.textContato.text = empresa.email.ifBlank { empresa.telefone }

            binding.root.setOnClickListener { onClick(empresa) }
            binding.btnEditar.setOnClickListener { onEditar(empresa) }
            binding.btnExcluir.setOnClickListener { onExcluir(empresa) }
        }
    }
}
