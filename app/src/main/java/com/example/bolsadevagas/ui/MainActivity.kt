package com.example.bolsadevagas.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bolsadevagas.R
import com.example.bolsadevagas.databinding.ActivityMainBinding
import com.example.bolsadevagas.ui.alunos.AlunosFragment
import com.example.bolsadevagas.ui.empresas.EmpresasFragment
import com.example.bolsadevagas.ui.retornos.RetornosFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            abrirFragment(EmpresasFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_empresas -> abrirFragment(EmpresasFragment())
                R.id.nav_alunos -> abrirFragment(AlunosFragment())
                R.id.nav_retornos -> abrirFragment(RetornosFragment())
            }
            true
        }
    }

    private fun abrirFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
