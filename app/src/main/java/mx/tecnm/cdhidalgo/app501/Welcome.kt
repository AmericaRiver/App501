package mx.tecnm.cdhidalgo.app501
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class Welcome : AppCompatActivity() {

    private lateinit var text1:TextView
    private lateinit var text2:TextView
    private  lateinit var btnConfirmar:Button

    val BaseDeDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val nombre = intent.getStringExtra("nombre")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val user = intent.getStringExtra("usuario")
        val password = intent.getStringExtra("password")

        btnConfirmar = findViewById(R.id.btn_confirmar)

        text1 = findViewById(R.id.txt01)
        text2 = findViewById(R.id.txt02)

        text1.text = "Hola $nombre!!\n"

        text2.text = "Tus datos son:\n" +
                "Correo Electronico:\n$email\n " +
                "Teléfono:\n$phone\n"+
                "Usuario: \n$user\n"+
                "Password:\n$password\n"

        val usuario = Usuario(nombre.toString(), email.toString(),phone.toString(),
            user.toString(),password.toString())

        btnConfirmar.setOnClickListener {
            if(email.toString().isNotEmpty()&&
                password.toString().isNotEmpty())
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    email.toString(),password.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this,Login::class.java).apply {
                            BaseDeDatos.collection("usuarios")
                                .document(email.toString()).set(usuario)
                        }
                        startActivity(intent)
                    }else{
                        showAlert()
                    }
                }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error  en la autenticación de usuario!!")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
}
  