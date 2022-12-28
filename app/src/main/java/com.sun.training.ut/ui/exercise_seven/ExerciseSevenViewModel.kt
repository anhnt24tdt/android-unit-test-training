package com.sun.training.ut.ui.exercise_seven

import androidx.lifecycle.MutableLiveData
import com.sun.training.ut.ui.base.BaseViewModel

class ExerciseSevenViewModel : BaseViewModel() {
    private var isFastShipping: Boolean = false
    private var isPremium: Boolean = false
    private val feeShipNormal = 500
    private val feeFastShip = 500
    private val moneyFreeShip = 5000

    var money = 0
    val feeLiveData: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val isErrorLiveData: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    fun calculateFee() {
        when {
            money <= 0 -> calculateError()
            money < moneyFreeShip -> calculateMoney()
            else -> calculateMoney5000()
        }
    }

    private fun calculateError() {
        isErrorLiveData.value = true
        feeLiveData.value = 0
    }

    private fun calculateMoney() {
        isErrorLiveData.value = false
        if (!isPremium) {
            if (isFastShipping) feeLiveData.value = feeShipNormal + feeFastShip
            else feeLiveData.value = feeShipNormal
        } else {
            if (isFastShipping) feeLiveData.value = feeShipNormal
            else feeLiveData.value = 0
        }
    }

    private fun calculateMoney5000() {
        isErrorLiveData.value = false
        if (isFastShipping) feeLiveData.value = feeFastShip
        else feeLiveData.value = 0
    }

    fun onFastShippingChecked(isChecked: Boolean) {
        isFastShipping = isChecked
    }

    fun onPremiumChecked(isChecked: Boolean) {
        isPremium = isChecked
    }

}