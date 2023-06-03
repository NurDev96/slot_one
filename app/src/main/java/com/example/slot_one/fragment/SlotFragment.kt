package com.example.slot_one.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.slot_one.R
import com.example.slot_one.databinding.FragmentSlotBinding
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Obfuscate
class SlotFragment : Fragment() {

    private var _binding: FragmentSlotBinding? = null
    private val binding: FragmentSlotBinding
        get() = _binding ?: throw RuntimeException("404")

    private var score = 0
    private var scoreTotal = 0
    private var mHealth = 5
    private var isStart = false

    private var isStop = 0
    private var slotJob: Job? = null

    private lateinit var slotImages: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slotImages = ArrayList<Int>().apply {
            add(R.drawable.png_1)
            add(R.drawable.png_2)
            add(R.drawable.png_3)
            add(R.drawable.png_4)
            add(R.drawable.png_5)
            add(R.drawable.png_6)
        }
        with(binding) {
            btnMenu.setOnClickListener {
                requireActivity().onBackPressed()
            }
            btnSpin.setOnClickListener {
                if (!isStart) {
                    tvAttempt.text = (--mHealth).toString()
                    startSlot()
                    isStart = true
                    tvScore.text = scoreTotal.toString()
                }
            }
        }
    }

    private fun startSlot() {
        slotJob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            while (isActive) {
                delay(200)
                isStop++
                with(binding) {
                    img1.set()
                    img2.set()
                    img3.set()
                    img4.set()
                    img5.set()
                    img6.set()
                    img7.set()
                    img8.set()
                    img9.set()
                    if (isStop == 20) { // тут не 20
                        delay(1000)
                        isStart = false
                        isStop = 0
                        slotJob?.cancel()
                        if (
                            (img1.tag == img2.tag && img2.tag == img3.tag)
                            ||
                            (img4.tag == img5.tag && img5.tag == img6.tag)
                            ||
                            (img7.tag == img8.tag && img8.tag == img9.tag)
                        ) {
                            // anime
                            score += 1000
                            Toast.makeText(
                                requireContext(),
                                "Jackpot +$score",
                                Toast.LENGTH_SHORT
                            ).show()
                            scoreTotal += score
                        }

                        if (
                            (img1.tag == img2.tag || img1.tag == img3.tag || img2.tag == img3.tag)
                            ||
                            (img4.tag == img5.tag || img4.tag == img6.tag || img5.tag == img6.tag)
                            ||
                            (img7.tag == img8.tag || img7.tag == img9.tag || img8.tag == img9.tag)
                        ) {
                            // anime
                            score += 300
                            Toast.makeText(
                                requireContext(),
                                "Jackpot +$score",
                                Toast.LENGTH_SHORT
                            ).show()
                            scoreTotal += score
                        } else {
                            // anime
                            score += 100
                            Toast.makeText(
                                requireContext(),
                                "Small +$score",
                                Toast.LENGTH_SHORT
                            ).show()
                            scoreTotal += score
                        }
                        if (mHealth == 0) {
                            Log.d("SlotGame", "endGame its work pre delay")
                            Log.d("SlotGame", "endGame its work post delay")
                            endGame()
                        }
                        tvScore.text = scoreTotal.toString()
                    }
                }
            }
        }
    }

    private fun endGame() {
        val alertDialog = AlertDialog.Builder(requireContext()).setCancelable(false)
        alertDialog.apply {
            setPositiveButton("Exit Game") { _, _ ->
                requireActivity().finish()
            }
            setNegativeButton("Spin") { _, _ ->
                requireActivity().recreate()
            }
            setTitle("Game result")
            setMessage("Your result : $scoreTotal")
            show()
        }
    }

    fun ImageView.set() {
        val index = Random.nextInt(0, 6)
        setImageResource(slotImages[index])
        tag = index.toString()
    }

//    private fun View.gone() {
//        visibility = View.GONE
//    }
//
//    private fun View.visible() {
//        visibility = View.VISIBLE
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        slotJob?.cancel()
    }
}
