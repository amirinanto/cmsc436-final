package cmsc436.rpg.healcity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_character.*
import java.lang.Integer.parseInt

class CreateCharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_character)

        val sharedPref = this.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

        if (sharedPref.contains(User.PLAYER_NAME_KEY)) {
            finish()
            return
        }

        create_button.setOnClickListener {
            val name = name_edit.text
            val target = target_edit.text

            if (name.isEmpty()) {
                Toast.makeText(this, "Please fill in name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (target.isEmpty()) {
                Toast.makeText(this, "Please fill in name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            User.initPlayer(sharedPref,
                name.toString(), parseInt(target.toString()))

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please finish creating character!", Toast.LENGTH_SHORT).show()
    }
}
