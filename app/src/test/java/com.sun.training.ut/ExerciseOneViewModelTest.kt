package com.sun.training.ut

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sun.training.ut.data.Constant
import com.sun.training.ut.data.model.Beer
import com.sun.training.ut.ui.exercise_one.ExerciseOneViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.jvm.Throws


@RunWith(MockitoJUnitRunner::class)
class ExerciseOneViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExerciseOneViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ExerciseOneViewModel()
    }

    /**
    No.	                        1	2	3
    voucher	                    Y	N	N
    Đang trong thời gian ưu đãi	-	Y	N
    100円/yên	                X	-	-
    290円/yên	                -	X	-
    490円/yên	                -	-	X
     **/

    @Test
    fun validatePrice_withVoucher_noTimeDiscount_numberBeerIs5_returnPriceIs500() {
        viewModel.onVoucherChecked(true)
        viewModel.onTimeCouponChecked(false)
        viewModel.numberBeer = 5
        viewModel.calculatePrice()
        assertEquals(viewModel.numberBeer * Constant.VOUCHER_PRICE, viewModel.priceLiveData.value)
    }

    @Test
    fun validatePrice_withVoucher_withTimeDiscount_numberBeerIs5_returnPriceIs500() {
        viewModel.onVoucherChecked(true)
        viewModel.onTimeCouponChecked(true)
        viewModel.numberBeer = 5
        viewModel.calculatePrice()
        assertEquals(viewModel.numberBeer * Constant.VOUCHER_PRICE, viewModel.priceLiveData.value)
    }

    @Test
    fun validatePrice_noVoucher_onTimeDiscount_numberBeerIs10_returnPriceIs2900() {
        viewModel.onVoucherChecked(false)
        viewModel.onTimeCouponChecked(true)
        viewModel.numberBeer = 10
        viewModel.calculatePrice()
        assertEquals(viewModel.numberBeer * Constant.TIME_PRICE, viewModel.priceLiveData.value)
    }

    @Test
    fun validatePrice_noVoucher_NoTimeDiscount_numberBeerIs7_returnPriceIs3430() {
        viewModel.onVoucherChecked(false)
        viewModel.onTimeCouponChecked(false)
        viewModel.numberBeer = 7
        viewModel.calculatePrice()
        assertEquals(viewModel.numberBeer * Constant.REGULAR_PRICE, viewModel.priceLiveData.value)
    }
}