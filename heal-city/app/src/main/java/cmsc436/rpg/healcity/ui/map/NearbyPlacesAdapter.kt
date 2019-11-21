import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import kotlinx.android.synthetic.main.nearby_map_list_item.view.*

class NearbyPlacesAdapter(val items : List<NearbyPlace>) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nearby_map_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placeName.text = items[position].name
        holder.latitude.text = items[position].lat.toString()
        holder.longitude.text = items[position].lng.toString()
    }

    fun refresh() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName = itemView.place_name_info
        val latitude = itemView.place_distance
        val longitude = itemView.place_reward
    }
}
