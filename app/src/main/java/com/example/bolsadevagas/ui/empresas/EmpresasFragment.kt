package com.example.bolsadevagas.ui.empresas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.FragmentEmpresasBinding
import com.google.firebase.firestore.ListenerRegistration

class EmpresasFragment : Fragment() {

    private var _binding: FragmentEmpresasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EmpresaAdapter
    private var listener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmpresasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EmpresaAdapter(
            onClick = { empresa ->
                val intent = Intent(requireContext(), EmpresaDetailActivity::class.java)
                intent.putExtra("empresaId", empresa.id)
                intent.putExtra("empresaNome", empresa.nome)
                startActivity(intent)
            },
            onEditar = { empresa ->
                val intent = Intent(requireContext(), EmpresaFormActivity::class.java)
                intent.putExtra("empresaId", empresa.id)
                startActivity(intent)
            },
            onExcluir = { empresa ->
                FirestoreRepository.excluirEmpresa(empresa.id) {}
            }
        )

        binding.recyclerEmpresas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEmpresas.adapter = adapter

        binding.fabAddEmpresa.setOnClickListener {
            startActivity(Intent(requireContext(), EmpresaFormActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        listener = FirestoreRepository.listenEmpresas { lista ->
            adapter.submitLista(lista)
            binding.textVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
