package com.ygorcesar.barbeirodorminhoco

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val barbeiro: Barbeiro = Barbeiro.getInstance(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun iniciarBarbeiro() {
        val newCliente = Cliente("CLI_${Calendar.getInstance().get(Calendar.SECOND)}")
        if (barbeiro.isDormindo) {
            barbeiro.cortarCabelo(newCliente)
            thread(start = true) {
                Thread.sleep(5000)
                barbeiro.finalizarCorte()
            }
        } else {
            barbeiro.cortarCabelo(newCliente)
            thread(start = true) {
                Thread.sleep(5000)
                while (!barbeiro.isDormindo) {
                    barbeiro.finalizarCorte()
                    barbeiro.checarFila()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("BARBEARIA", "INICIOU A BARBEARIA")
        bt_novo_cliente?.setOnClickListener { iniciarBarbeiro() }
        bt_finalizar_corte?.setOnClickListener { barbeiro.finalizarCorte() }
    }
}
