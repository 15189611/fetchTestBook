import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.handy.fetchbook.R
import com.handy.fetchbook.data.ChatDataBean

open class AIChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: ArrayList<ChatDataBean>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ai_chat_item, parent, false)
        )
    }

    fun getLastData() = getPositionData(mData?.size!! - 1)

    private fun getPositionData(position: Int): ChatDataBean? {
        val size = mData?.size
        if (size != null) {
            if (size > position) {
                return mData?.get(position)
            }
        }
        return null
    }

    override fun getItemCount(): Int {
        return if (null == mData) 0 else mData!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mData?.let {
            (holder as ViewHolder).setData(it[position])
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)!!
        val mPbLoading = itemView.findViewById<ProgressBar>(R.id.pb_loading)!!
        val tvMsg = itemView.findViewById<TextView>(R.id.tv_msg)!!
        val ivMineHead = itemView.findViewById<ImageView>(R.id.iv_mine_head)!!
        val ivOtherHead = itemView.findViewById<ImageView>(R.id.iv_other_head)!!

        fun setData(data: ChatDataBean) {
            tvMsg.text = data.data
            tvName.text = if (data.isMySelf) "" else "非去不可AI导游"
            tvName.visibility = if (data.isMySelf) View.GONE else View.VISIBLE
            ivMineHead.visibility = if (data.isMySelf) View.VISIBLE else View.GONE
            ivOtherHead.visibility = if (data.isMySelf) View.GONE else View.VISIBLE
            tvMsg.visibility = if (data.isLoading) View.GONE else View.VISIBLE
            mPbLoading.visibility = if (data.isLoading) View.VISIBLE else View.GONE
        }
    }

    fun addMsg(recyclerView: RecyclerView, data: ChatDataBean) {
        mData?.add(data)
        notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(itemCount - 1)
    }

    fun notifyView(recyclerView: RecyclerView) {
        notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(itemCount - 1)
    }
}