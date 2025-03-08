package com.datn.viettech_md_12.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datn.viettech_md_12.R
import com.datn.viettech_md_12.data.model.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            delay(2000)

            _categories.value = listOf(
                Category("Electronics", R.drawable.ic_category1),
                Category("Fashion", R.drawable.ic_category2),
                Category("Furniture", R.drawable.ic_category3),
                Category("Industrial", R.drawable.ic_category4),
                Category("Electronics1", R.drawable.ic_category1),
                Category("Fashion2", R.drawable.ic_category2),
                Category("Furniture3", R.drawable.ic_category3),
                Category("Industrial4", R.drawable.ic_category4),
                Category("Electronics5", R.drawable.ic_category1),
                Category("Fashion6", R.drawable.ic_category2),
                Category("Furniture7", R.drawable.ic_category3),
                Category("Industrial8", R.drawable.ic_category4),
            )

            _isLoading.value = false
        }
    }
}
