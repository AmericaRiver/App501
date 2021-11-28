package mx.tecnm.cdhidalgo.app501
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
class Sign_up : AppCompatActivity() {

    private lateinit var btnIngresa:Button
    private lateinit var btnResgistrarse:Button

    private lateinit var nombre:TextInputLayout
    private lateinit var email:TextInputLayout
    private lateinit var phone:TextInputLayout
    private lateinit var user:TextInputLayout
    private lateinit var password:TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnIngresa = findViewById(R.id.btn_ingresar)
        btnResgistrarse = findViewById(R.id.btn_registrarse)

        nombre = findViewById(R.id.txtNombre)
        email = findViewById(R.id.txtEmail)
        phone = findViewById(R.id.txtPhone)
        user = findViewById(R.id.txtUsuario)
        password = findViewById(R.id.txtPassword)

        btnResgistrarse.setOnClickListener {
            val intent = Intent(this,Welcome::class.java)
            intent.putExtra("nombre",nombre.editText?.text.toString())
            intent.putExtra("email",email.editText?.text.toString())
            intent.putExtra("phone",phone.editText?.text.toString())
            intent.putExtra("usuario",user.editText?.text.toString())
            intent.putExtra("password",password.editText?.text.toString())
            startActivity(intent)
        }

        btnIngresa.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}