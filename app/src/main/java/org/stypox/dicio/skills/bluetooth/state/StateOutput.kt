package org.stypox.dicio.skills.bluetooth.state

import org.dicio.skill.chain.OutputGenerator
import org.stypox.dicio.R
import org.stypox.dicio.output.graphical.GraphicalOutputUtils

class StateOutput : OutputGenerator<StateOutput.Data?>() {
    enum class State { ON, OFF, UNCHANGED }

    class Data(val state: State, val success: Boolean)

    override fun generate(data: Data?) {
        val message = if (data == null || !data.success) ctx().android().getString(
            R.string.skill_calculator_could_not_calculate
        )
        else when (data.state) {
            State.ON -> "I enabled bluetooth"
            State.OFF -> "I disabled bluetooth"
            State.UNCHANGED -> "I could not change the bluetooth state"
        }
        ctx().speechOutputDevice.speak(message)
        ctx().graphicalOutputDevice.display(
            GraphicalOutputUtils.buildHeader(
                ctx().android(), message
            )
        )
    }
}
