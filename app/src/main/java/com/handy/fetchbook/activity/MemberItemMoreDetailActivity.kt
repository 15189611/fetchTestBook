package com.handy.fetchbook.activity

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.handy.fetchbook.R
import com.handy.fetchbook.adapter.ImageStringAdapter
import com.handy.fetchbook.basic.ext.parseMyState
import com.handy.fetchbook.basic.util.BooKLogger
import com.handy.fetchbook.viewModel.MemberItemMoreDetailViewModel
import com.hjq.toast.ToastUtils
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_my_buy_wallet_history_view.back
import kotlinx.android.synthetic.main.expo_activity_expo_detail.vBanner
import kotlinx.android.synthetic.main.member_item_more_detail_activity.btn_buy
import kotlinx.android.synthetic.main.member_item_more_detail_activity.btn_cancel
import kotlinx.android.synthetic.main.member_item_more_detail_activity.btn_ok
//import kotlinx.android.synthetic.main.member_item_more_detail_activity.iv_image
import kotlinx.android.synthetic.main.member_item_more_detail_activity.lin_body
import kotlinx.android.synthetic.main.member_item_more_detail_activity.lin_buy
import kotlinx.android.synthetic.main.member_item_more_detail_activity.sv_body
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_balance
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_end_date
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_item_price
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_name
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_price
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_provice
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_start_date
import kotlinx.android.synthetic.main.member_item_more_detail_activity.webView
import kotlinx.android.synthetic.main.member_item_more_detail_activity.tv_pttime
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.ext.parseState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class MemberItemMoreDetailActivity : BaseVmActivity<MemberItemMoreDetailViewModel>() {

    var package_id: String? = null;
    var price: String? = null;
    var balance: String? = null;

    override fun createObserver() {
        mViewModel.myBuyHistoryResult.observe(this) {
            parseMyState(it, onSuccess = { isRefresh, model ->
                BooKLogger.d("createObserver:" + model.name)
                lin_body.visibility = View.VISIBLE
                tv_start_date.text = model.start_date;
                tv_end_date.text = model.end_date;
                tv_name.text = model.name;
                tv_provice.text = model.province;
                tv_item_price.text = model.price.toString()
                tv_price.text = model.price.toString();
                tv_pttime.text = model.start_date + "-" + model.end_date;
//                Glide.with(iv_image.context)
//                    .load(model.image)
//                    .into(iv_image)
                var banner: List<String> = arrayListOf(model.image.toString());
                vBanner.setLoopTime(5000)
                vBanner.addBannerLifecycleObserver(this@MemberItemMoreDetailActivity)
                    .setAdapter(ImageStringAdapter(this, banner)).indicator =
                    CircleIndicator(this)
                vBanner.setIndicatorNormalColor(Color.DKGRAY)
                vBanner.setIndicatorSelectedColor(Color.RED)

                if (webView != null) {
                    webView.loadData(getNewContent(model.description.toString()).toString(), "text/html;charset=UTF-8", null)
                }
            }, onError = { isRefresh, error ->
                BooKLogger.d("MemberItemMoreDetailActivity-> ${error.errCode}")
            })
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        package_id = intent.getStringExtra("id");
        price = intent.getStringExtra("price");
        BooKLogger.d("package_id:" + package_id)
        back.setOnClickListener { finish() }
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        webView.webViewClient = ArticleWebViewClient()
        mViewModel.memberItemDetail(package_id.toString());

        btn_buy.setOnClickListener {
            lin_buy.visibility = View.VISIBLE;
            btn_buy.background = resources.getDrawable(R.drawable.btn_bk_gray)
            scrollToEnd();
        }
        btn_cancel.setOnClickListener{
            lin_buy.visibility = View.GONE;
            btn_buy.background = resources.getDrawable(R.drawable.btn_bk)
            scrollToEnd();
        }
        btn_ok.setOnClickListener {

            if (price!! > balance.toString()){
                showPing(btn_ok);
            }else{
                ToastUtils.show("余额不足!")
            }

        }
        mViewModel.wallet()
        mViewModel.walletResult.observe(this){ resultState ->
            parseState(resultState, {
                balance = it.invest
                tv_balance.text = it.invest;
            })
        }
    }

    override fun layoutId() = R.layout.member_item_more_detail_activity

    override fun showLoading(message: String) {}

    fun scrollToEnd() {
        sv_body.post(Runnable {
            sv_body.fullScroll(View.FOCUS_DOWN);//滚到底部
        });
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    fun imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++) " +
                "{"
                + "var img = objs[i]; " +
                " img.style.maxWidth = '100%'; img.style.height = 'auto'; " +
                "}" +
                "})()");
    }

    private class ArticleWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            //重置webview中img标签的图片大小

        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    private fun getNewContent(htmlText: String): String? {
        val doc: Document = Jsoup.parse(htmlText)
        val elements: Elements = doc.getElementsByTag("img")
        for (element in elements) {
            element.attr("width", "100%").attr("height", "auto")
        }
        return doc.toString()
    }

    private var logoutPop: PopupWindow? = null
    private var logoutPopView: View? = null
    private fun showPing(view:View) {
        val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        logoutPopView = inflater.inflate(R.layout.home_dialog_pinglu, null)
        logoutPop = PopupWindow(
            logoutPopView,
            WindowManager.LayoutParams.FILL_PARENT,
            WindowManager.LayoutParams.FILL_PARENT,
            true
        )
        logoutPopView!!.findViewById<ImageView>(R.id.aiv_close).setOnClickListener {
            logoutPop!!.dismiss()
        }
        logoutPopView!!.findViewById<TextView>(R.id.atv_title).text = "购买成功!"
        logoutPop!!.setBackgroundDrawable(BitmapDrawable())
        logoutPop!!.animationStyle = R.style.common_CustomDialog
        logoutPop!!.showAsDropDown(view)
    }
}