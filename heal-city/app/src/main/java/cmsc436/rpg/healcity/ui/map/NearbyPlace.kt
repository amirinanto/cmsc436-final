package cmsc436.rpg.healcity.ui.map

import android.location.Location
import java.util.*

data class NearbyPlace (var name: String,
                        var distance: Float = -1f,
                        var id: String = "",
                        var reward_exp: Int = 5)