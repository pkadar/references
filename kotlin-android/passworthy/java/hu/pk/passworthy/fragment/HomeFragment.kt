package hu.pk.passworthy.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import hu.pk.passworthy.R
import hu.pk.passworthy.adapter.ItemAdapter
import hu.pk.passworthy.data.Item
import hu.pk.passworthy.data.ItemDatabase
import hu.pk.passworthy.databinding.FragmentHomeBinding
import hu.pk.passworthy.dialogfragment.AddNewItemDialogFragment
import hu.pk.passworthy.dialogfragment.DeleteAllConfirmationDialogFragment
import hu.pk.passworthy.dialogfragment.DeleteItemConfirmationDialogFragment
import hu.pk.passworthy.encryption.Encryption
import hu.pk.passworthy.notification.Notification
import kotlin.concurrent.thread

class HomeFragment : Fragment(), ItemAdapter.ItemClickListener,
    AddNewItemDialogFragment.AddNewItemDialogListener,
    DeleteAllConfirmationDialogFragment.DeleteAllItemListener,
    DeleteItemConfirmationDialogFragment.DeleteItemListener{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: ItemDatabase
    private lateinit var adapter: ItemAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ItemDatabase.getDatabase(requireContext())
        val sP = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if(!Notification.onStartHappenedOnce) {
            if (sP.getBoolean("notification", false)) {
                val currMinusGiven =
                    System.currentTimeMillis() - sP.getLong("daysuntilalert", 7776000000)
                thread {
                    val oldPwCount = database.itemDao().getNumbersOfOldPasswords(currMinusGiven)
                    if (oldPwCount > 0)
                        Notification.sendNotification(
                            requireContext(),
                            getString(R.string.notification_title),
                            getString(R.string.notification_text, oldPwCount)
                        )
                }
            }
        }
        Notification.onStartHappenedOnce = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.fab.setOnClickListener {
            AddNewItemDialogFragment(this, null).show(
                requireActivity().supportFragmentManager,
                AddNewItemDialogFragment.TAG
            )
        }

        binding.fabDeleteAll.setOnClickListener {
            DeleteAllConfirmationDialogFragment(this).show(
                requireActivity().supportFragmentManager,
                DeleteAllConfirmationDialogFragment.TAG
            )
        }
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView(){
        adapter = ItemAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = adapter

        loadItemsInBackground()
    }

    override fun onExpiredIconShow(item: Item): Boolean {
    val sP = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if(sP.getBoolean("notification", false)){
            val currMinusGiven =
                System.currentTimeMillis() - sP.getLong("daysuntilalert", 7776000000)
            if(item.lastModified < currMinusGiven) {
                return true
            }
        }
        return false
    }

    private fun loadItemsInBackground(){
        thread {
            val items = database.itemDao().getAll()
            Encryption.decryptAllItems(items)
            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemCreated(newItem: Item){
        thread {
            val key = database.itemDao().insert(Encryption.encryptItem(newItem))
            newItem.id = key
            requireActivity().runOnUiThread {
                adapter.addItem(Encryption.decryptItem(newItem))
            }
        }
    }

    override fun onItemEdit(item: Item) {
        AddNewItemDialogFragment(this, item).show(
            requireActivity().supportFragmentManager,
            AddNewItemDialogFragment.TAG
        )
    }

    override fun onItemEdited(item: Item){
        thread {
            database.itemDao().update(Encryption.encryptItem(item))

            requireActivity().runOnUiThread {
                adapter.updateItem(Encryption.decryptItem(item))
            }
        }
    }

    override fun onAllItemDeleteConfirmed() {
        thread {
            database.itemDao().deleteAll()

            requireActivity().runOnUiThread {
                adapter.removeAll()
            }
        }
    }

    override fun onItemDeleteDialogOpen(removedItem: Item) {
        DeleteItemConfirmationDialogFragment(this, removedItem).show(
            requireActivity().supportFragmentManager,
            DeleteItemConfirmationDialogFragment.TAG
        )
    }

    override fun onDeleteItemListener(itemToDelete: Item) {
        thread {
            database.itemDao().deleteItem(itemToDelete)

            requireActivity().runOnUiThread {
                adapter.removeItem(itemToDelete)
            }
        }
    }
}