package com.ygorcesar.barbeirodorminhoco

import android.util.Log

class Barbeiro(val cadeiras: Int) {
    var isDormindo: Boolean = true
    var isCortando: Boolean = false
    var clienteCortando: Cliente? = null
    var clientesAguardando: MutableList<Cliente> = mutableListOf()
    var clientesAtendidos: MutableList<Cliente> = mutableListOf()

    fun cortarCabelo(cliente: Cliente) {
        if (isDormindo && !isCortando) {
            isDormindo = false
            isCortando = true
            clienteCortando = cliente
            Log.i("BARBEIRO", "Cliente ${cliente.nome} iniciou o corte")
        } else if (isCortando && clientesAguardando.size < cadeiras) {
            val clientes = clientesAguardando.filter { it.nome.equals(cliente.nome, ignoreCase = true) }
            if (clientes.isEmpty()) {
                Log.i("BARBEIRO", "Cliente ${cliente.nome} está aguradando")
                clientesAguardando.add(cliente)
            }
        } else {
            Log.i("BARBEIRO", "Cliente ${cliente.nome} foi embora por não ter vagas, Cadeiras:$cadeiras, Clientes Aguardando:${clientesAguardando.size}")
            print("Não tem vaga para cortar cabelo")
        }
    }

    fun finalizarCorte() {
        if (clienteCortando != null) {
            Log.i("BARBEIRO", "Cliente ${clienteCortando?.nome} finalizou o corte")
            clienteCortando?.let { clientesAtendidos.add(it) }
            clienteCortando = null

            val temClientes = clientesAguardando.isNotEmpty()
            isCortando = temClientes
            isDormindo = !temClientes
        }
    }

    fun checarFila() {
        if (clienteCortando == null) {
            val clienteAguardando = clientesAguardando.firstOrNull()
            if (clienteAguardando != null) {
                isDormindo = false
                isCortando = true
                clienteCortando = clienteAguardando
                clientesAguardando.removeAt(0)
                Log.i("BARBEIRO", "Cliente ${clienteCortando?.nome} iniciou o corte")
            } else {
                Log.i("BARBEIRO", "Não tem mais nenhum cliente para cortar cabelo barbeiro voltou a dormir")
                clienteCortando = null
                isCortando = false
                isDormindo = true
            }
        }
    }

    companion object {
        private var barbeiro: Barbeiro? = null
        fun getInstance(cadeiras: Int): Barbeiro {
            if (barbeiro == null) {
                barbeiro = Barbeiro(cadeiras)
            }
            return barbeiro!!
        }
    }
}