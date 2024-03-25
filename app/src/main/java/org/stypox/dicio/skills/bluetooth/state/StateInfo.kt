package org.stypox.dicio.skills.bluetooth.state

import android.Manifest
import androidx.fragment.app.Fragment
import org.dicio.skill.Skill
import org.dicio.skill.SkillContext
import org.dicio.skill.SkillInfo
import org.dicio.skill.chain.ChainSkill
import org.dicio.skill.standard.StandardRecognizer
import org.stypox.dicio.R
import org.stypox.dicio.Sections
import org.stypox.dicio.SectionsGenerated

class StateInfo : SkillInfo(
    "bluetooth_state",
    R.string.skill_name_bluetooth_state,
    R.string.skill_sentence_example_bluetooth_state,
    R.drawable.ic_bluetooth_white,
    false
) {
    override fun isAvailable(context: SkillContext): Boolean =
        Sections.isSectionAvailable(SectionsGenerated.bluetooth_state)

    override fun build(context: SkillContext?): Skill = ChainSkill.Builder()
        .recognize(StandardRecognizer(Sections.getSection(SectionsGenerated.bluetooth_state)))
        .process(StateProcessor())
        .output(StateOutput())

    override fun getPreferenceFragment(): Fragment? = null

    override fun getNeededPermissions(): List<String> = listOf(Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_CONNECT)
}