package cmsc436.rpg.healcity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_character.*
import java.lang.Integer.parseInt

class CreateCharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_character)

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

            User.initPlayer(this.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE),
                name.toString(), parseInt(target.toString()))
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please finish creating character!", Toast.LENGTH_SHORT).show()
    }
}
