package cmsc436.rpg.healcity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.me.Achievement
import kotlinx.android.synthetic.main.achievement_list_item.view.*


/**
 * This class is the adapter for the achievement list on "me" tab.
 *
 * To create an instance of this adapter, you will need to pass in
 * a List of Achivement
 *
 * @author Muchlas Amirinanto
 */
class AchievementsAdapter(val items : List<Achievement>): RecyclerView.Adapter<AchievementsAdapter.ViewHolder>() {

    /**
     * This will inflate the layout file for each item
     *
     * @author Muchlas Amirinanto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.achievement_list_item, parent, false))

    /**
     * This will return the number of achievements on the list
     *
     * @author Muchlas Amirinanto
     */
    override fun getItemCount(): Int = items.size

    /**
     * This will bind data from the achievement to each view
     *
     * @author Muchlas Amirinanto
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.achievementName.text = items[position].name
        holder.achievementDate.text = items[position].date
    }

    /**
     * This is the ViewHolder class that helps with referencing each item's views
     *
     * @author Muchlas Amirinanto
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val achievementName = itemView.achievement_name
        val achievementDate = itemView.achievement_date
    }

}