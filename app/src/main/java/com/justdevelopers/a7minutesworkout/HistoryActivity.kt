package com.justdevelopers.a7minutesworkout


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.justdevelopers.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding:ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistory)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        val historyDao = (application as HistoryApp).db.historyDao()
        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }
        lifecycleScope.launch {
            historyDao.fetchAll().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list,historyDao)
            }
        }
    }

    private fun setupListOfDataIntoRecyclerView(list: ArrayList<HistoryEntity>, historyDao: HistoryDao) {
            if(list.isNotEmpty()){
                val itemAdapter= HistoryAdapter(list)
                binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
                binding?.rvItemsList?.adapter = itemAdapter
                binding?.rvItemsList?.visibility = View.VISIBLE

            }else{
                binding?.rvItemsList?.visibility = View.GONE
            }
        }


    override fun onBackPressed() {
//        val dialog = AlertDialog.Builder(this)
//        dialog.setTitle("Alert")
//        dialog.setMessage("Are you sure want to exit")
//        dialog.setPositiveButton("ok"
//        ) { dialog, which ->
//            run {
//                dialog.dismiss()
//                finish()
//            }
//        }
//        dialog.setNegativeButton("cancel"
//        ) { dialog,which ->
//            run{
//                dialog.dismiss()
//            }
//        }
//        dialog.setCancelable(true)
//        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}