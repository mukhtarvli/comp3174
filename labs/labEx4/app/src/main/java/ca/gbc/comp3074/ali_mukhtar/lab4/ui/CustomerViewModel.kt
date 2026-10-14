
package ca.gbc.comp3074.ali_mukhtar.lab4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.ali_mukhtar.lab4.data.Customer
import ca.gbc.comp3074.ali_mukhtar.lab4.data.CustomerDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CustomerViewModel(
    private val customerDao: CustomerDao
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val customers: Flow<List<Customer>> =
        searchQuery.flatMapLatest { query ->
            if (query.isBlank()) {
                customerDao.getAllCustomers()
            } else {
                customerDao.searchCustomers(query)
            }
        }

    fun searchCustomers(searchText: String) {
        searchQuery.value = searchText.trim()
    }

    fun clearSearch() {
        searchQuery.value = ""
    }

    fun addCustomer(
        name: String,
        age: Int,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            customerDao.insert(
                Customer(
                    name = name,
                    age = age,
                    isActive = isActive
                )
            )
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.delete(customer)
        }
    }
}

