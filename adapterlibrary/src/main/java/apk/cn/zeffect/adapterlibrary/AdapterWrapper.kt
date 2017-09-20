package cn.zeffect.notes.recycler

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import apk.cn.zeffect.adapterlibrary.WrapperUtils


/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/09/19
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class AdapterWrapper<T : RecyclerView.ViewHolder>(adapter: RecyclerView.Adapter<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = AdapterWrapper::class.java.name
    override fun getItemCount(): Int {
        return if (isEmpty()) 1 else mAdapter.itemCount + mHeaderViews.size() + mFooterViews.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            isEmpty() -> {
                if (mEmptyView == null)
                    mEmptyView = generateEmpty(parent.context)
                BaseViewHolder(mEmptyView!!)
            }
            mHeaderViews.get(viewType) != null -> BaseViewHolder(mHeaderViews.get(viewType))
            mFooterViews.get(viewType) != null -> BaseViewHolder(mFooterViews.get(viewType))
            else -> mAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        return when {
            isEmpty() -> return
            isHeader(position) -> return
            isFooter(position) -> return
            else -> mAdapter.onBindViewHolder(holder as T, position - mHeaderViews.size())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty() -> return Int.MAX_VALUE
            isHeader(position) -> mHeaderViews.keyAt(position)
            isFooter(position) -> mFooterViews.keyAt(position - mHeaderViews.size() - mAdapter.itemCount)
            else -> mAdapter.getItemViewType(position - mHeaderViews.size())
        }
    }

    private val mAdapter by lazy { adapter }
    private var mEmptyView: View? = null
    private val mHeaderViews = SparseArray<View>()
    private val mFooterViews = SparseArray<View>()
    /**布局为空item**/
    private val HEADER_POSITION = Int.MIN_VALUE + 1
    private val FOOTER_POSITION = Int.MIN_VALUE + 1000

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        mAdapter.registerAdapterDataObserver(mAdapterObserver)
        WrapperUtils.onAttachedToRecyclerView(mAdapter, recyclerView, WrapperUtils.SpanSizeCallback { layoutManager, oldLookup, position ->
            val viewType = getItemViewType(position)
            if (mHeaderViews.get(viewType) != null) {
                return@SpanSizeCallback layoutManager.spanCount
            } else if (mFooterViews.get(viewType) != null) {
                return@SpanSizeCallback layoutManager.spanCount
            }
            oldLookup?.getSpanSize(position) ?: 1
        })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        mAdapter.unregisterAdapterDataObserver(mAdapterObserver)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        mAdapter.onViewAttachedToWindow(holder as T)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < mHeaderViews.size()
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= mHeaderViews.size() + mAdapter.itemCount
    }


    fun setEmptyView(view: View) {
        if (view != null) mEmptyView = view
    }

    fun addHeader(view: View) {
        if (view != null) mHeaderViews.put(HEADER_POSITION + mHeaderViews.size(), view)
    }

    fun addFooter(view: View) {
        if (view != null) mFooterViews.put(FOOTER_POSITION + mFooterViews.size(), view)
    }

    private fun isHeader(position: Int): Boolean = position < mHeaderViews.size()
    private fun isFooter(position: Int): Boolean = position >= mHeaderViews.size() + mAdapter.itemCount

    private fun isEmpty(): Boolean = mAdapter.itemCount == 0

    private val mAdapterObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                this@AdapterWrapper.notifyDataSetChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                this@AdapterWrapper.notifyItemRangeInserted(positionStart + mHeaderViews.size(), itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (isEmpty())
                    this@AdapterWrapper.notifyDataSetChanged()
                else
                    this@AdapterWrapper.notifyItemRangeRemoved(positionStart + mHeaderViews.size(), itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                this@AdapterWrapper.notifyItemChanged(positionStart + mHeaderViews.size(), itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                this@AdapterWrapper.notifyItemRangeChanged(positionStart + mHeaderViews.size(), itemCount, payload)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                this@AdapterWrapper.notifyItemMoved(fromPosition, toPosition)
            }

        }
    }

    private class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun generateEmpty(context: Context) = LinearLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        gravity = Gravity.CENTER
        addView(TextView(context).apply { text = "无数据";setTextColor(Color.parseColor("#aaaaaa")) })
    }
}