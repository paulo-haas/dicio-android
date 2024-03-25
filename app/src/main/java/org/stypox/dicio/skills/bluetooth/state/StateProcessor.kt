package org.stypox.dicio.skills.bluetooth.state

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.dicio.skill.chain.IntermediateProcessor
import org.dicio.skill.standard.StandardResult
import org.stypox.dicio.skills.bluetooth.state.StateOutput.Data
import org.stypox.dicio.skills.bluetooth.state.StateOutput.State

class StateProcessor : IntermediateProcessor<StandardResult, Data?>() {
    override fun process(data: StandardResult?): Data {
        val bluetoothAdapter: BluetoothAdapter? = (ctx().android()
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
        // TODO("Add Regex to also find enabled and disabled")
        val newState = when (data?.getCapturingGroup("state")?.trim()) {
            "up", "on", "enable" -> State.ON
            "down", "off", "disable" -> State.OFF
            else -> null
        }
        return if (bluetoothAdapter == null || ActivityCompat.checkSelfPermission(
                ctx().android(), Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Data(
                State.UNCHANGED,
                false
            ) // Device doesn't support Bluetooth or permisssions not granted
        } else {
            if (!bluetoothAdapter.isEnabled && newState == State.ON) Data(
                newState,
                bluetoothAdapter.enable()
            )
            else if (bluetoothAdapter.isEnabled && newState == State.OFF) Data(
                newState,
                bluetoothAdapter.disable()
            )
            else Data(
                State.UNCHANGED,
                (!bluetoothAdapter.isEnabled && newState == State.OFF) || (bluetoothAdapter.isEnabled && newState == State.ON)
            )
        }
    }
}