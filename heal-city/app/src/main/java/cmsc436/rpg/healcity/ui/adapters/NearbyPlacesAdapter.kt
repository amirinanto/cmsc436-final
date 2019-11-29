import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import kotlinx.android.synthetic.main.nearby_map_list_item.view.*

class NearbyPlacesAdapter(val places : List<NearbyPlace>, val onClick: (NearbyPlace) -> Unit) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nearby_map_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.onBind(places[position], onClick)

    fun refresh() = notifyDataSetChanged()

    override fun getItemCount(): Int = places.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(place: NearbyPlace, onClick: (NearbyPlace) -> Unit) {
            with (itemView) {
                place_name.text = place.name
                place_reward.text = place.reward_exp.toString()
                place_distance.text = "%.2f".format(place.distance.toString())
                place_checked.setOnClickListener { onClick(place) }
            }
        }
    }
}
