package cmsc436.rpg.healcity.ui.missions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.R


class MissionFragment : Fragment() {

    private lateinit var dashboardViewModel: MissionViewModel
    private lateinit var mListView: ListView
    private lateinit var missionList: ListView
    private lateinit var mListAdapter: ListAdapter

    var missions = ArrayList<Mission>()

    private val missionTitles = arrayOf("Walking Goal", "Check-In Goal")
    private val missionDesc = arrayOf("Walk 1 mile", "Check-In at 1 location")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(MissionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_missions, container, false)

        val aList = ArrayList<HashMap<String, String>>()



        for (i in 0..1) {
            val hm = HashMap<String, String>()
            hm["listview_title"] = missionTitles[i]
            hm["listview_discription"] = missionDesc[i]
            aList.add(hm)
        }

        val from = arrayOf("listview_title", "listview_discription")
        val to = intArrayOf(
            R.id.mission_title,
            R.id.mission_description
        )

        val simpleAdapter = SimpleAdapter(context, aList, R.layout.mission, from, to)

        mListView = root.findViewById(R.id.mission_list)
        mListView.adapter = simpleAdapter
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }




//    class MissionListAdapter(context: Context, private var missions: List<Mission>) :
//        SimpleAdapter<Mission>(context, R.layout.mission, missions) {
//
//        private lateinit var missionView: View
//
//        @SuppressLint("ViewHolder")
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//            missionView = inflater.inflate(R.layout.mission, parent.findViewById(R.id.mission_layout), false)
//
//            val missionName = missionView.findViewById<TextView>(R.id.mission_title)
//            //val missionDesc = missionView.findViewById<TextView>(R.id.mission_description)
//
//            val mission = missions[position]
//            missionName.text = mission.missionName
//            //missionDesc.text = mission.missionDesc
//            Log.i("MISSION 12345", missionName.text.toString() + " ") //missionDesc.text.toString())
//
//            return missionView
//        }
//
//        override fun getCount() = 2
//        override fun getItem(position: Int) = missions[position]
//        override fun getItemId(position: Int) = position.toLong()
//
//    }


}