package com.sun.training.ut

import com.sun.training.ut.ui.base.BaseViewModel

open class BaseTestViewModel<ViewModel : BaseViewModel> : BaseTest() {
    protected lateinit var viewModel: ViewModel
}