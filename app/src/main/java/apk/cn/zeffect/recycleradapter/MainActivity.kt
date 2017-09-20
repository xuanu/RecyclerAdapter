package apk.cn.zeffect.recycleradapter

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import cn.zeffect.notes.recycler.AdapterWrapper
import org.jetbrains.anko.find

class MainActivity : Activity() {
    private val mContext by lazy { this }
    private val mRecycler by lazy { find<RecyclerView>(R.id.recycler) }
    private val mDatas by lazy { arrayListOf<String>() }
    private val mAdapter by lazy { MyAdapter(mDatas) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mRecycler?.layoutManager = GridLayoutManager(this, 2);//LinearLayoutManager(this)
        val tempAdapter = AdapterWrapper(mAdapter)
        tempAdapter.addHeader(TextView(mContext).apply { text = "头部1" })
        tempAdapter.addHeader(TextView(mContext).apply { text = "头部2" })
        tempAdapter.addHeader(TextView(mContext).apply { text = "头部3" })
        tempAdapter.addHeader(TextView(mContext).apply { text = "头部4" })
        tempAdapter.addFooter(TextView(mContext).apply { text = "尾部1" })
        tempAdapter.addFooter(TextView(mContext).apply { text = "尾部2" })
        tempAdapter.addFooter(TextView(mContext).apply { text = "尾部3" })
        tempAdapter.addFooter(TextView(mContext).apply { text = "尾部4" })
//        tempAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.layout_default_empty, null))
        mRecycler.adapter = tempAdapter
        find<Button>(R.id.clear).setOnClickListener { val size = mDatas.size; mDatas.clear();mAdapter.notifyDataSetChanged() }
        find<Button>(R.id.add).setOnClickListener { mDatas.add("随机时间${System.currentTimeMillis()}");mAdapter.notifyItemInserted(mDatas.size - 1) }
        find<Button>(R.id.remove).setOnClickListener {
            if (mDatas.size < 1) return@setOnClickListener
            val size = mDatas.size - 1;mDatas.removeAt(size);mAdapter.notifyItemRemoved(size)
        }
        find<Button>(R.id.update).setOnClickListener { if (mDatas.size < 1) return@setOnClickListener;mDatas[0] = "当前时间${System.currentTimeMillis()}";mAdapter.notifyItemChanged(0) }
    }
}
