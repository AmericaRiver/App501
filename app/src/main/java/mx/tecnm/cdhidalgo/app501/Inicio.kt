//Autor: América Estefanía Rivera Morales
package mx.tecnm.cdhidalgo.app501
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
enum class tipoProveedor {
    BASIC,
    GOOGLE
}

class Inicio : AppCompatActivity() {

    private lateinit var emailApp:TextView
    private lateinit var proveedorApp:TextView
    private lateinit var nombreApp:TextInputLayout
    private lateinit var telefonoApp:TextInputLayout
    private lateinit var usuarioApp:TextInputLayout
    private lateinit var passwordApp:TextInputLayout

    private lateinit var btnActualizar:Button
    private lateinit var btnEliminar: Button
    private lateinit var btnCerrarSesion: Button

    private val BaseDeDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        emailApp = findViewById(R.id.txtEmail)
        proveedorApp = findViewById(R.id.txtProveedor)
        nombreApp = findViewById(R.id.txtNombre)
        telefonoApp = findViewById(R.id.txtPhone)
        usuarioApp = findViewById(R.id.txtUsuario)
        passwordApp = findViewById(R.id.txtPassword)
        btnActualizar = findViewById(R.id.btn_actualizar)
        btnEliminar = findViewById(R.id.btn_eliminar)
        btnCerrarSesion = findViewById(R.id.btn_cerrar)

        val email = intent.getStringExtra("email")
        val proveedor = intent.getStringExtra("proveedor")

        setup(email?:"",proveedor?:"")

        //Guardar el estado de la sesión por si cerramos la App
        val pref = getSharedPreferences(getString(R.string.preferencia),
            Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.putString("proveedor", proveedor)
        pref.apply()
    }

    private fun setup(email:String,proveedor:String){
        emailApp.text = email
        proveedorApp.text = proveedor

        BaseDeDatos.collection("usuarios").document(email).get().addOnSuccessListener {
            nombreApp.editText?.setText(it.get("nombre") as String?)
            telefonoApp.editText?.setText(it.get("telefono") as String?)
            usuarioApp.editText?.setText(it.get("usuario") as String?)
            passwordApp.editText?.setText(it.get("password") as String?)
        }
        btnActualizar.setOnClickListener {
            val usuario = Usuario(nombreApp.editText?.text.toString(),emailApp.text.toString(),
                telefonoApp.editText?.text.toString(),usuarioApp.editText?.text.toString(),
                passwordApp.editText?.text.toString())

            BaseDeDatos.collection("usuarios").document(email).set(usuario)
        }
        btnEliminar.setOnClickListener {
            BaseDeDatos.collection("usuarios").document(email).delete()
        }
        btnCerrarSesion.setOnClickListener {
            //Borrar el estado de la sesión dado que se cierra la sesión de manera normal
            val pref = getSharedPreferences(getString(R.string.preferencia),
                Context.MODE_PRIVATE).edit()
            pref.clear()
            pref.apply()

            FirebaseAuth.getInstance().signOut()
            Firebase.auth.signOut()
            onBackPressed()
        }
    }
}