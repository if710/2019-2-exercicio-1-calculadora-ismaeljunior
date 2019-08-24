package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.app.Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var visor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_Add.setOnClickListener {
            visor = visor + "+"
            text_calc.setText(visor)
        }

        btn_Subtract.setOnClickListener {
            visor = visor + "-"
            text_calc.setText(visor)
        }

        btn_Multiply.setOnClickListener {
            visor = visor + "*"
            text_calc.setText(visor)
        }

        btn_Divide.setOnClickListener {
            visor = visor + "/"
            text_calc.setText(visor)
        }

        btn_Power.setOnClickListener {
            visor = visor + "^"
            text_calc.setText(visor)
        }

        btn_Clear.setOnClickListener {
            visor = ""
            text_calc.setText(visor)
        }


        btn_Equal.setOnClickListener {
            try {
                visor = eval(visor).toString()
                text_info.setText(visor)
            }

            catch(e: RuntimeException) {
                Toast.makeText(this, "Digite uma expressão válida", Toast.LENGTH_LONG ).show()
            }

        }

        btn_0.setOnClickListener {
            visor = visor + "0"
            text_calc.setText(visor)
        }

        btn_1.setOnClickListener {
            visor = visor + "1"
            text_calc.setText(visor)
        }

        btn_2.setOnClickListener {
            visor = visor + "2"
            text_calc.setText(visor)
        }

        btn_3.setOnClickListener {
            visor = visor + "3"
            text_calc.setText(visor)
        }

        btn_4.setOnClickListener {
            visor = visor + "4"
            text_calc.setText(visor)
        }

        btn_5.setOnClickListener {
            visor = visor + "5"
            text_calc.setText(visor)
        }

        btn_6.setOnClickListener {
            visor = visor + "6"
            text_calc.setText(visor)
        }

        btn_7.setOnClickListener {
            visor = visor + "7"
            text_calc.setText(visor)
        }

        btn_8.setOnClickListener {
            visor = visor + "8"
            text_calc.setText(visor)
        }

        btn_9.setOnClickListener {
            visor = visor + "9"
            text_calc.setText(visor)
        }

        btn_Dot.setOnClickListener {
            visor = visor + "."
            text_calc.setText(visor)
        }

        btn_LParen.setOnClickListener {
            visor = visor + "("
            text_calc.setText(visor)
        }

        btn_RParen.setOnClickListener {
            visor = visor + ")"
            text_calc.setText(visor)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("oQueFoiDigitado",text_info.text.toString())
        outState.putString("oQueFoisalvo",text_calc.text.toString())
        super.onSaveInstanceState(outState)
    }
    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }

}
