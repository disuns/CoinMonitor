package com.android.trade.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog<VB : ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    private val binding get() = _binding!!

    abstract fun setupView()

    override fun onStart() {
        super.onStart()

        val bottomSheetView = requireView().parent as View
        val behavior = BottomSheetBehavior.from(bottomSheetView)

        // 바텀시트의 내용물 높이를 계산
        val contentView = binding.root // 바텀시트에 있는 콘텐츠의 루트 뷰
        contentView.post {
            val contentHeight = contentView.height // 콘텐츠 높이 계산
            val maxPeekHeight = (resources.displayMetrics.heightPixels * 0.5).toInt() // 화면의 절반 크기

            // 콘텐츠 높이가 화면 절반보다 작으면 콘텐츠 크기만큼 표시
            // 그렇지 않으면 화면 절반만큼 표시
            behavior.peekHeight =
                if (contentHeight < maxPeekHeight) contentHeight else maxPeekHeight

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isFitToContents = false // 콘텐츠 크기에 맞추지 않도록 설정
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        // 바깥 영역 클릭 시 닫히도록 설정
        dialog?.setCancelable(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun bind(block: VB.() -> Unit) {
        binding.apply(block)
    }
}