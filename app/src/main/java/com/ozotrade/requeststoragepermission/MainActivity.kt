package com.ozotrade.requeststoragepermission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val TAG = "PermissionCheck"
    private val STORAGE_PERMISSION_CODE = 100

    private val storagePermissionsArray = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private fun checkArrayStoragePermissions(): Boolean {
        for (permission in storagePermissionsArray) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    var permissionGrant=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkReadWritePermission()){
            permissionGrant=true
        }else{
            requestCallPermission()
        }
    }

    private fun requestCallPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
//            try {
//
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                val uri = Uri.fromParts("package", this.packageName, null)
//                intent.data = uri
//                sdkUpperActivityResultLauncher.launch(intent)
//            }
//            catch (e: Exception){
//                Log.e(TAG, "error ", e)
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                sdkUpperActivityResultLauncher.launch(intent)
//            }

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }else{
            //for below version
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private val sdkUpperActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        //here we will handle the result of our intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            if (Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                Log.d(TAG, "Manage External Storage Permission is granted")
            //    createNewFolder()
            }
            else{
                //Manage External Storage Permission is denied....
                Log.d(TAG, "Permission is denied")
                toast("Manage External Storage Permission is denied....")
            }
        }

    }

    private fun checkReadWritePermission(): Boolean{

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Permission is below 11(R)
            //  checkBelowPermissionGranted()
            checkArrayStoragePermissions()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
                Log.d(TAG, "External Storage Permission granted")
                permissionGrant=true
             //   createNewFolder()
            }
            else{
                //External Storage Permission denied...
                Log.d(TAG, "Some  Permission denied...")
                toast("Some Storage Permission denied...")
            }
        }
    }


    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}