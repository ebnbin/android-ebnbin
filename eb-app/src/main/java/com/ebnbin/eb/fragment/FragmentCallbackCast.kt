package com.ebnbin.eb.fragment

/**
 * 按照什么顺序强转 Callback 接口.
 */
enum class FragmentCallbackCast {
    /**
     * 优先尝试 parentFragment, 如果失败则尝试 activity, 如果失败则强转失败.
     */
    PREFER_PARENT_FRAGMENT,
    /**
     * 优先尝试 activity, 如果失败则尝试 parentFragment, 如果失败则强转失败.
     */
    PREFER_ACTIVITY,
    /**
     * 仅尝试 parentFragment, 如果失败则强转失败.
     */
    PARENT_FRAGMENT,
    /**
     * 仅尝试 activity, 如果失败则强转失败.
     */
    ACTIVITY
}
