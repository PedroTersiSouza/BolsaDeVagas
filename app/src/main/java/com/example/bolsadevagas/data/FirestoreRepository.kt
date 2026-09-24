package com.example.bolsadevagas.data

import com.example.bolsadevagas.model.AlunoEncaminhado
import com.example.bolsadevagas.model.Empresa
import com.example.bolsadevagas.model.RequisitoTecnico
import com.example.bolsadevagas.model.Retorno
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

/**
 * Camada responsável por todas as operações de CRUD no Firebase Firestore.
 * Coleções:
 *  - empresas
 *  - requisitos          (cada documento referencia empresaId)
 *  - alunos_encaminhados (cada documento referencia empresaId)
 *  - retornos            (cada documento referencia alunoId e empresaId)
 */
object FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    private const val COL_EMPRESAS = "empresas"
    private const val COL_REQUISITOS = "requisitos"
    private const val COL_ALUNOS = "alunos_encaminhados"
    private const val COL_RETORNOS = "retornos"

    // ---------- EMPRESAS ----------

    fun listenEmpresas(onChange: (List<Empresa>) -> Unit): ListenerRegistration {
        return db.collection(COL_EMPRESAS)
            .orderBy("nome", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Empresa::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                onChange(lista)
            }
    }

    fun salvarEmpresa(empresa: Empresa, onResult: (Boolean) -> Unit) {
        if (empresa.id.isBlank()) {
            db.collection(COL_EMPRESAS).add(empresa)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            db.collection(COL_EMPRESAS).document(empresa.id).set(empresa)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        }
    }

    fun excluirEmpresa(empresaId: String, onResult: (Boolean) -> Unit) {
        db.collection(COL_EMPRESAS).document(empresaId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ---------- REQUISITOS TÉCNICOS ----------

    fun listenRequisitos(empresaId: String, onChange: (List<RequisitoTecnico>) -> Unit): ListenerRegistration {
        return db.collection(COL_REQUISITOS)
            .whereEqualTo("empresaId", empresaId)
            .addSnapshotListener { snapshot, _ ->
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(RequisitoTecnico::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                onChange(lista)
            }
    }

    fun salvarRequisito(requisito: RequisitoTecnico, onResult: (Boolean) -> Unit) {
        if (requisito.id.isBlank()) {
            db.collection(COL_REQUISITOS).add(requisito)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            db.collection(COL_REQUISITOS).document(requisito.id).set(requisito)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        }
    }

    fun excluirRequisito(requisitoId: String, onResult: (Boolean) -> Unit) {
        db.collection(COL_REQUISITOS).document(requisitoId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ---------- ALUNOS ENCAMINHADOS ----------

    fun listenAlunos(onChange: (List<AlunoEncaminhado>) -> Unit): ListenerRegistration {
        return db.collection(COL_ALUNOS)
            .orderBy("nomeAluno", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(AlunoEncaminhado::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                onChange(lista)
            }
    }

    fun salvarAluno(aluno: AlunoEncaminhado, onResult: (Boolean) -> Unit) {
        if (aluno.id.isBlank()) {
            db.collection(COL_ALUNOS).add(aluno)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            db.collection(COL_ALUNOS).document(aluno.id).set(aluno)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        }
    }

    fun excluirAluno(alunoId: String, onResult: (Boolean) -> Unit) {
        db.collection(COL_ALUNOS).document(alunoId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ---------- RETORNOS ----------

    fun listenRetornos(onChange: (List<Retorno>) -> Unit): ListenerRegistration {
        return db.collection(COL_RETORNOS)
            .addSnapshotListener { snapshot, _ ->
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Retorno::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                onChange(lista)
            }
    }

    fun salvarRetorno(retorno: Retorno, onResult: (Boolean) -> Unit) {
        if (retorno.id.isBlank()) {
            db.collection(COL_RETORNOS).add(retorno)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            db.collection(COL_RETORNOS).document(retorno.id).set(retorno)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        }
    }

    fun excluirRetorno(retornoId: String, onResult: (Boolean) -> Unit) {
        db.collection(COL_RETORNOS).document(retornoId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // ---------- Utilitários (preencher spinners) ----------

    fun buscarEmpresasUmaVez(onResult: (List<Empresa>) -> Unit) {
        db.collection(COL_EMPRESAS).get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Empresa::class.java)?.apply { id = doc.id }
                })
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun buscarAlunosUmaVez(onResult: (List<AlunoEncaminhado>) -> Unit) {
        db.collection(COL_ALUNOS).get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.documents.mapNotNull { doc ->
                    doc.toObject(AlunoEncaminhado::class.java)?.apply { id = doc.id }
                })
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
