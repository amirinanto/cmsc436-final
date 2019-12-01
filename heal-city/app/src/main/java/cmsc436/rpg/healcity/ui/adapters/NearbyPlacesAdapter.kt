import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import kotlinx.android.synthetic.main.nearby_map_list_item.view.*

class NearbyPlacesAdapter(val places : List<NearbyPlace>, val onClick: (Int) -> Unit) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.nearby_map_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.onBind(places[position], onClick, position)

    fun refresh() = notifyDataSetChanged()

    override fun getItemCount(): Int = places.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(place: NearbyPlace, onClick: (Int) -> Unit, position: Int) {
            with (itemView) {
                place_name.text = place.name
                place_reward.text = place.reward_exp.toString()
                place_distance.text = place.distance.toString()
                if (place.checked) {
                    place_checked.isChecked = true
                    place_checked.isClickable = false
                } else
                    place_checked.setOnClickListener { onClick(position) }
            }
        }
    }
}
