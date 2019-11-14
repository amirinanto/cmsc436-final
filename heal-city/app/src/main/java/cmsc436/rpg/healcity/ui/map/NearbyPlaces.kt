package cmsc436.rpg.healcity.ui.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class NearbyPlaces (var name: String,
                         var lat: Double,
                         var lng: Double,
                         var reward_exp: Int = 0,
                         var checked: Boolean = false) {

    fun getDistance(from_lat: Double, from_long: Double) : Float {
        with(floatArrayOf()) {
            Location.distanceBetween(from_lat, from_long, lat, lng,this)
            return this[0]
        }
    }
}