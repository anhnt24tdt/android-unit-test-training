package com.sun.training.ut.exercise_ten.ui

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.exercise_ten.R
import com.sun.training.ut.exercise_ten.*
import com.sun.training.ut.exercise_ten.data.model.Invoice
import com.sun.training.ut.exercise_ten.data.model.MemberClassType
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class ExerciseTenViewModelUnitTest {
    private lateinit var tenViewModel: ExerciseTenViewModel

    @Mock
    private lateinit var resource: Resources

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(resource.getString(R.string.ex_10_user_name_default))
            .thenReturn("Bach Ngoc Hoai")
        `when`(resource.getString(R.string.ex_10_class_type_black))
            .thenReturn("Hạng Đen")
        `when`(resource.getString(R.string.ex_10_class_type_gold))
            .thenReturn("Hạng Vàng")
        `when`(resource.getString(R.string.ex_10_class_type_silver))
            .thenReturn("Hạng Bạc")
        tenViewModel = ExerciseTenViewModel(resource)
    }

    @Test
    fun updateMemberClassType_withTypeBlack_willReceive_BackClass() {
        tenViewModel.updateMemberClassType("Hạng Đen")
        assertEquals(tenViewModel.user.value?.classType, MemberClassType.BLACK_CLASS)
    }

    @Test
    fun updateMemberClassType_withTypeGold_willReceive_GoldClass() {
        tenViewModel.updateMemberClassType("Hạng Vàng")
        assertEquals(tenViewModel.user.value?.classType, MemberClassType.GOLD_CLASS)
    }

    @Test
    fun updateMemberClassType_withTypeSilver_willReceive_SilverClass() {
        tenViewModel.updateMemberClassType("Hạng Bạc")
        assertEquals(tenViewModel.user.value?.classType, MemberClassType.SILVER_CLASS)
    }

    @Test
    fun updateMemberClassType_withTypeEmpty_willReceive_Unknown() {
        tenViewModel.updateMemberClassType("")
        assertEquals(tenViewModel.user.value?.classType, MemberClassType.UNKNOWN_CLASS)
    }

    @Test
    fun updateMemberClassType_withUserNull_willReceive_Null() {
        tenViewModel.user.value = null
        tenViewModel.updateMemberClassType("")
        assertEquals(tenViewModel.user.value?.classType, null)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentLessThan3K_willReceive_0() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = 2900.0

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserValueNull_andPaymentLessThan3K_willReceive_0() {
        val paymentAmount = 2900.0

        tenViewModel.subTotal.value = paymentAmount.toString()
        tenViewModel.user.value = null
        assertEquals(tenViewModel.discountCalculation(paymentAmount), 0.0)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentEquals3K_willReceive_1Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = 3000.0

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.01)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentFrom3KTo5K_willReceive_1Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = Random.nextDouble(3001.0, 5000.0)

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.01)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentEquals5K_willReceive_2Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = 5000.0

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.02)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentFrom5KTo10K_willReceive_2Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = Random.nextDouble(5001.0, 10000.0)

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.02)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentEquals10K_willReceive_4Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = 10000.0

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.04)
    }

    @Test
    fun discountCalculate_withUserSilverClass_andPaymentGreatThan10K_willReceive_4Percent() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = 10001.0 + Random.nextDouble()

        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.04)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentLessThan3K_willReceive_0() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = 2900.0

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentEquals3K_willReceive_3Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = 3000.0

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.03)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentFrom3KTo5K_willReceive_3Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = Random.nextDouble(3001.0, 5000.0)

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.03)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentEquals5K_willReceive_5Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = 5000.0

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.05)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentFrom5KTo10K_willReceive_5Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = Random.nextDouble(5001.0, 10000.0)

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.05)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentEquals10K_willReceive_10Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = 10000.0

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.10)
    }

    @Test
    fun discountCalculate_withUserGoldClass_andPaymentGreatThan10K_willReceive_10Percent() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = 10001.0 + Random.nextDouble()

        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.10)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentLessThan3K_willReceive_0() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = 2900.0

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentEquals3K_willReceive_5Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = 3000.0

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.05)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentFrom3KTo5K_willReceive_5Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = Random.nextDouble(3001.0, 5000.0)

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.05)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentEquals5K_willReceive_7Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = 5000.0

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.07)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentFrom5KTo10K_willReceive_7Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = Random.nextDouble(5001.0, 10000.0)

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.07)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentEquals10K_willReceive_15Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = 10000.0

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.15)
    }

    @Test
    fun discountCalculate_withUserBlackClass_andPaymentGreatThan10K_willReceive_15Percent() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = 10001.0 + Random.nextDouble()

        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.15)
    }

    @Test
    fun discountCalculate_withUserAnonymous_andPaymentEquals3K_willReceive_0() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = 3000.0

        tenViewModel.user.value = userAnonymous
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserAnonymous_andPaymentEquals5K_willReceive_0() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = 5000.0

        tenViewModel.user.value = userAnonymous
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserAnonymous_andPaymentEquals10K_willReceive_0() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = 10000.0

        tenViewModel.user.value = userAnonymous
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun discountCalculate_withUserAnonymous_andPaymentAny_willReceive_0() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = Random.nextDouble()

        tenViewModel.user.value = userAnonymous
        tenViewModel.subTotal.value = paymentAmount.toString()

        assertEquals(tenViewModel.discountCalculation(paymentAmount), paymentAmount * 0.0)
    }

    @Test
    fun giftChecking_withUserAnonymous_andPaymentEquals5K_willReturnTrue() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = 5000.0

        tenViewModel.user.value = userAnonymous
        assertEquals(tenViewModel.giftAccepted(paymentAmount), true)
    }

    @Test
    fun giftChecking_withUserAnonymous_andPaymentEquals10K_willReturnTrue() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = 10000.0

        tenViewModel.user.value = userAnonymous
        assertEquals(tenViewModel.giftAccepted(paymentAmount), true)
    }

    @Test
    fun giftChecking_withUserAnonymous_andPaymentNotEquals5KOr10K_willReturnFalse() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)
        val accept = paymentAmount == 5000.0 || paymentAmount == 10000.0

        tenViewModel.user.value = userAnonymous
        assertEquals(tenViewModel.giftAccepted(paymentAmount), accept)
    }

    @Test
    fun checkInvoice_withUserSilver_andPaymentAny_willReturnInformation() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = paymentAmount.toString()

        val currentDiscount = tenViewModel.discountCalculation(paymentAmount)
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)
        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, paymentAmount)
            assertEquals(discount, currentDiscount)
            assertEquals(total, paymentAmount - currentDiscount)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }

    @Test
    fun checkInvoice_withUserSilver_andPaymentAny_totalNull_willReturnInformation() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userSilver
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)
        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, 0.0)
            assertEquals(discount, 0.0)
            assertEquals(total, 0.0)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }

    @Test
    fun checkInvoice_withUserSilver_andPaymentAny_totalInvalid_willReturnInformation() {
        val userSilver = createUserTypeSilverClass()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userSilver
        tenViewModel.subTotal.value = "abc"
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)
        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, 0.0)
            assertEquals(discount, 0.0)
            assertEquals(total, 0.0)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }

    @Test
    fun checkInvoice_withUserGold_andPaymentAny_willReturnInformation() {
        val userGold = createUserTypeGoldClass()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userGold
        tenViewModel.subTotal.value = paymentAmount.toString()

        val currentDiscount = tenViewModel.discountCalculation(paymentAmount)
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)

        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, paymentAmount)
            assertEquals(discount, currentDiscount)
            assertEquals(total, paymentAmount - currentDiscount)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }

    @Test
    fun checkInvoice_withUserBlack_andPaymentAny_willReturnInformation() {
        val userBlack = createUserTypeBlackClass()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userBlack
        tenViewModel.subTotal.value = paymentAmount.toString()

        val currentDiscount = tenViewModel.discountCalculation(paymentAmount)
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)

        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, paymentAmount)
            assertEquals(discount, currentDiscount)
            assertEquals(total, paymentAmount - currentDiscount)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }

    @Test
    fun checkInvoice_withUserAnonymous_andPaymentAny_willReturnInformation() {
        val userAnonymous = createUserTypeUnknown()
        val paymentAmount = Random.nextDouble(Double.MAX_VALUE)

        val observer = mock<Observer<Invoice>>()
        tenViewModel.invoice.observeForever(observer)
        tenViewModel.user.value = userAnonymous
        tenViewModel.subTotal.value = paymentAmount.toString()

        val currentDiscount = tenViewModel.discountCalculation(paymentAmount)
        val currentGiftAccepted = tenViewModel.giftAccepted(paymentAmount)

        tenViewModel.printInvoice()
        tenViewModel.invoice.value?.run {
            assertEquals(subTotal, paymentAmount)
            assertEquals(discount, currentDiscount)
            assertEquals(total, paymentAmount - currentDiscount)
            assertEquals(giftAccepted, currentGiftAccepted)
        }
    }
}
