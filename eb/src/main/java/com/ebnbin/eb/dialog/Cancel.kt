package com.ebnbin.eb.dialog

/**
 * 对话框关闭类型.
 */
enum class Cancel {
    /**
     * 可通过按返回键或点击空白处关闭.
     */
    CANCELABLE,
    /**
     * 只能通过返回键关闭, 不能通过点击空白处关闭.
     */
    NOT_CANCELED_ON_TOUCH_OUTSIDE,
    /**
     * 不能通过按返回键或点击空白处关闭.
     */
    NOT_CANCELABLE
}
