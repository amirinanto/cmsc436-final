package cmsc436.rpg.healcity.ui.map

import android.location.Location
import java.util.*

/**
 * This class is the representation of a place for the "Map" tab
 *
 * @author Muchlas Amirinanto
 */
data class NearbyPlace (var name: String,
                        var distance: Float = -1f,
                        var id: String = "",
                        var reward_exp: Int = 5,
                        var checked: Boolean = false)