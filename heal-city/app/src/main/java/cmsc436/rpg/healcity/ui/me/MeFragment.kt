package cmsc436.rpg.healcity.ui.me

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R

class MeFragment : Fragment() {

    private lateinit var homeViewModel: MeViewModel
    private lateinit var listener: onEquipSelected

    private lateinit var headCard: ImageView
    private lateinit var bodyCard: ImageView
    private lateinit var handCard: ImageView
    private lateinit var hatCard: ImageView
    private lateinit var pantsCard: ImageView
    private lateinit var feetCard: ImageView

    private var headColor: Int = R.color.black
    private var bodyColor: Int = R.color.black
    private var handColor: Int = R.color.black
    private var hatColor: Int = R.color.black
    private var pantsColor: Int = R.color.black
    private var feetColor: Int = R.color.black


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(MeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    // Set up some information about the mQuoteView TextView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        headCard = activity!!.findViewById(R.id.image_head)
        bodyCard = activity!!.findViewById(R.id.image_body)
        handCard = activity!!.findViewById(R.id.image_hand)
        hatCard = activity!!.findViewById(R.id.image_hat)
        pantsCard = activity!!.findViewById(R.id.image_pants)
        feetCard = activity!!.findViewById(R.id.image_feet)

        var sharedPref = this.activity?.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

        if (isEquipSaved(sharedPref)) { // load prefs

        } else { // first run
            sharedPref?.edit {
                val black = R.color.black

                putInt(HEAD_KEY, black)
                putInt(BODY_KEY, black)
                putInt(HAND_KEY, black)
                putInt(HAT_KEY, black)
                putInt(PANTS_KEY, black)
                putInt(FEET_KEY, black)

            }

            reloadEquipColor()
        }


        headCard.setOnClickListener {
            chooseEquip(headCard)
        }

        bodyCard.setOnClickListener {
            chooseEquip(bodyCard)
        }

        handCard.setOnClickListener {
            chooseEquip(handCard)
        }

        hatCard.setOnClickListener {
            chooseEquip(hatCard)
        }

        pantsCard.setOnClickListener {
            chooseEquip(pantsCard)
        }

        feetCard.setOnClickListener {
            chooseEquip(feetCard)
        }
    }

    private fun reloadEquipColor() {
    }

    private fun isEquipSaved(sharedPref: SharedPreferences?): Boolean {
        return sharedPref!!.contains(HEAD_KEY) &&
                sharedPref!!.contains(BODY_KEY) &&
                sharedPref!!.contains(HAND_KEY) &&
                sharedPref!!.contains(HAT_KEY) &&
                sharedPref!!.contains(PANTS_KEY) &&
                sharedPref!!.contains(FEET_KEY)
    }

    private fun chooseEquip(equipCard: ImageView?) {
        Log.i(TAG, "equip choose entered")
        Toast.makeText(context, equipCard?.id.toString(), Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "ME_FRAGMENT"

        private const val HEAD_KEY = "HEAD_ITEM"
        private const val BODY_KEY = "BODY_ITEM"
        private const val HAND_KEY = "HAND_ITEM"
        private const val HAT_KEY = "HAT_ITEM"
        private const val PANTS_KEY = "PANTS_ITEM"
        private const val FEET_KEY = "FEET_ITEM"
    }

}

interface onEquipSelected {
    fun onEquipSelected(meViewModel: MeViewModel)
}