package com.handy.fetchbook.basic.ext

/**
 * - Author: Charles
 * - Date: 2023/9/1
 * - Description:
 */

fun String.toAvatar(): String {
    if (this.isEmpty()) return ""
    return "http://p28web.bbq.bet/$this"
}