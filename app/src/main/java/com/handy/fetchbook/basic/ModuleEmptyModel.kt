package com.handy.fetchbook.basic

import android.graphics.Color
import androidx.annotation.ColorInt
import com.handy.fetchbook.basic.ext.dp

interface IEmptyDivider

/**
 * 默认分割线
 */
data class ModuleDividerModel(
    val height: Int = 0.5f.dp(),
    val tag: Any? = null,
    val start: Int = 0,
    val end: Int = 0
) : IEmptyDivider

/**
 * 默认灰色分割块
 */
data class ModuleSpaceModel(
    val height: Int = 8.dp(),
    val tag: Any? = null,
    val start: Int = 0,
    val end: Int = 0
) : IEmptyDivider

/**
 * 自定义颜色的分割块
 */
data class ModuleSeparatorBarModel(
    val height: Int = 8.dp(),
    val tag: Any? = null,
    val color: Int = 0
) : IEmptyDivider

/**
 * 自定义图片分割快
 */
data class ModuleSeparatorBarWithImageModel(
    val height: Int = 8.dp(),
    val tag: Any? = null,
    val color: Int = 0,
    val backgroundImg: String = ""
) : IEmptyDivider

/**
 * 空白分割块
 */
data class ModuleEmptyModel(
    val height: Int = 8.dp(),
    val tag: Any? = null
) : IEmptyDivider

/**
 * 空白块
 */
data class ModuleEmptyContentModel(
    val imageRes: Int = 0,
    val hint: String? = null,
    val buttonText: String? = null,
    val paddingTop: Int = 100.dp(),
    val paddingBottom: Int = 28.dp(),
    val matchParent: Boolean = false,
    @ColorInt
    val backgroundColor: Int = Color.TRANSPARENT,
    val buttonClick: (() -> Unit)? = null
)

data class ModuleLoadingContentModel(
    val paddingTop: Int = 100.dp(),
    val paddingBottom: Int = 28.dp(),
    val matchParent: Boolean = false
)

class PlaceHolderModel(var targetType: Class<*>)

/**
 * 显示更多
 */
class ModuleNoMoreTipModel

/**
 * 分组间隔，插入到列表后，groupPosition会从0开始
 */
data class ModuleGroupSectionModel(val tag: Any = Any())

/**
 * 数组中插入数据
 */
fun IEmptyDivider.joinTo(list: List<Any>, hasStart: Boolean = false, hasEnd: Boolean = false): List<Any> {
    if (list.isEmpty()) return list
    val result = mutableListOf<Any>()
    val size = list.size
    list.forEachIndexed { index, any ->
        if (hasStart || index != 0) {
            result.add(this)
        }
        result.add(any)
        if (hasEnd && index == size - 1) {
            result.add(this)
        }
    }
    return result
}
