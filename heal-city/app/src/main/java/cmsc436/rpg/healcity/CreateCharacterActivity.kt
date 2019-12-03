package cmsc436.rpg.healcity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_character.*
import java.lang.Integer.parseInt

/**
 * This class is the activity to initialize the player
 *
 * @author Muchlas Amirinanto
 */
class CreateCharacterActivity : AppCompatActivity() {

    // if user really wants to exit
    var exitDouble = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_character)

        val sharedPref = this.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

        // if user already created then do nothing
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

            // initialize user
            User.initPlayer(sharedPref,
                name.toString(), parseInt(target.toString()))

            // send back result
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    /**
     * This function override the default back button to make sure user did not exit on accident
     *
     * @author Muchlas Amirinanto
     */
    override fun onBackPressed() {
        if (exitDouble) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        Toast.makeText(this, "You need to create character before using this app.", Toast.LENGTH_SHORT).show()
        exitDouble = true
    }
}
