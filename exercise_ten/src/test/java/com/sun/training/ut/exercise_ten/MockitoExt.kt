package com.sun.training.ut.exercise_ten

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)