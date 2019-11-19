package cmsc436.rpg.healcity.ui.map

import NearbyPlacesAdapter
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.fragment_map.*
import org.json.JSONArray
import org.json.JSONObject
import java.security.Permission
import kotlin.random.Random

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener{


    private lateinit var notificationsViewModel: MapViewModel
    private lateinit var googleMap: SupportMapFragment

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lastLocation: Location? = null
    private var lastMarker: Marker? = null

    private lateinit var map: GoogleMap

    private lateinit var placesClient: PlacesClient

    private var googleApiClient: GoogleApiClient? = null

    private var zoomLevel: Float = 18.0f

    private var nearbyPlacesList = ArrayList<NearbyPlaces>()
    private lateinit var adapter: NearbyPlacesAdapter

    private lateinit var warningText: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)

        var root = inflater.inflate(R.layout.fragment_map, container, false)

        //map start thingy
        googleMap = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        googleMap.getMapAsync(this)

        //location api
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        warningText = root.findViewById(R.id.map_warning)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nearbyPlacesList = ArrayList<NearbyPlaces>()

        adapter = NearbyPlacesAdapter(nearbyPlacesList)
        listview_nearby.layoutManager = LinearLayoutManager(context!!)
        listview_nearby.adapter = adapter

        Places.initialize(context!!, resources.getString(R.string.google_maps_key))
        placesClient = Places.createClient(context!!)
    }


    override fun onMarkerClick(p0: Marker?): Boolean = false

    override fun onMapReady(gMap: GoogleMap) {
        map = gMap

        lastMarker = map.addMarker(MarkerOptions().position(LatLng(0.toDouble(),0.toDouble())))

        //seting up map
        map.apply {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!,  R.raw.night_map))
            uiSettings.setAllGesturesEnabled(false)
            setOnMarkerClickListener(this@MapFragment)
        }

        //request permission
        setUpPermission()

        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            noLocationProvided()
            return
        }

        //show button on top right to move to current location
        map.isMyLocationEnabled = true

        //when fusedLocationClient success on getting location
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location ->
            requestLocation(location)
        }.addOnFailureListener {
            Log.e(MainActivity.TAG, "fusedLocationClient fail to get location")
        }
    }

    private fun noLocationProvided() {
        warningText.visibility = View.VISIBLE
    }

    private fun setUpPermission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)

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
            var time = System.currentTimeMillis()

            if (lastLocation != null) {
                lat = lastLocation!!.latitude.toString()
                lng = lastLocation!!.longitude.toString()
                time = lastLocation!!.time
            }

            putString(LAT_KEY, lat)
            putString(LNG_KEY, lng)
            putLong(TIME_KEY, time)

            commit()

        }
    }

    private fun requestLocation(location: Location?) {
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

    private fun updateUserMarker(location: LatLng) {
        if (lastMarker != null) lastMarker?.remove()
        else lastMarker = map.addMarker(MarkerOptions().position(location))

    }

    private fun populateNearby() {
        // Use fields to define the data types to return.
        var placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG )

        // Use the builder to create a FindCurrentPlaceRequest.
        var request = FindCurrentPlaceRequest.builder(placeFields).build()
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener {
                for (result in it.placeLikelihoods) {
                    Log.i(MainActivity.TAG, "name: ${result.place.name}, likelihood: ${result.place}")

                    val place = result.place
                    val lat = place.latLng?.latitude
                    val lng = place.latLng?.longitude
                    addNearbyPlace(NearbyPlaces(place.name!!, lat!!, lng!!))
                }
            }.addOnFailureListener {
                if (it is ApiException) {
                    Log.e(MainActivity.TAG, "not found: ${it}")
                }
            }

        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            setUpPermission()

        }

    }

    override fun onLocationChanged(location: Location?) {
        requestLocation(location)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }

    private fun getPlaces() {

    }

    fun addNearbyPlace(nearbyPlace: NearbyPlaces) {
        if (nearbyPlacesList.size > 9) return
        nearbyPlacesList.add(nearbyPlace)
        adapter.refresh()
        Log.d(MainActivity.TAG, nearbyPlace.name + " added, list: " + nearbyPlacesList)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private const val LAT_KEY = "USER_LAT"
        private const val LNG_KEY = "USER_LNG"
        private const val TIME_KEY = "USER_TIME"

        private const val ZERO = 0.toDouble()
    }
}