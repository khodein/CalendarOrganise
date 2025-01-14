package ru.calendar.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.calendar.navigator.Navigator
import ru.calendar.navigator.NavigatorScreen

abstract class BaseFragment<VB : ViewBinding, NS: NavigatorScreen>(
    @LayoutRes private val contentLayoutId: Int,
    private val vbInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val navigatorScreen: NS,
) : Fragment(contentLayoutId), Navigator.Provider {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract val viewModel: BaseViewModel

    final override fun getNavigatorScreen(): NavigatorScreen = navigatorScreen

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = vbInflate.invoke(inflater, container, false)
        return binding.root
    }
}