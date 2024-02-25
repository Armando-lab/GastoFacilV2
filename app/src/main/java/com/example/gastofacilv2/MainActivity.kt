package com.example.gastofacilv2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnResumen = findViewById<Button>(R.id.btnResumen)



        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnResumen.setOnClickListener {
            val intent = Intent(this, ResumenActivity::class.java)
            startActivity(intent)
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        // Configurar el ActionBarDrawerToggle para abrir y cerrar el drawer
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar clics en los elementos del menú del cajón (NavigationView)
        if (item.itemId == R.id.menu_export) {
            // Manejar clic en el elemento "Exportar"
            val intent = Intent(this, ExportActivity::class.java)
            startActivity(intent)
            return true // Indica que el evento ha sido consumido
        } else if (item.itemId == R.id.menu_history) {
            // Manejar clic en el elemento "Historial"
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            return true // Indica que el evento ha sido consumido
        }

        // Manejar clics en el icono de hamburguesa para abrir y cerrar el drawer
        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }




    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }
}
