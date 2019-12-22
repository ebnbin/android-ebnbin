package com.ebnbin.eb.dialog

/**
 * 对话框关闭类型.
 */
enum class DialogCancelable {
    /**
     * 可通过按返回键或点击空白区域关闭.
     */
    CANCELABLE,
    /**
     * 可通过按返回键关闭, 不可通过点击空白区域关闭.
     */
    NOT_CANCELABLE_ON_TOUCH_OUTSIDE,
    /**
     * 不可通过按返回键和点击空白区域关闭.
     */
    NOT_CANCELABLE
}
