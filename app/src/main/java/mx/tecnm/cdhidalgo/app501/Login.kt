//Autor: América EstefaníaRivera Morales
package mx.tecnm.cdhidalgo.app501
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    private lateinit var btnRegistrarse:Button
    private lateinit var btnIngresar:Button
    private lateinit var btnOlvidar:Button
    private lateinit var btnGoogle:Button
    private lateinit var emailApp:TextInputLayout
    private lateinit var passwordApp:TextInputLayout
    private lateinit var loginLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnRegistrarse = findViewById(R.id.btn_registrarse)
        btnIngresar = findViewById(R.id.btn_ingresar)
        btnOlvidar = findViewById(R.id.btn_olvidar)
        btnGoogle = findViewById(R.id.btn_google)
        emailApp = findViewById(R.id.txtEmail)
        passwordApp = findViewById(R.id.txtPassword)
        loginLayout = findViewById(R.id.loginLayout)

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }

        btnIngresar.setOnClickListener {
            if (emailApp.editText?.text.toString().isNotEmpty() &&
                passwordApp.editText?.text.toString().isNotEmpty()
            )
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailApp.editText?.text.toString(),
                    passwordApp.editText?.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showInicio(it.result?.user?.email ?: "", tipoProveedor.BASIC)
                    } else {
                        showAlert()
                    }
                }

        }
        btnOlvidar.setOnClickListener {
            val intent = Intent(this, ForgetPass::class.java)
            startActivity(intent)
        }

        btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

            sesion()
        }
    }

    override fun onStart() {
        super.onStart()
        loginLayout.visibility = View.VISIBLE
    }

    private fun sesion(){
        val pref = getSharedPreferences(getString(R.string.preferencia),
            Context.MODE_PRIVATE)
        val email = pref.getString("email", null)
        val proveedor = pref.getString("proveedor", null)
        if (email != null && proveedor != null){
            loginLayout.visibility = View.INVISIBLE
            showInicio(email, tipoProveedor.valueOf(proveedor))
        }


    }

    private fun showInicio(email:String,proveedor: tipoProveedor){
        val intent = Intent(this,Inicio::class.java).apply {
            putExtra("email",email)
            putExtra("proveedor",proveedor.name)
        }
        startActivity(intent)
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error en la autentificacion del usuario!!")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showInicio(account.email?:"",tipoProveedor.GOOGLE)
                        }else{
                            showAlert()
                        }

                    }

                }
            }catch (e:ApiException){
                showAlert()
            }
        }
    }
}