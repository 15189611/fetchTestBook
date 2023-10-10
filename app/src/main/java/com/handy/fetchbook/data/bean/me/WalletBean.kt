package com.handy.fetchbook.data.bean.me

import com.google.gson.annotations.SerializedName


data class WalletBean(

    @SerializedName("trc_address") var trcAddress: String? = null,
    @SerializedName("bsc_address") var bscAddress: String? = null,
    @SerializedName("balance") var balance: String? = null,
    @SerializedName("cny_balance") var cnyBalance: String? = null,
    @SerializedName("invest") var invest: String? = null,
    @SerializedName("usd_invest") var usdInvest: String? = null,
    @SerializedName("cny_invest") var cnyInvest: String? = null,
    @SerializedName("integral") var integral: String? = null,
    @SerializedName("static") var static: String? = null,
    @SerializedName("usd") var usd: String? = null,
    @SerializedName("leadership") var leadership: String? = null,
    @SerializedName("activeIncome") var activeIncome: String? = null,
    @SerializedName("team") var team: String? = null,
    @SerializedName("deposit") var deposit: String? = null,
    @SerializedName("withdraw") var withdraw: String? = null,
    @SerializedName("total") var total: String? = null,
    @SerializedName("travel_point_rate") var travelPointRate: String? = null,
    @SerializedName("fee_rate") var feeRate: String? = null,
    @SerializedName("min") var min: String? = null,
    @SerializedName("min_fee") var minFee: String? = null,
    @SerializedName("invest_cny_rate") var investCnyRate: Int? = null,
    @SerializedName("cny_min") var cnyMin: Int? = null,
    @SerializedName("min_invest") var minInvest: String? = null


)