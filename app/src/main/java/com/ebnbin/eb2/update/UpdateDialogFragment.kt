package com.ebnbin.eb2.update

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.applicationId
import com.ebnbin.eb.closeApp
import com.ebnbin.eb.copy
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb.mainHandler
import com.ebnbin.eb.md5ToString
import com.ebnbin.eb.openBrowser
import com.ebnbin.eb.openMarket
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.permission.openPermissionFragment
import com.ebnbin.eb.requireSystemService
import com.ebnbin.eb.sdk24N
import com.ebnbin.eb.toast
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.eb2.githubapi.model.content.Update
import com.ebnbin.ebapp.isGoogleFlavor
import com.ebnbin.windowcamera.R
import java.io.File
import kotlin.math.roundToInt

internal class UpdateDialogFragment : AppCompatDialogFragment(), PermissionFragment.Callback {
    private val update: Update
        get() = requireArgument("update")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val hasForceUpdate = update.hasForceUpdate()
        val titleStringId = if (hasForceUpdate) R.string.eb_update_title_force else R.string.eb_update_title
        val negativeStringId = if (hasForceUpdate) R.string.eb_update_close else R.string.eb_update_ignore
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(titleStringId)
            .setPositiveButton(R.string.eb_update_download, null)
            .setNegativeButton(negativeStringId, null)
            .setView(R.layout.eb_update_dialog_fragment)
        if (isGoogleFlavor) {
            builder.setNeutralButton(null, null)
        } else {
            builder.setNeutralButton(R.string.eb_update_browser, null)
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val alertDialog = it as AlertDialog
            if (isGoogleFlavor) {
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                if (positiveButton != null) {
                    positiveButton.setText(R.string.eb_update_market)
                    positiveButton.setOnClickListener {
                        requireContext().openMarket(isGoogleFlavor)
                    }
                }
            } else {
                invalidatePositiveButton()
            }
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                if (hasForceUpdate) {
                    requireActivity().closeApp()
                } else {
                    dismissAllowingStateLoss()
                }
            }
            if (!isGoogleFlavor) {
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener {
                    requireContext().openBrowser(update.url)
                    requireContext().copy(update.url)
                    requireContext().toast(R.string.eb_update_copied)
                }
            }
            alertDialog.findViewById<TextView>(R.id.eb_message_text_view)?.text = update.message
        }
        return dialog
    }

    //*****************************************************************************************************************

    private var downloadId: Long? = null

    private var status: Int? = null

    //*****************************************************************************************************************

    private val updateContentObserver: ContentObserver = object : ContentObserver(mainHandler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val downloadId = downloadId ?: return
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = EBApplication.instance.requireSystemService<DownloadManager>().query(query) ?: return
            if (!cursor.moveToFirst()) return

            fun onDownloadComplete(failedStringId: Int) {
                if (invalidatePositiveButton()) {
                    requestInstallPermission()
                } else {
                    status = null
                    requireContext().toast(failedStringId)
                }
                removeDownload()
                setIsDownloading(false)
            }

            status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_PENDING -> {
                    // Do nothing.
                }
                DownloadManager.STATUS_RUNNING -> {
                    val totalSizeBytes =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    val bytesDownloadedSoFar =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    setDownloadProgress(totalSizeBytes, bytesDownloadedSoFar)
                }
                DownloadManager.STATUS_PAUSED -> {
                    onDownloadComplete(R.string.eb_update_download_complete_pause)
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    onDownloadComplete(R.string.eb_update_download_complete_successful)
                }
                DownloadManager.STATUS_FAILED -> {
                    onDownloadComplete(R.string.eb_update_download_complete_failed)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireContext().contentResolver.registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,
            updateContentObserver)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        requireContext().contentResolver.unregisterContentObserver(updateContentObserver)
        super.onDestroyView()
    }

    //*****************************************************************************************************************

    private fun download() {
        setIsDownloading(true)
        setDownloadProgress(-1, -1)
        val file = getFile()
        if (file.exists()) {
            file.delete()
        }
        val request = DownloadManager.Request(Uri.parse(update.url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setVisibleInDownloadsUi(false)
            .setDestinationInExternalFilesDir(requireContext(), null, FILE_NAME)
            .setMimeType(MINE_TYPE)
        downloadId = EBApplication.instance.requireSystemService<DownloadManager>().enqueue(request)
    }

    private fun removeDownload() {
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            downloadId = null
        } else {
            downloadId?.also { EBApplication.instance.requireSystemService<DownloadManager>().remove(it) }
            downloadId = null
            status = null
        }
    }

    //*****************************************************************************************************************

    private fun requestInstallPermission() {
        childFragmentManager.openPermissionFragment(arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES))
    }

    override fun onPermissionResult(
        context: Context,
        result: PermissionFragment.Result,
        deniedPermission: String?,
        callbackBundle: Bundle
    ): CharSequence? {
        if (result == PermissionFragment.Result.GRANTED) {
            install()
        }
        return super.onPermissionResult(context, result, deniedPermission, callbackBundle)
    }

    private fun install() {
        if (invalidatePositiveButton()) {
            val file = getFile()
            val intent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = if (sdk24N()) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(requireContext(), "${EBApplication.instance.applicationId}.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }
            intent.setDataAndType(uri, MINE_TYPE)
            openActivity(intent)
        } else {
            requireContext().toast(R.string.eb_update_unverified)
        }
    }

    //*****************************************************************************************************************

    /**
     * @return 是否可以安装.
     */
    private fun invalidatePositiveButton(): Boolean {
        val positiveButton = (dialog as AlertDialog?)?.getButton(AlertDialog.BUTTON_POSITIVE) ?: return false
        val file = getFile()
        val canInstall = file.exists() && file.readBytes().md5ToString() == update.md5
        if (canInstall) {
            positiveButton.setText(R.string.eb_update_install)
            positiveButton.setOnClickListener {
                requestInstallPermission()
            }
        } else {
            positiveButton.setText(R.string.eb_update_download)
            positiveButton.setOnClickListener {
                download()
            }
        }
        return canInstall
    }

    private fun setIsDownloading(isDownloading: Boolean) {
        val positiveButton = (dialog as AlertDialog?)?.getButton(AlertDialog.BUTTON_POSITIVE)
        if (positiveButton != null) {
            positiveButton.isEnabled = !isDownloading
        }

        val downloadView = dialog?.findViewById<View>(R.id.eb_download_view)
        if (downloadView != null) {
            downloadView.visibility = if (isDownloading) View.VISIBLE else View.GONE
        }
    }

    private fun setDownloadProgress(totalSizeBytes: Int, bytesDownloadedSoFar: Int) {
        val progressBar = dialog?.findViewById<ProgressBar>(R.id.eb_progress_bar)
        if (progressBar != null) {
            when (totalSizeBytes) {
                -1 -> {
                    progressBar.isIndeterminate = true
                }
                0 -> {
                    progressBar.isIndeterminate = false
                    progressBar.progress = 0
                }
                else -> {
                    progressBar.isIndeterminate = false
                    progressBar.progress = (bytesDownloadedSoFar.toFloat() / totalSizeBytes * 100f).roundToInt()
                }
            }
        }

        val progressTextView =  dialog?.findViewById<TextView>(R.id.eb_progress_text_view)
        if (progressTextView != null) {
            val text = if (totalSizeBytes == -1) {
                getString(R.string.eb_update_connecting)
            } else {
                getString(R.string.eb_update_progress,
                    bytesDownloadedSoFar / 1024f / 1024f, totalSizeBytes / 1024f / 1024f)
            }
            progressTextView.text = text
        }
    }

    //*****************************************************************************************************************

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        removeDownload()
    }

    //*****************************************************************************************************************

    private fun getFile(): File {
        return File(requireContext().getExternalFilesDir(null), FILE_NAME)
    }

    //*****************************************************************************************************************

    companion object {
        private const val FILE_NAME = "update.apk"
        private const val MINE_TYPE = "application/vnd.android.package-archive"

        fun start(fm: FragmentManager, update: Update): UpdateDialogFragment {
            return FragmentHelper.add(fm, UpdateDialogFragment::class.java, arguments = bundleOf(
                "update" to update
            ))
        }
    }
}
