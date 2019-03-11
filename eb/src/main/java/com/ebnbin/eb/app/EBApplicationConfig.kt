package com.ebnbin.eb.app

import com.ebnbin.eb.debug.BaseDebugPageFragment

/**
 * Application 配置.
 */
open class EBApplicationConfig {
    /**
     * 应用 debug page 页面.
     */
    open val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = null
}
