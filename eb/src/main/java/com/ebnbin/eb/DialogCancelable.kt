package com.ebnbin.eb

/**
 * 允许关闭对话框的类型.
 */
enum class DialogCancelable {
    /**
     * 允许通过按返回键或点击空白区域关闭对话框.
     */
    CANCELABLE,
    /**
     * 允许通过按返回键关闭对话框, 不允许通过点击空白区域关闭对话框.
     */
    NOT_CANCELABLE_ON_TOUCH_OUTSIDE,
    /**
     * 不允许通过按返回键与点击空白区域关闭对话框.
     */
    NOT_CANCELABLE
}
