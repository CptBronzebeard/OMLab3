import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealVector
import kotlin.math.pow
import kotlin.math.sqrt

data class Vector(val coords: List<Double>) {
    val arity: Int
        get() = coords.size
    val len: Double
        get() = sqrt(coords.fold(0.0) { sum, element -> sum + element.pow(2) })

    fun project(i:Int):Vector {
        val tmp=ArrayList<Double>()
        coords.forEachIndexed{idx,it -> if (idx==i) tmp.add(it) else tmp.add(0.0)}
        return Vector(tmp)
    }

    operator fun plus(input: Vector) = Vector(coords.zip(input.coords, Double::plus))

    operator fun times(input: Double) = Vector(coords.map { it * input })

    operator fun div(input: Double) = times(1 / input)

    operator fun minus(input: Vector) = this + (input * -1.0)

    fun toApache():RealVector {
        return ArrayRealVector(coords.toDoubleArray())
    }

    companion object {
        fun getSingular(arg: Int, dimensions: Int):Vector {
            val arr = Array(dimensions) {0.0}
            arr[arg] = 1.0
            return Vector(arr.toList())
        }
    }
}

operator fun Double.times(input: Vector) = input * this