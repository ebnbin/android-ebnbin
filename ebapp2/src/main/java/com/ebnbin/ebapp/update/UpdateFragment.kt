package com.ebnbin.ebapp.update

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ebnbin.eb.coroutine.Loading
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.eb.dialog.closeLoadingDialogFragment
import com.ebnbin.eb.dialog.openLoadingDialogFragment
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.ebapp.api.GitHubApi
import kotlinx.coroutines.Job

class UpdateFragment : Fragment() {
    private val viewModel: UpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            requestUpdate()
        }
    }

    private fun requestUpdate() {
        val loading = when (requireArgument<Type>(KEY_TYPE)) {
            Type.MANUAL -> {
                object : Loading<GitHubApi.Update> {
                    override fun onStart(job: Job) {
                        super.onStart(job)
                        childFragmentManager.openLoadingDialogFragment(
                            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
                            fragmentTag = "loading"
                        )
                    }

                    override fun onSuccess(result: GitHubApi.Update) {
                        super.onSuccess(result)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }

                    override fun onFailure(throwable: Throwable) {
                        super.onFailure(throwable)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }
                }
            }
            Type.SILENT -> {
                object : Loading<GitHubApi.Update> {
                    override fun onStart(job: Job) {
                        super.onStart(job)
                        childFragmentManager.openLoadingDialogFragment(
                            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
                            fragmentTag = "loading"
                        )
                    }

                    override fun onSuccess(result: GitHubApi.Update) {
                        super.onSuccess(result)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }

                    override fun onFailure(throwable: Throwable) {
                        super.onFailure(throwable)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }
                }
            }
            Type.ALWAYS -> {
                object : Loading<GitHubApi.Update> {
                    override fun onStart(job: Job) {
                        super.onStart(job)
                        childFragmentManager.openLoadingDialogFragment(
                            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
                            fragmentTag = "loading"
                        )
                    }

                    override fun onSuccess(result: GitHubApi.Update) {
                        super.onSuccess(result)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }

                    override fun onFailure(throwable: Throwable) {
                        super.onFailure(throwable)
                        childFragmentManager.closeLoadingDialogFragment("loading")
                    }
                }
            }
        }
        viewModel.requestUpdate(loading)
    }

    enum class Type {
        /**
         * 手动检查更新.
         */
        MANUAL,
        /**
         * 静默更新.
         */
        SILENT,
        /**
         * 始终唤起更新.
         */
        ALWAYS
    }

    companion object {
        private const val KEY_TYPE = "type"

        fun createArguments(
            type: Type = Type.MANUAL
        ): Bundle {
            return bundleOf(
                KEY_TYPE to type
            )
        }
    }
}
