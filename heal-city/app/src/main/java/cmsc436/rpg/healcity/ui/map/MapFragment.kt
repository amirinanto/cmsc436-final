package cmsc436.rpg.healcity.ui.map

import NearbyPlacesAdapter
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.nearby_map_list_item.view.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener{


    private lateinit var notificationsViewModel: MapViewModel
    private lateinit var googleMap: SupportMapFragment

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var lastMarker: Marker? = null

    private lateinit var map: GoogleMap

    private var zoomLevel: Float = 18.0f

    private var nearbyPlacesList = ArrayList<NearbyPlaces>()
    private lateinit var adapter: NearbyPlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)

        var root = inflater.inflate(R.layout.fragment_map, container, false)

        //map start thingy
        googleMap = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        googleMap.getMapAsync(this)

        //location api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        populateNearby()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nearbyPlacesList = ArrayList<NearbyPlaces>().apply {
            add(NearbyPlaces("a", 1.toDouble(), 2.toDouble()))
            add(NearbyPlaces("b", 2.toDouble(), 3.toDouble()))
        }

        adapter = NearbyPlacesAdapter(nearbyPlacesList)
        listview_nearby.layoutManager = LinearLayoutManager(context!!)
        listview_nearby.adapter = adapter

    }


    override fun onMarkerClick(p0: Marker?): Boolean = false

    override fun onMapReady(gMap: GoogleMap) {
        map = gMap

        lastMarker = map.addMarker(MarkerOptions().position(LatLng(0.toDouble(),0.toDouble())))

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!,  R.raw.night_map))
        map.uiSettings.setAllGesturesEnabled(false)
        map.setOnMarkerClickListener(this)

        //request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
            }
        } else {
            buildGoogleApiClient()
        }

        //show button on top right to move to current location
        map.isMyLocationEnabled = true

        //when fusedLocationClient success on getting location
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location ->
            if (location != null) {
                Log.i(MainActivity.TAG, "location, lat: ${location.latitude} long: ${location.longitude}")

                lastLocation = location

                saveLocation()

                val curLatLng = LatLng(location.latitude, location.longitude)

                updateUserMarker(curLatLng)

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, zoomLevel))
            } else {
                Log.i(MainActivity.TAG, "NULL LOCATION")
            }
        }
    }

    private fun saveLocation() {
        val pref =  activity!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        with (editor) {
            //default
            var lat = "0"
            var lng = "0"

            if (lastLocation != null) {
                lat = lastLocation!!.latitude.toString()
                lng = lastLocation!!.longitude.toString()
            }

            putString(LAT_KEY, lat)
            putString(LNG_KEY, lng)

            commit()

        }
    }

    private fun buildGoogleApiClient() {

    }

    private fun updateUserMarker(location: LatLng) {
        if (lastMarker != null) lastMarker?.remove()
        else lastMarker = map.addMarker(MarkerOptions().position(location))

    }

    private fun addPlaceMarker(location: LatLng) {
        val nearbyPlaces = NearbyPlaces("", 0.toDouble(), 0.toDouble())
        nearbyPlacesList.add(nearbyPlaces)
    }

    private fun populateNearby() {
        if (lastLocation != null) {
            val googleUrl =
                StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/JSON?key=")
                    .append(resources.getString(R.string.google_maps_key))
                    .append("&locationbias=circle:300@")
                    .append(lastLocation!!.latitude)
                    .append(",")
                    .append(lastLocation!!.longitude)
                    .append("&fields=name,geometry,types")
        }

        nearbyPlacesList.add(NearbyPlaces("ERROR: Current Location Not Found", Double.NaN, Double.NaN))


    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            Log.i(MainActivity.TAG, "location, lat: ${location.latitude} long: ${location.longitude}")
            lastLocation = location
            val curLatLng = LatLng(location.latitude, location.longitude)
            updateUserMarker(curLatLng)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, zoomLevel))
        } else {
            Log.i(MainActivity.TAG, "NULL LOCATION")
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    private fun getPlaces() {

    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val PLACE_PICKER_REQUEST = 3

        private const val LAT_KEY = "USER_LAT"
        private const val LNG_KEY = "USER_LNG"
    }
}