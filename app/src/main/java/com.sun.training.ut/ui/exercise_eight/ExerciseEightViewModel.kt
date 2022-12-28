package com.sun.training.ut.ui.exercise_eight

import androidx.lifecycle.MutableLiveData
import com.sun.training.ut.data.Constant
import com.sun.training.ut.data.model.No8Member
import com.sun.training.ut.ui.base.BaseViewModel

class ExerciseEightViewModel : BaseViewModel() {

    val member = object : MutableLiveData<No8Member>() {
        override fun setValue(value: No8Member?) {
            super.setValue(value)
            calculateBadmintonFee()
        }
    }

    var dayOfWeek: Constant.DayOfWeek? = null
        set(value) {
            field = value
            calculateBadmintonFee()
        }

    val fee = MutableLiveData<Int>()

    fun validateMemberAge(member: No8Member?): Boolean {
        return (member?.age in Constant.BADMINTON_MIN_AGE..Constant.BADMINTON_MAX_AGE)
    }

    fun calculateBadmintonFee() {
        if (dayOfWeek == null) return
        val validate = validateMemberAge(member.value)
        member.value?.takeIf { validate }?. apply {
            fee.value = when {
                dayOfWeek == Constant.DayOfWeek.TUESDAY -> Constant.BADMINTON_FEE_1200
                dayOfWeek == Constant.DayOfWeek.FRIDAY -> {
                    when {
                        age <= 13 -> Constant.BASE_BADMINTON_FEE / 2
                        else -> {
                            if (gender == Constant.Gender.FEMALE)
                                Constant.BADMINTON_FEE_1400
                            else {
                                if (age < 66)
                                    Constant.BASE_BADMINTON_FEE
                                else
                                    Constant.BADMINTON_FEE_1600
                            }
                        }
                    }
                }
                else -> {
                    when {
                        age <= 13 -> Constant.BASE_BADMINTON_FEE / 2
                        age < 66 -> Constant.BASE_BADMINTON_FEE
                        else -> Constant.BADMINTON_FEE_1600
                    }
                }
            }
        }
    }

    fun genderChangedMale(isChecked: Boolean) {
        if (isChecked) {
            member.value = No8Member(age = member.value?.age ?: 0, gender = Constant.Gender.MALE)
        }
    }

    fun genderChangedFemale(isChecked: Boolean) {
        if (isChecked)
            member.value = No8Member(
                age = member.value?.age ?: 0, gender = Constant.Gender.FEMALE
            )
    }

    fun ageChanged(newVal: Int) {
        var gender =  Constant.Gender.MALE
        member.value?.apply {
            gender = this.gender
        }
        member.value = No8Member(
            age = newVal, gender = gender
        )
    }
}