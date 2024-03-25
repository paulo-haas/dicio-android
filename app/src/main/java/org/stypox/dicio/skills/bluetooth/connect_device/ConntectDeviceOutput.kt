package org.stypox.dicio.skills.bluetooth.connect_device

import org.dicio.skill.chain.OutputGenerator
import org.stypox.dicio.R
import org.stypox.dicio.output.graphical.GraphicalOutputUtils

class ConnectDeviceOutput : OutputGenerator<ConnectDeviceOutput.Data?>() {
    class Data(val success: Boolean, val device: String?)

    override fun generate(data: Data?) {
        val message =
            if (data == null || !data.success || data.device == null) ctx().android().getString(
                R.string.skill_calculator_could_not_calculate
            )
            else "Connected successfully to ${data.device}"
        ctx().speechOutputDevice.speak(message)
        ctx().graphicalOutputDevice.display(
            GraphicalOutputUtils.buildHeader(
                ctx().android(), message
            )
        )
    }
}
