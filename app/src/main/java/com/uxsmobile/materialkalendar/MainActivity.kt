package com.uxsmobile.materialkalendar

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.recyclerView

/**
 * @author   Daniel Manrique <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    17/10/2018.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val CATEGORY_SAMPLE = "com.uxsmobile.materialkalendar.app.SAMPLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            val layoutHandler = LinearLayoutManager(this@MainActivity)
            layoutManager = layoutHandler
            adapter = ResolveInfoAdapter(getAllSampleActivities())
            addItemDecoration(DividerItemDecoration(this@MainActivity, layoutHandler.orientation))
        }
    }

    private fun getAllSampleActivities(): List<ResolveInfo> {
        val filter = Intent()
        filter.action = Intent.ACTION_RUN
        filter.addCategory(CATEGORY_SAMPLE)
        return packageManager.queryIntentActivities(filter, 0)
    }

    private fun onRouteClicked(route: ResolveInfo) {
        val activity = route.activityInfo
        val name = ComponentName(activity.applicationInfo.packageName, activity.name)
        startActivity(Intent(Intent.ACTION_VIEW).setComponent(name))
    }

    inner class ResolveInfoAdapter(private val samples: List<ResolveInfo>) :
            RecyclerView.Adapter<ResolveInfoAdapter.ResolveInfoViewHolder>() {

        private val layoutInflater = LayoutInflater.from(this@MainActivity)
        private val packageManager = this@MainActivity.packageManager

        override fun getItemCount(): Int = samples.size

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ResolveInfoViewHolder(layoutInflater.inflate(R.layout.item_activity_routing, viewGroup, false))

        override fun onBindViewHolder(holder: ResolveInfoViewHolder, position: Int) = holder.bindHolder(
                samples[position].loadLabel(packageManager).toString())

        inner class ResolveInfoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

            init {
                view.setOnClickListener {
                    onRouteClicked(samples[adapterPosition])
                }
            }

            fun bindHolder(title: String) {
                (view as? TextView)?.text = title
            }

        }
    }

}