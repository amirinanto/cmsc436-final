package cmsc436.rpg.healcity.ui.map

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private lateinit var notificationsViewModel: MapViewModel
    private lateinit var googleMap: SupportMapFragment

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var map: GoogleMap

    private var zoomLevel: Float = 18.0f


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)

        var root = inflater.inflate(R.layout.fragment_map, container, false)

        //map start thingy
        googleMap = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        googleMap.getMapAsync(this)

        //location api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        return root
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    override fun onMapReady(gMap: GoogleMap) {
        map = gMap

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!,  R.raw.night_map))
        map.uiSettings.setAllGesturesEnabled(false)
        map.setOnMarkerClickListener(this)

        //request permission
        setUpMap()
        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener {
            location ->
            if (location != null) {
                Log.i(MainActivity.TAG, "location, lat: ${location.latitude} long: ${location.longitude}")
                lastLocation = location
                val curLatLng = LatLng(location.latitude, location.longitude)
                placeMarker(curLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, zoomLevel))
            } else {
                Log.i(MainActivity.TAG, "NULL LOCATION")
            }
        }
    }

    private fun placeMarker(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        map.addMarker(markerOptions)

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}