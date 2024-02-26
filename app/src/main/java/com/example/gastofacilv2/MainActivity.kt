package com.example.gastofacilv2

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var gastoDBHelper: GastoDBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnResumen = findViewById<Button>(R.id.btnResumen)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_history -> {
                    // Manejar clic en el elemento "Historial"
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    true // Indica que el evento ha sido consumido
                }
                else -> false
            }
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnResumen.setOnClickListener {
            val intent = Intent(this, ResumenActivity::class.java)
            startActivity(intent)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        gastoDBHelper = GastoDBHelper(this)

        // Configurar el ActionBarDrawerToggle para abrir y cerrar el drawer
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar clics en los elementos del menú del cajón (NavigationView)
        if (item.itemId == R.id.menu_history) {


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
