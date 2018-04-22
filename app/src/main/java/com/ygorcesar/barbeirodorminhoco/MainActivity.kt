package com.ygorcesar.barbeirodorminhoco

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val barbeiro: Barbeiro = Barbeiro.getInstance(5)
    private var threadRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun iniciarBarbeiro() {
        imageView?.setImageResource(R.drawable.barbeiro_cortando)
        val newCliente = Cliente("CLI_${Calendar.getInstance().get(Calendar.SECOND)}")
        if (barbeiro.isDormindo) {
//            toast("Iniciou o corte do cliente ${newCliente.nome}")
            tv_cli_barbeiro.text = "Cortando Cabelo Cliente: ${newCliente.nome}"
            thread(start = true) { barbeiro.cortarCabelo(newCliente) }
        } else {
            if (barbeiro.clientesAguardando.size <= 4) toast("Cliente ${newCliente.nome} está aguardando na cadeira: ${barbeiro.clientesAguardando.size + 1}!")
            thread(start = true) {
                Log.i("BARBEARIA", "Cortar Size: ${barbeiro.clientesAguardando.size}")
                when (barbeiro.clientesAguardando.size) {
                    0 -> runOnUiThread {
                        iv_cadeira_1.visible()
                        tv_cli_1.text = "${newCliente.nome}"
                    }
                    1 -> runOnUiThread {
                        iv_cadeira_2.visible()
                        tv_cli_2.text = "${newCliente.nome}"
                    }
                    2 -> runOnUiThread {
                        iv_cadeira_3.visible()
                        tv_cli_3.text = "${newCliente.nome}"
                    }
                    3 -> runOnUiThread {
                        iv_cadeira_4.visible()
                        tv_cli_4.text = "${newCliente.nome}"
                    }
                    4 -> runOnUiThread {
                        iv_cadeira_5.visible()
                        tv_cli_5.text = "${newCliente.nome}"
                    }
                    else -> runOnUiThread { toast("Barbearia cheia volte outra hora!") }
                }
                barbeiro.cortarCabelo(newCliente)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("BARBEARIA", "INICIOU A BARBEARIA")
        bt_novo_cliente?.setOnClickListener { iniciarBarbeiro() }
        bt_finalizar_corte?.setOnClickListener {
            thread(start = true) {
                val clienteFinalizado = barbeiro.clienteCortando
                runOnUiThread { toast("Finalizou o corte do cliente ${clienteFinalizado?.nome}") }
                barbeiro.finalizarCorte()
                barbeiro.checarFila()
                if (barbeiro.clienteCortando != null) {
                    runOnUiThread {
                        imageView?.setImageResource(R.drawable.barbeiro_cortando)
                        tv_cli_barbeiro.text = "Cortando Cabelo Cliente: ${barbeiro.clienteCortando?.nome}"
                        cleanTexts()
                        barbeiro.clientesAguardando.forEachIndexed { index, cliente ->
                            when (index) {
                                0 -> tv_cli_1.text = cliente.nome
                                1 -> tv_cli_2.text = cliente.nome
                                2 -> tv_cli_3.text = cliente.nome
                                3 -> tv_cli_4.text = cliente.nome
                                4 -> tv_cli_5.text = cliente.nome
                            }
                        }
//                        toast("Iniciou o corte do cliente ${barbeiro.clienteCortando?.nome}")
                    }
                }
                when (barbeiro.clientesAguardando.size) {
                    0 -> runOnUiThread {
                        hideAllChairs()
                        cleanTexts()
                    }
                    1 -> runOnUiThread {
                        iv_cadeira_2.invisible()
                        tv_cli_2.text = ""
                    }
                    2 -> runOnUiThread {
                        iv_cadeira_3.invisible()
                        tv_cli_3.text = ""
                    }
                    3 -> runOnUiThread {
                        iv_cadeira_4.invisible()
                        tv_cli_4.text = ""
                    }
                    4 -> runOnUiThread {
                        iv_cadeira_5.invisible()
                        tv_cli_5.text = ""
                    }
                }
            }

            Log.i("BARBEARIA", "THREAD IS RUNNING: $threadRunning")
            if (!threadRunning) {
                threadRunning = true
                thread(start = true) {
                    do {
                        var isDormindo = false
                        if (barbeiro.isDormindo) {
                            isDormindo = true
                            runOnUiThread {
                                imageView?.setImageResource(R.drawable.barbeiro_dormindo)
                                tv_cli_barbeiro.text = "DORMINDO"
                            }
                            threadRunning = false
                        }
                    } while (!isDormindo)
                }
            }
        }
    }

    fun hideAllChairs() {
        iv_cadeira_1.invisible()
        iv_cadeira_2.invisible()
        iv_cadeira_3.invisible()
        iv_cadeira_4.invisible()
        iv_cadeira_5.invisible()
    }

    fun cleanTexts() {
        tv_cli_1.text = ""
        tv_cli_2.text = ""
        tv_cli_3.text = ""
        tv_cli_4.text = ""
        tv_cli_5.text = ""
    }

    fun View?.invisible() {
        this?.visibility = View.INVISIBLE
    }

    fun View?.visible() {
        this?.visibility = View.VISIBLE
    }

    fun Activity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, duration).show()
}
