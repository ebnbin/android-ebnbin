//package com.ebnbin.eb2.debug
//
//import com.ebnbin.eb.app2.dev.DevFragment2
//import com.ebnbin.eb.fragment.openFragment
//import com.ebnbin.eb2.about.AboutFragment
//import com.ebnbin.eb2.update.UpdateFragment
//
///**
// * Debug EB 页面.
// */
//open class EBDebugPageFragment : DevFragment2() {
//    override fun onAddDevItems() {
//        super.onAddDevItems()
//
//        addDevItem("About") {
//            openFragment<AboutFragment>()
//        }
//
//        addDevItem("异步任务", "5 秒后完成，可按返回键取消") {
////            asyncHelper.task(
////                { Thread.sleep(5000L) },
////                DialogLoading(requireContext(), DialogCancel.NOT_CANCELED_ON_TOUCH_OUTSIDE),
////                onSuccess = {
////                    AppHelper.toast(requireContext(), "onSuccess")
////                },
////                onFailure = {
////                    AppHelper.toast(requireContext(), "onFailure")
////                }
////            )
//        }
//
//        addDevItem("更新") {
//            UpdateFragment.start(childFragmentManager, false)
//        }
//
//        addDevItem("重置偏好", "TODO")
//    }
//}
