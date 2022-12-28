package com.sun.training.ut.ui.exercise_nine

import androidx.lifecycle.MutableLiveData
import com.sun.training.ut.data.model.No9Input
import com.sun.training.ut.data.model.No9Result
import com.sun.training.ut.ui.base.BaseViewModel

class ExerciseNineViewModel : BaseViewModel() {

    private var hasMagicWand: Boolean = false
    private var hasMaster: Boolean = false
    private var hasKey: Boolean = false
    private var hasLightSword: Boolean = false

    val resultBeatBoss: MutableLiveData<No9Result> = MutableLiveData()

    /**
     * Điều kiện tiên quyết：
    ①：Để tìm được căn phòng nơi có Big Boss, cần mang theo công cụ là "Đũa phép" hoặc là cần có "Quân sư" làm bạn đồng hành
    ②：Khi vào trong phòng có Big Boss , cần mang theo công cụ là "Chìa khoá bóng đêm".
    ③：Để đánh bại Big Boss, dũng sĩ Bánh Mì cần phải mang theo vũ khí là "Kiếm ánh sáng"
     * Check result for exercise 9
     */

    fun checkResultExerciseNine() {
        val result = checkResultExerciseNine(
            No9Input(
                magicWand = hasMagicWand,
                master = hasMaster,
                key = hasKey,
                lightSword = hasLightSword
            )
        )
        resultBeatBoss.postValue(result)
    }

    private fun checkResultExerciseNine(input: No9Input): No9Result {
        val no9Result = No9Result(findRoom = false)
        if (input.magicWand || input.master) {
            no9Result.findRoom = true
            if (input.key == true) {
                no9Result.inputRoom = true
                if (input.lightSword == true) {
                    no9Result.beatBoss = true
                }
            }
        }

        return no9Result
    }

    fun onMagicWandChecked(isChecked: Boolean) {
        hasMagicWand = isChecked
    }

    fun onMasterChecked(isChecked: Boolean) {
        hasMaster = isChecked
    }

    fun onKeyChecked(isChecked: Boolean) {
        hasKey = isChecked
    }

    fun onLightSwordChecked(isChecked: Boolean) {
        hasLightSword = isChecked
    }
}
