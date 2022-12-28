package com.sun.training.ut.ui.exercise_eight

import android.os.Bundle
import android.util.TypedValue
import android.widget.EditText
import android.widget.NumberPicker
import androidx.recyclerview.widget.GridLayoutManager
import com.sun.training.ut.BR
import com.sun.training.ut.R
import com.sun.training.ut.data.Constant
import com.sun.training.ut.data.Constant.BADMINTON_MAX_AGE
import com.sun.training.ut.data.Constant.BADMINTON_MIN_AGE
import com.sun.training.ut.data.model.No8Member
import com.sun.training.ut.databinding.ActivityExerciseEightBinding
import com.sun.training.ut.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import android.view.View


class ExerciseEightActivity : BaseActivity<ActivityExerciseEightBinding, ExerciseEightViewModel>() {

    override val viewModel: ExerciseEightViewModel by viewModel()
    override val bindingVariable = BR.viewModel
    override val layoutId = R.layout.activity_exercise_eight

    var layoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.lifecycleOwner = this

        layoutManager = GridLayoutManager(this, 3)
        val adapter = DayOfWeekAdapter({ _, position ->
            viewModel.dayOfWeek = Constant.DayOfWeek.values()[position]
        }, layoutManager as GridLayoutManager)
        viewBinding.recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = this@ExerciseEightActivity.layoutManager
            addItemDecoration(
                GridSpaceItemDecoration(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimension(R.dimen.dp_4), resources.displayMetrics
                    ).toInt(), 3
                )
            )
        }

        adapter.listItem =
            ArrayList<String>().apply { addAll(resources.getStringArray(R.array.day_of_week)) }
        adapter.notifyDataSetChanged()
        adapter.selectItem(0)

        viewBinding.pickerAge.apply {
            minValue = BADMINTON_MIN_AGE
            maxValue = BADMINTON_MAX_AGE
            value = 25

            // NOTE: workaround for a bug that rendered the selected value wrong until user scrolled, see also: https://stackoverflow.com/q/27343772/3451975
            try {
                val field = NumberPicker::class.java.getDeclaredField("mInputText")
                field.isAccessible = true
                val inputText = field.get(this) as EditText
                inputText.visibility = View.INVISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Because setting default value not invoke OnValueChangedListener
            viewModel.member.value = No8Member(
                age = value, gender = if (viewBinding.radioFemale.isChecked)
                    Constant.Gender.FEMALE else Constant.Gender.MALE
            )
        }
    }
}