package hu.pk.passworthy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import hu.pk.passworthy.data.Item
import hu.pk.passworthy.databinding.OneRowBinding

class ItemAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Item>()

    private lateinit var tmpHolder: ItemViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        OneRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int){
        val item = items[position]

        holder.binding.itemName.text = item.name
        holder.binding.itemUsername.text = item.username
        holder.binding.idOldPw.visibility = View.GONE
        if(listener.onExpiredIconShow(item)){
            holder.binding.idOldPw.visibility = View.VISIBLE
        }

        holder.binding.btnDelete.setOnClickListener {
            listener.onItemDeleteDialogOpen(item)
        }

        holder.binding.oneRowLayout.setOnClickListener {
            listener.onItemEdit(item)
        }

        tmpHolder = holder
    }

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(dataItems: List<Item>) {
        items.clear()
        items.addAll(dataItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: Item){
        var position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    fun updateItem(item: Item){
        notifyItemChanged(items.indexOf(item))
    }

    fun removeAll(){
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface ItemClickListener {
        fun onItemDeleteDialogOpen(item: Item)
        fun onItemEdit(item: Item)

        fun onExpiredIconShow(item: Item):Boolean
    }

    inner class ItemViewHolder(val binding: OneRowBinding) : RecyclerView.ViewHolder(binding.root)
}