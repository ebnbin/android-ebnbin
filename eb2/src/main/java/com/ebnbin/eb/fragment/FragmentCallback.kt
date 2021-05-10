package com.ebnbin.eb.fragment

/**
 * 按照什么顺序强转 Fragment 的 Callback 接口.
 */
enum class FragmentCallback {
    /**
     * 优先尝试强转 parentFragment, 其次尝试强转 activity.
     */
    PREFER_PARENT_FRAGMENT,
    /**
     * 优先尝试强转 activity, 其次尝试强转 parentFragment.
     */
    PREFER_ACTIVITY,
    /**
     * 仅尝试强转 parentFragment.
     */
    PARENT_FRAGMENT,
    /**
     * 仅尝试强转 activity.
     */
    ACTIVITY
}
