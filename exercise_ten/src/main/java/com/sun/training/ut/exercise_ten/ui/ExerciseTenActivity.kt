package com.sun.training.ut.exercise_ten.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.exercise_ten.R
import com.example.exercise_ten.databinding.ActivityExerciseTenBinding
import com.sun.training.ut.BR
import com.sun.training.ut.exercise_ten.di.exerciseModules
import com.sun.training.ut.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ExerciseTenActivity : BaseActivity<ActivityExerciseTenBinding, ExerciseTenViewModel>() {
    override val viewModel by viewModel<ExerciseTenViewModel>()

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId = R.layout.activity_exercise_ten

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(exerciseModules)
        super.onCreate(savedInstanceState)
        viewBinding.lifecycleOwner = this

        setupUI()
        subscriberUI()
    }

    override fun onDestroy() {
        unloadKoinModules(exerciseModules)
        super.onDestroy()
    }

    private fun subscriberUI() = with(viewModel) {
        invoice.observe(this@ExerciseTenActivity, Observer {
                invoice ->
            user.value?.let { u ->
                val message = String.format(
                    getString(R.string.ex_10_invoice_information),
                    u.userName,
                    viewBinding.member.selectedItem.toString(),
                    invoice.subTotal.toFloat(),
                    invoice.discount.toFloat(),
                    invoice.total.toFloat()
                )

                AlertDialog.Builder(this@ExerciseTenActivity)
                    .setTitle(R.string.ex_10_invoice_title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .apply {
                        if (invoice.giftAccepted) {
                            setNegativeButton(getString(R.string.ex_10_invoice_gift_title)) { _, _ ->
                                showGiftMessage()
                            }
                        }
                    }
                    .create()
                    .show()
            } ?: showUserEmpty()
        })
    }

    private fun setupUI() {
        ArrayAdapter.createFromResource(
            this,
            R.array.member,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.member.adapter = adapter
        }

        viewBinding.member.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                viewModel.updateMemberClassType(resources.getStringArray(R.array.member)[pos])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        viewBinding.payment.setOnClickListener {
            viewModel.subTotal.value = viewBinding.price.text.toString()
            viewModel.printInvoice()
        }
    }

    private fun showGiftMessage() {
        AlertDialog.Builder(this)
            .setTitle(R.string.ex_10_invoice_gift_title)
            .setMessage(R.string.ex_10_invoice_gift_message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()
            .show()
    }

    private fun showUserEmpty() {}
}