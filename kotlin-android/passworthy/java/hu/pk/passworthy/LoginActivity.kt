package hu.pk.passworthy

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.preference.PreferenceManager
import hu.pk.passworthy.databinding.ActivityLoginBinding
import hu.pk.passworthy.encryption.Encryption
import hu.pk.passworthy.notification.Notification

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var firststart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sP = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sP.edit()
        firststart = sP.getBoolean("firststart", true)

        if(!firststart) {
            binding.tvLogin.setText(getString(R.string.enter_mpw_to_log_in))
            binding.btnLogin.setText(getString(R.string.login_text))
        }

        binding.masterPasswordField.editText?.doOnTextChanged { text, start, before, count ->
            if(binding.masterPasswordField.error != null){
                binding.masterPasswordField.error = null
            }
        }

        binding.btnLogin.setOnClickListener {
            if(!firststart){
                if(binding.masterPasswordField.editText?.text.toString().isNotEmpty()){
                    if(Encryption.toSHA256(binding.masterPasswordField.editText?.text.toString()).equals(sP.getString("masterpw", null).toString())){
                        Encryption.initEncDecKey(binding.masterPasswordField.editText?.text.toString())
                        goToMainActivity()
                    } else {
                        binding.masterPasswordField.error = getString(R.string.incorrect_password)
                    }
                } else {
                    binding.masterPasswordField.error = getString(R.string.enter_your_password)
                }
            } else {
                if(binding.masterPasswordField.editText?.text.toString().isNotEmpty()){
                    if(binding.masterPasswordField.editText?.text.toString().length > 5) {
                        editor.putString(
                            "masterpw",
                            Encryption.toSHA256(binding.masterPasswordField.editText?.text.toString())
                        )
                        editor.putBoolean("firststart", false)
                        editor.apply()
                    Encryption.initEncDecKey(binding.masterPasswordField.editText?.text.toString())
                        goToMainActivity()
                    }
                    else {
                        binding.masterPasswordField.error = getString(R.string.password_min_length)
                    }
                } else {
                    binding.masterPasswordField.error = getString(R.string.enter_your_password)
                }
            }
        }
    }

    fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}