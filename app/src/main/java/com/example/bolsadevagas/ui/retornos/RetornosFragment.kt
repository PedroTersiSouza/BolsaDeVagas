package com.example.bolsadevagas.ui.retornos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.FragmentRetornosBinding
import com.google.firebase.firestore.ListenerRegistration

class RetornosFragment : Fragment() {

    private var _binding: FragmentRetornosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RetornoAdapter
    private var listener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRetornosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RetornoAdapter(
            onEditar = { retorno ->
                val intent = Intent(requireContext(), RetornoFormActivity::class.java)
                intent.putExtra("retornoId", retorno.id)
                startActivity(intent)
            },
            onExcluir = { retorno -> FirestoreRepository.excluirRetorno(retorno.id) {} }
        )

        binding.recyclerRetornos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRetornos.adapter = adapter

        binding.fabAddRetorno.setOnClickListener {
            startActivity(Intent(requireContext(), RetornoFormActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        listener = FirestoreRepository.listenRetornos { lista ->
            adapter.submitLista(lista)
            binding.textVazioRetornos.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
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
