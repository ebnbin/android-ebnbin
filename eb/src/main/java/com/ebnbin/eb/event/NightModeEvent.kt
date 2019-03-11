package com.ebnbin.eb.event

/**
 * 夜间模式设置变化事件. [oldNightMode] 和 [newNightMode] 一定不相同.
 */
class NightModeEvent(val oldNightMode: Int, val newNightMode: Int)
