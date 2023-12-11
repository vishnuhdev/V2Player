package com.player.v2player.presentation.utils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
object PermissionHandler {
    // Declare the launcher outside the requestPermission function
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var permissionResultCallback: (Boolean) -> Unit

    // Initialize the launcher in the Application class or in the activity's onCreate method
    fun initialize(activity: ComponentActivity) {
        requestPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean -> permissionResultCallback(isGranted)
            }
    }

    fun requestPermission(
        activity: ComponentActivity,
        permission: String,
        onPermissionResult: (Boolean) -> Unit
    ) {
        permissionResultCallback= onPermissionResult
        if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it.
            requestPermissionLauncher.launch(permission)
        } else {
            // Permission is already granted.
            permissionResultCallback(true)
        }
    }
}