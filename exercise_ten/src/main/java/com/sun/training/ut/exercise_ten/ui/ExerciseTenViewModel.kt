package com.sun.training.ut.exercise_ten.ui

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.exercise_ten.R
import com.sun.training.ut.exercise_ten.data.model.Invoice
import com.sun.training.ut.exercise_ten.data.model.MemberClassType
import com.sun.training.ut.exercise_ten.data.model.User
import com.sun.training.ut.exercise_ten.domain.business.DiscountBusiness
import com.sun.training.ut.exercise_ten.domain.business.GiftBusiness
import com.sun.training.ut.exercise_ten.domain.business.PaymentAmountPointBusiness
import com.sun.training.ut.exercise_ten.util.SingleLiveData
import com.sun.training.ut.ui.base.BaseViewModel

class ExerciseTenViewModel constructor(private val resources: Resources) : BaseViewModel() {
    val user = MutableLiveData<User>()

    val invoice = SingleLiveData<Invoice>()
    val subTotal = MutableLiveData<String>()

    init {
        user.value = User(
            userId = 1,
            userName = resources.getString(R.string.ex_10_user_name_default),
            classType = MemberClassType.GOLD_CLASS
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun discountCalculation(subTotal: Double): Double {
        return user.value?.let { currentUser ->
            when {
                currentUser.classType == MemberClassType.BLACK_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_10K -> subTotal * DiscountBusiness.BLACK_CLASS_MIN_10K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.BLACK_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_5K -> subTotal * DiscountBusiness.BLACK_CLASS_MIN_5K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.BLACK_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_3K -> subTotal * DiscountBusiness.BLACK_CLASS_MIN_3K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.GOLD_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_10K -> subTotal * DiscountBusiness.GOLD_CLASS_MIN_10K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.GOLD_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_5K -> subTotal * DiscountBusiness.GOLD_CLASS_MIN_5K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.GOLD_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_3K -> subTotal * DiscountBusiness.GOLD_CLASS_MIN_3K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.SILVER_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_10K -> subTotal * DiscountBusiness.SILVER_CLASS_MIN_10K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.SILVER_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_5K -> subTotal * DiscountBusiness.SILVER_CLASS_MIN_5K_DISCOUNT_PERCENT
                currentUser.classType == MemberClassType.SILVER_CLASS && subTotal >= PaymentAmountPointBusiness.PAYMENT_3K -> subTotal * DiscountBusiness.SILVER_CLASS_MIN_3K_DISCOUNT_PERCENT
                else -> subTotal * DiscountBusiness.UNKNOWN_CLASS_DISCOUNT_PERCENT
            }
        } ?: 0.0
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun giftAccepted(subTotal: Double): Boolean =
        subTotal in GiftBusiness.GIFT_ACCEPTED_WITH_PAYMENT_EQUALS

    fun printInvoice() {
        val amount = subTotal.value?.toDoubleOrNull() ?: 0.0
        val discount = discountCalculation(amount)

        invoice.value = Invoice(
            invoiceId = 1,
            subTotal = amount,
            discount = discount,
            giftAccepted = giftAccepted(amount),
            total = amount - discount
        )
    }

    fun updateMemberClassType(type: String) {
        val memberType = when (type) {
            resources.getString(R.string.ex_10_class_type_black) -> MemberClassType.BLACK_CLASS
            resources.getString(R.string.ex_10_class_type_gold) -> MemberClassType.GOLD_CLASS
            resources.getString(R.string.ex_10_class_type_silver) -> MemberClassType.SILVER_CLASS
            else -> MemberClassType.UNKNOWN_CLASS
        }
        user.value?.let { user ->
            user.classType = memberType
        }
    }
}