package edu.umg.controlagua

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.umg.controlagua.databinding.ActivityMainBinding
import edu.umg.programacion2.agua.datos.RegistroAguaRepositoryEnMemoria
import edu.umg.programacion2.agua.modelo.MomentoDia
import edu.umg.programacion2.agua.modelo.RegistroAgua
import edu.umg.programacion2.agua.servicio.ControlAgua
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val control = ControlAgua(RegistroAguaRepositoryEnMemoria())
    private val metaDiaria = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el XML con View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Llenar el Spinner con los momentos del día
        binding.spMomento.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, MomentoDia.entries
        )

        // Evento del botón
        binding.btnRegistrar.setOnClickListener { registrar() }

        // Mostrar progreso inicial
        lifecycleScope.launch { actualizarProgreso() }
    }

    private fun registrar() {
        // Leer la cantidad escrita por el usuario
        val cantidad = binding.etCantidad.text.toString().toIntOrNull()
        if (cantidad == null) {
            binding.etCantidad.error = "Ingresa una cantidad válida"
            return
        }

        // Leer el momento seleccionado
        val momento = binding.spMomento.selectedItem as MomentoDia

        // Crear el objeto RegistroAgua
        val registro = RegistroAgua(
            cantidadMl = cantidad,
            momento = momento,
            fecha = fechaDeHoy(),
            hora = horaActual()
        )

        // Enviar a la librería
        lifecycleScope.launch {
            val error = control.registrar(registro)
            if (error != null) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Limpiar el formulario
            binding.etCantidad.text.clear()

            // Actualizar la pantalla
            actualizarProgreso()
        }
    }

    private suspend fun actualizarProgreso() {
        val progreso = control.progresoDelDia(fechaDeHoy(), metaDiaria)
        binding.tvProgreso.text = "Hoy: $progreso"

        // Actualizar la lista de registros
        val registros = control.listarPorFecha(fechaDeHoy())
        val textos = registros.map {
            "#${it.id} - ${it.hora} - ${it.cantidadMl}ml - ${it.momento}"
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
        binding.lvRegistros.adapter = adapter
    }

    private fun fechaDeHoy(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private fun horaActual(): String =
        SimpleDateFormat("HH:mm", Locale.US).format(Date())
}