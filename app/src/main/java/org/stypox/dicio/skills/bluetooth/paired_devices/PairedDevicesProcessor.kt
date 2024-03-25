package org.stypox.dicio.skills.bluetooth.paired_devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.dicio.skill.chain.IntermediateProcessor
import org.dicio.skill.standard.StandardResult
import org.stypox.dicio.skills.bluetooth.paired_devices.PairedDevicesOutput.Data

class PairedDevicesProcessor : IntermediateProcessor<StandardResult, Data?>() {
    override fun process(data: StandardResult?): Data {
        val bluetoothAdapter: BluetoothAdapter? =
            (ctx().android().getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
        return if (bluetoothAdapter == null || ActivityCompat.checkSelfPermission(
                ctx().android(), Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Data(false, null) // Device doesn't support Bluetooth or permisssions not granted
        } else {
            Data(true, bluetoothAdapter.bondedDevices.map { it.name })
        }
    }
}