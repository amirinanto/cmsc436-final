package cmsc436.rpg.healcity.ui.map

import NearbyPlacesAdapter
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback{


    private lateinit var notificationsViewModel: MapViewModel
    private lateinit var googleMap: SupportMapFragment

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient

    private var nearbyPlacesList = ArrayList<NearbyPlace>()
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

        //location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    updateLocation(location)
                }
            }
        }

        return root
    }

    /**
     * Setting up nearby places list and API Client
     *
     * @author Muchlas Amirinanto
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        nearbyPlacesList = ArrayList<NearbyPlace>()

        adapter = NearbyPlacesAdapter(nearbyPlacesList)
        listview_nearby.layoutManager = LinearLayoutManager(context!!)
        listview_nearby.adapter = adapter

        Places.initialize(context!!, resources.getString(R.string.google_maps_key))
        placesClient = Places.createClient(context!!)
    }

    /**
     * Start location update listener when app is resumed
     * TODO
     *
     * @author Muchlas Amirinanto
     */
    override fun onResume() {
        super.onResume()
        val locationRequest = LocationRequest()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    /**
     * Stop location update listener when app is paused
     *
     * @author Muchlas Amirinanto
     */
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * This function handle the initial map operation, including setting up map
     * and location update listener
     *
     * @author Muchlas Amirinanto
     */
    override fun onMapReady(gMap: GoogleMap) {
        map = gMap

        //seting up map
        map.apply {
            map.isMyLocationEnabled = true
            setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!,  R.raw.night_map))
            uiSettings.setAllGesturesEnabled(false)
        }

        loading_map.visibility = View.GONE

        //request permission TODO
        setUpPermission()

        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            noLocationProvided()
            return
        }


        //when fusedLocationClient success on getting location
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location ->
            updateLocation(location)
            populateNearby()
        }.addOnFailureListener {
            Log.e(MainActivity.TAG, "fusedLocationClient fail to get location")
        }
    }

    /**
     * This function will show error message and disable location-permission-dependent functions
     *
     * @author Muchlas Amirinanto
     */
    private fun noLocationProvided() {
        map_warning.visibility = View.VISIBLE
        map_card.visibility = View.GONE
    }

    /**
     * Requesting location permission for the app
     * Should only be in the beginning ???
     * TODO
     *
     * @author Muchlas Amirinanto
     */
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


    /**
     * Saves last known user location into sharedPreference
     *
     * @author Muchlas Amirinanto
     */
    private fun saveLocation() {
        val pref =  activity!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
        with (pref.edit()) {
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

    /**
     * This function will update user's location marker on the map and save the location
     *
     * @author Muchlas Amirinanto
     * @param location -> new user's location
     */
    private fun updateLocation(location: Location?) {
        if (location != null) {
            Log.i(MainActivity.TAG, "location, lat: ${location.latitude} long: ${location.longitude}")

            lastLocation = location

            saveLocation()

            val curLatLng = LatLng(location.latitude, location.longitude)

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, zoomLevel))
        } else {
            Log.i(MainActivity.TAG, "NULL LOCATION")
        }
    }

    /**
     * This function will fetch information of places nearby the user's current location using
     * Google's Places API
     *
     * @author Muchlas Amirinanto
     */
    private fun populateNearby() {
        // Use fields to define the data types to return.
        var placeFields = listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG )

        // Use the builder to create a FindCurrentPlaceRequest.
        var request = FindCurrentPlaceRequest.builder(placeFields).build()

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            placesClient.findCurrentPlace(request)
                .addOnSuccessListener {
                    for (result in it.placeLikelihoods) {
                        Log.i(MainActivity.TAG, "name: ${result.place.name}, likelihood: ${result.place}")

                        val place = result.place
                        val lat = place.latLng?.latitude
                        val lng = place.latLng?.longitude
                        val id = place.id?.toInt()
                        addNearbyPlace(NearbyPlace(place.name!!, lat!!, lng!!, id = id!!))
                    }

                    if (nearbyPlacesList.isEmpty()) {
                        loading_progress.visibility = View.GONE
                        loading_text.text = resources.getString(R.string.no_nearby)
                    } else {
                        loading_card.visibility = View.GONE
                    }
                }.addOnFailureListener {
                    if (it is ApiException) {
                        Log.e(MainActivity.TAG, "not found: ${it}")
                    }
                }

        } else {
            // A local method to request required permissions;
            setUpPermission()

        }

    }

    /**
     * This function will add a NearbyPlace object to the nearby places list
     * and update the refresher.
     *
     * Note:
     * We limit the number of Nearby Place to be shown to 10 to avoid long list
     *
     * @author Muchlas Amirinanto
     * @param nearbyPlace -> a NearbyPlace object with to be added to the list
     */
    fun addNearbyPlace(nearbyPlace: NearbyPlace) {
        if (nearbyPlacesList.size >= 10) return
        nearbyPlacesList.add(nearbyPlace)
        adapter.refresh()
        Log.d(MainActivity.TAG, nearbyPlace.name + " added, list: " + nearbyPlacesList)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private const val LAT_KEY = "USER_LAT"
        private const val LNG_KEY = "USER_LNG"
        private const val TIME_KEY = "USER_TIME"

        private const val zoomLevel = 18.0f
    }
}