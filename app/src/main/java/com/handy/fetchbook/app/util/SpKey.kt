package com.handy.fetchbook.app.util

/**
 * @Author: QuYunShuo
 * @Time: 2020/8/29
 *
 * "id": 3000001, //账户UID
"avatar": "/preset-avatars/avatar-3.png", //会员头像图片路径
"nickname": "熏悟空", //会员昵称
"account": "a1****@qq.com", //会员账号
"level_desc": "一级代理", //会员等级描述
"verify": 1, //会员是否通过实名认证, 1 = 通过
"notification": {
"share": 0, //会员今日分享任务状态 1=完成
"secret": 0, //会员今日品鉴任务状态 1=完成
"shareTotal": 5, //会员分享任务完成总数
"secretTotal": 6 //会员品鉴任务完成总数
},
"birthday": "2014-02-02", //会员生日
"membership_at": "2023-04-11 23:00:00", //拓展会员激活日
"redeemable_amount": "16530.90",
"lucky_ticket": "1", //旅游抽奖次数
"lucky_bag": 0, //福袋抽奖次数
"location": "北京",
"gender": 1, //会员性别 0=男, 1=女
"invest": "16530.90", //总旅游宝余额
"type": 2, //会员类型 0=普通会员, 1=拓展会员, 2=福袋会员
"type_desc": "福袋会员", //会员类型描述
"level": 1, //会员等级
"last_at": "2023-06-13 10:07:53", //会员最后登录时间
"created_at": "2023-04-11 14:14:46",
"referral": "/sign-up", //推荐链接页
"super": 1, //是否超级福袋会员, 1=是
"cny_restriction": 1,
"wheel_level": 3
 * @Class: SpKey
 * @Remark: 本地存储的键 放在此类中
 */
object SpKey {
    const val MMKV_WITH_ID = "app"
    const val LOGINNAME = "loginName"
    const val LOGINPWD = "loginpwd"
    const val TOKEN = "token"
    const val TOKEN_INFO = "tokenInfo"
    const val USER_INFO = "userInfo"
    const val IS_LOG_IN = "login"
    const val LANGUAGE = "language"
    const val TRANSLATE = "translate"


    const val USER_TYPE = "user_type"//会员类型 0=普通会员, 1=拓展会员, 2=福袋会员
    const val USER_LEVEL = "user_level" //会员等级
    const val USER_LUCKY_TICKET = "user_lucky_ticket" //旅游抽奖次数
    const val USER_LUCKY_BAG = "user_lucky_bag" //福袋抽奖次数

}

