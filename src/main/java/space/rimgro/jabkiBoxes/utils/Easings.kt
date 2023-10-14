package space.rimgro.jabkiBoxes.utils

import kotlin.math.pow
import kotlin.math.sqrt

class Easings {
    companion object{
        fun easeOutCirc(x: Double): Double{
            return sqrt(1 - (x - 1).pow(2));
        }
    }
}