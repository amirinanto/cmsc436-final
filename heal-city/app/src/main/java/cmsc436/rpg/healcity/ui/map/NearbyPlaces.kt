package cmsc436.rpg.healcity.ui.map

import android.location.Location
import java.util.*

data class NearbyPlaces (var name: String,
                         var lat: Double,
                         var lng: Double,
                         var id: Int = -1,
                         var reward_exp: Int = 0,
                         var checked: Boolean = false,
                         var checkInDate: Date = Date(0)) {

    fun getDistance(from_lat: Double, from_long: Double) : Float {
        with(floatArrayOf()) {
            Location.distanceBetween(from_lat, from_long, lat, lng,this)
            return this[0]
        }
    }

    fun checkIn(date: Date): Boolean {
        //already checked in
        if (checked) return false

        checkInDate = date
        checked = true
        return true
    }
}