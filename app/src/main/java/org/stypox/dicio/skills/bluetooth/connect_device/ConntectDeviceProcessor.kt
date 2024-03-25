package org.stypox.dicio.skills.bluetooth.connect_device

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.dicio.skill.chain.IntermediateProcessor
import org.dicio.skill.standard.StandardResult
import org.stypox.dicio.skills.bluetooth.connect_device.ConnectDeviceOutput.Data
import org.stypox.dicio.util.StringUtils
import java.io.IOException
import java.util.UUID

class ConnectDeviceProcessor : IntermediateProcessor<StandardResult, Data?>() {
    override fun process(data: StandardResult?): Data {
        val bluetoothAdapter: BluetoothAdapter? = (ctx().android()
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
        val device = data?.getCapturingGroup("device")?.trim()
        val a2dpUUID = UUID.fromString("0000110A-0000-1000-8000-00805F9B34FB")
        return if (device == null || ActivityCompat.checkSelfPermission(
                ctx().android(), Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) Data(false, null)
        else {
            var success = false
            var deviceName: String? = null
            try {
                bluetoothAdapter?.bondedDevices?.minBy {
                    StringUtils.levenshteinDistance(
                        it.name, device
                    )
                }?.let {
                    deviceName = it.name
                    it.createRfcommSocketToServiceRecord(a2dpUUID).connect()
                    success = true

                }
            } catch (ioException: IOException) {
            }
            Data(success, deviceName)
        }
    }
}
