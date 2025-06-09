package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val combinationList: MutableList<IntArray> = ArrayList()
    private var boxPositions = IntArray(9) // 9 zeros
    private var playerTurn = 1
    private var totalSelectedBoxes = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        combinationList.addAll(
            listOf(
                intArrayOf(0, 1, 2),
                intArrayOf(3, 4, 5),
                intArrayOf(6, 7, 8),
                intArrayOf(0, 3, 6),
                intArrayOf(1, 4, 7),
                intArrayOf(2, 5, 8),
                intArrayOf(2, 4, 6),
                intArrayOf(0, 4, 8)
            )
        )

        val playerOne = intent.getStringExtra("playerOne")
        val playerTwo = intent.getStringExtra("playerTwo")

        binding?.playerOneName?.text = playerOne
        binding?.playerTwoName?.text = playerTwo

        val imageViews = listOf(
            binding?.image1,
            binding?.image2,
            binding?.image3,
            binding?.image4,
            binding?.image5,
            binding?.image6,
            binding?.image7,
            binding?.image8,
            binding?.image9
        )

        for (i in imageViews.indices) {
            imageViews[i]?.setOnClickListener {
                if (isBoxSelectable(i)) {
                    performAction(it as ImageView, i)
                }
            }
        }
    }

    private fun performAction(imageView: ImageView, selectedBoxPosition: Int) {
        boxPositions[selectedBoxPosition] = playerTurn

        val winnerName = if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage)
            binding?.playerOneName?.text.toString()
        } else {
            imageView.setImageResource(R.drawable.oimage)
            binding?.playerTwoName?.text.toString()
        }

        if (checkResults()) {
            val dialog = ResultDialog(this, "$winnerName is a Winner!", this)
            dialog.setCancelable(false)
            dialog.show()
        } else if (totalSelectedBoxes == 9) {
            val dialog = ResultDialog(this, "Match Draw", this)
            dialog.setCancelable(false)
            dialog.show()
        } else {
            changePlayerTurn(if (playerTurn == 1) 2 else 1)
            totalSelectedBoxes++
        }
    }

    private fun changePlayerTurn(currentPlayerTurn: Int) {
        playerTurn = currentPlayerTurn
        if (playerTurn == 1) {
            binding?.playerOneLayout?.setBackgroundResource(R.drawable.black_border)
            binding?.playerTwoLayout?.setBackgroundResource(R.drawable.white_border)
        } else {
            binding?.playerTwoLayout?.setBackgroundResource(R.drawable.black_border)
            binding?.playerOneLayout?.setBackgroundResource(R.drawable.white_border)
        }
    }

    private fun checkResults(): Boolean {
        for (combination in combinationList) {
            if (boxPositions[combination[0]] == playerTurn &&
                boxPositions[combination[1]] == playerTurn &&
                boxPositions[combination[2]] == playerTurn
            ) {
                return true
            }
        }
        return false
    }

    private fun isBoxSelectable(boxPosition: Int): Boolean {
        return boxPositions[boxPosition] == 0
    }

    fun restartMatch() {
        boxPositions = IntArray(9) // Reset to 9 zeros
        playerTurn = 1
        totalSelectedBoxes = 1

        val imageViews = listOf(
            binding?.image1,
            binding?.image2,
            binding?.image3,
            binding?.image4,
            binding?.image5,
            binding?.image6,
            binding?.image7,
            binding?.image8,
            binding?.image9
        )

        imageViews.forEach { it?.setImageResource(R.drawable.white_border) }
    }
}
