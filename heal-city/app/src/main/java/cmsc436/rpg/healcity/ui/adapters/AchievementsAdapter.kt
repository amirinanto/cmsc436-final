package cmsc436.rpg.healcity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.ui.me.Achievement
import kotlinx.android.synthetic.main.achievement_list_item.view.*

class AchievementsAdapter(val items : List<Achievement>): RecyclerView.Adapter<AchievementsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.achievement_list_item, parent, false))

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.achievementName.text = items[position].name
        holder.achievementDate.text = items[position].date
    }

    fun refresh() = notifyDataSetChanged()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val achievementName = itemView.achievement_name
        val achievementDate = itemView.achievement_date
    }

}