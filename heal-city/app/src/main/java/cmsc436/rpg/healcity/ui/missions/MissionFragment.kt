package cmsc436.rpg.healcity.ui.missions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.FitnessTrackingActivity
import cmsc436.rpg.healcity.User
import java.lang.Integer.parseInt


class MissionFragment : Fragment() {

    private lateinit var mListView: ListView
    private lateinit var steps: Number
    private lateinit var mTitles: ArrayList<String>
    private lateinit var mDesc: ArrayList<String>
    private lateinit var mProg: ArrayList<Double>
    private lateinit var mLength: ArrayList<Int>
    private lateinit var simpleAdapter: SimpleAdapter

    private val from = arrayOf("listview_title", "listview_description", "listview_progress")
    private val to = intArrayOf(
        R.id.mission_title,
        R.id.mission_description,
        R.id.mission_progress
    )

    private val aList = ArrayList<HashMap<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_missions, container, false)

        mTitles = MainActivity.missionTitles
        mDesc = MainActivity.missionDesc
        mProg = MainActivity.missionProg
        mLength = MainActivity.missionLength

        val addMissionButton = root.findViewById(R.id.AddNewMissionButton) as Button
        addMissionButton.setOnClickListener{
            addNewMission()
        }

        val startWalkingButton = root.findViewById(R.id.StartWalking) as Button
        startWalkingButton.setOnClickListener {
            trackSteps()
        }

        for (i in mTitles.indices) {
            val hm = HashMap<String, String>()
            hm["listview_title"] = mTitles[i]
            hm["listview_description"] = mDesc[i]
            hm["listview_progress"] = mProg[i].toString() + "/" + mLength[i].toString()
            aList.add(hm)
        }

        simpleAdapter = SimpleAdapter(context, aList, R.layout.mission, from, to)

        mListView = root.findViewById(R.id.mission_list)
        mListView.adapter = simpleAdapter

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    @SuppressLint("InflateParams")
    private fun addNewMission() {
        val view = layoutInflater.inflate(R.layout.add_mission_dialog, null)
        val dialog = AlertDialog.Builder(context!!)
        dialog.setView(view)
        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            // Retrieve values
            val title = view.findViewById<EditText>(R.id.added_mission_title).text.toString()
            val type = view.findViewById<Spinner>(R.id.added_mission_type).selectedItem.toString()
            val length = parseInt(view.findViewById<Spinner>(R.id.added_mission_length).selectedItem.toString())
            val hm = HashMap<String, String>()

            aList.clear()
            // Add to list
            hm["listview_title"] = title
            if (type == "Walk") {
                hm["listview_description"] = "Walk $length miles"
            } else {
                hm["listview_description"] = "Check-In at $length locations"
            }
            hm["listview_progress"] = "0.0/$length"

            //Store values for future
            MainActivity.missionTitles.add(title)
            MainActivity.missionDesc.add(hm["listview_description"].toString())
            MainActivity.missionProg.add(0.0)
            MainActivity.missionLength.add(length)
            simpleAdapter.notifyDataSetChanged()

            val fragment = fragmentManager!!.findFragmentById(R.id.nav_host_fragment)!!
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(fragment)
            ft?.attach(fragment)
            ft?.commit()

        }
        dialog.setNegativeButton("Cancel") { d: DialogInterface, _: Int ->
            d.cancel()
        }
        dialog.show()
    }

    private fun trackSteps() {
        val intent = Intent(context!!, FitnessTrackingActivity::class.java)
        startActivityForResult(intent, FITNESS_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FITNESS_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK){

                val toRemove = arrayListOf<Int>()

                steps = data?.getIntExtra(MainActivity.STEP_KEY, 0)!!

                val distance = (steps as Int)/2.0 // TODO: 2000.0

                aList.clear()
                for (i in MainActivity.missionTitles.indices) {
                    if (MainActivity.missionDesc[i].split(" ")[0] == "Walk") { // If it is a walking goal, then update
                        if (MainActivity.missionProg[i] + distance >=  MainActivity.missionLength[i]) {
                            // Mission completed
                            MainActivity.missionProg[i] = MainActivity.missionLength[i]*1.0
                            val sharedPref = context!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
                            val user = User.getPlayer(sharedPref)!!
                            val reward = MainActivity.missionLength[i]*10
                            User.addExp(user, reward)
                            toRemove.add(i)
                            Toast.makeText(context!!, "You completed a mission for $reward exp!", Toast.LENGTH_SHORT).show()
                        } else {
                            MainActivity.missionProg[i] += distance
                            simpleAdapter.notifyDataSetChanged()
                        }
                    }
                }
                removeMission(toRemove.reversed())
                val fragment = fragmentManager!!.findFragmentById(R.id.nav_host_fragment)!!
                val ft = fragmentManager?.beginTransaction()
                ft?.detach(fragment)
                ft?.attach(fragment)
                ft?.commit()
            }
        }
    }

    private fun removeMission(indexes: List<Int>) {
        for (i in indexes) {
            MainActivity.missionTitles.removeAt(i)
            MainActivity.missionDesc.removeAt(i)
            MainActivity.missionLength.removeAt(i)
            MainActivity.missionProg.removeAt(i)
            simpleAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val FITNESS_REQ_CODE = 1
    }


}