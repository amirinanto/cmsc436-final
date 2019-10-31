package cmsc436.rpg.healcity.ui.missions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.R

class MissionFragment : Fragment() {

    private lateinit var dashboardViewModel: MissionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(MissionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_missions, container, false)
        return root
    }


}