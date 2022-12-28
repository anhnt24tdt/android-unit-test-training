package com.sun.training.ut.ui.exercise_three

import androidx.lifecycle.MutableLiveData
import com.sun.training.ut.data.Constant
import com.sun.training.ut.data.model.Shopping
import com.sun.training.ut.ui.base.BaseViewModel

class ExerciseThreeViewModel : BaseViewModel() {

    val discountLiveData: MutableLiveData<Int> = MutableLiveData()
    private var isHaveShirt: Boolean = false
    private var isHaveTie: Boolean = false
    var numberOfItems = 0

    fun onChangedShirt(isChecked: Boolean) {
        isHaveShirt = isChecked
    }

    fun onChangedTie(isChecked: Boolean) {
        isHaveTie = isChecked
    }

    fun calculate() {
        val shopping = Shopping(
            numberOfItems = numberOfItems,
            isWhiteShirt = isHaveShirt,
            isTie = isHaveTie
        )
        val isSevenItems = shopping.numberOfItems >= Constant.DEFAULT_ITEM_HAVE_DISCOUNT
        val discount = when {
            shopping.isTie && shopping.isWhiteShirt -> if (isSevenItems) Constant.DISCOUNT_12 else Constant.DISCOUNT_5
            isSevenItems -> Constant.DISCOUNT_7
            else -> 0
        }
        discountLiveData.postValue(discount)
    }
}