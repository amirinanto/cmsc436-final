import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import kotlinx.android.synthetic.main.nearby_map_list_item.view.*

/**
 * This is the adapter for nearby places in "map" tab
 *
 * It takes a list of Places and a function to execute when check in radio is clicked
 *
 * @author Muchlas Amirinanto
 */
class NearbyPlacesAdapter(val places : List<NearbyPlace>, val onClick: (NearbyPlace) -> Unit) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {


    /**
     * This will inflate the layout file for each item
     *
     * @author Muchlas Amirinanto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nearby_map_list_item, parent, false))

    /**
     * This will bind data from the achievement to each view
     *
     * @author Muchlas Amirinanto
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.onBind(places[position], onClick)

    /**
     * Refreshes the list view and data
     *
     * @author Muchlas Amirinanto
     */
    fun refresh() = notifyDataSetChanged()

    /**
     * Returns the number of places in the list
     *
     * @author Muchlas Amirinanto
     */
    override fun getItemCount(): Int = places.size


    /**
     * This is the ViewHolder class that helps with referencing each item's views
     *
     * @author Muchlas Amirinanto
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(place: NearbyPlace, onClick: (NearbyPlace) -> Unit) {
            with (itemView) {
                place_name.text = place.name
                place_reward.text = place.reward_exp.toString()
                place_distance.text = place.distance.toString()
                if (place.checked)
                    place_checked.isChecked = true

                place_checked.setOnClickListener { onClick(place) }
            }
        }
    }
}
