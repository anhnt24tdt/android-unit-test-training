package com.sun.training.ut.exercise_ten

import com.sun.training.ut.exercise_ten.data.model.MemberClassType
import com.sun.training.ut.exercise_ten.data.model.User

fun createUserTypeSilverClass(): User =
    User(userId = 1, userName = "Anonymous", classType = MemberClassType.SILVER_CLASS)

fun createUserTypeGoldClass(): User =
    User(userId = 2, userName = "Anonymous", classType = MemberClassType.GOLD_CLASS)

fun createUserTypeBlackClass(): User =
    User(userId = 3, userName = "Anonymous", classType = MemberClassType.BLACK_CLASS)

fun createUserTypeUnknown(): User =
    User(userId = 4, userName = "Anonymous", classType = MemberClassType.UNKNOWN_CLASS)
