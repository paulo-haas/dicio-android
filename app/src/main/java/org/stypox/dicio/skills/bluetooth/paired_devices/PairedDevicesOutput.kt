package org.stypox.dicio.skills.bluetooth.paired_devices

import org.dicio.skill.chain.OutputGenerator
import org.stypox.dicio.R
import org.stypox.dicio.output.graphical.GraphicalOutputUtils

class PairedDevicesOutput : OutputGenerator<PairedDevicesOutput.Data?>() {
    class Data(val success: Boolean, val pairedDevices: List<String>?)

    override fun generate(data: Data?) {
        val message = if (data == null || !data.success || data.pairedDevices == null) ctx().android().getString(
            R.string.skill_calculator_could_not_calculate
        )
        else "You are paired with these devices: ${data.pairedDevices.joinToString(", ")}"
        ctx().speechOutputDevice.speak(message)
        ctx().graphicalOutputDevice.display(
            GraphicalOutputUtils.buildHeader(
                ctx().android(), message
            )
        )
    }
}
