package com.example.bolsadevagas.ui.alunos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bolsadevagas.data.FirestoreRepository
import com.example.bolsadevagas.databinding.FragmentAlunosBinding
import com.google.firebase.firestore.ListenerRegistration

class AlunosFragment : Fragment() {

    private var _binding: FragmentAlunosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AlunoAdapter
    private var listener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlunosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlunoAdapter(
            onEditar = { aluno ->
                val intent = Intent(requireContext(), AlunoFormActivity::class.java)
                intent.putExtra("alunoId", aluno.id)
                startActivity(intent)
            },
            onExcluir = { aluno -> FirestoreRepository.excluirAluno(aluno.id) {} }
        )

        binding.recyclerAlunos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAlunos.adapter = adapter

        binding.fabAddAluno.setOnClickListener {
            startActivity(Intent(requireContext(), AlunoFormActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        listener = FirestoreRepository.listenAlunos { lista ->
            adapter.submitLista(lista)
            binding.textVazioAlunos.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
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
