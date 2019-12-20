import Vector.Companion.getSingular
import org.apache.commons.math3.linear.BlockRealMatrix
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import kotlin.math.pow

typealias VecFun = (Vector) -> Double

fun getGrad(start: Vector, func: VecFun, delta: Double): Vector {
    val grad = ArrayList<Double>(start.arity)
    for (i in 0 until start.arity) {
        val dx = delta * start.project(i)
        val diff = (func(start + dx) - func(start))
        grad.add(diff / (dx.coords[i]))
    }
    var tmp = Vector(grad)
    //tmp /= tmp.len
    return tmp
}

fun getHesse(start: Vector, func: VecFun, delta: Double):RealMatrix {
    val matrix = BlockRealMatrix(start.arity,start.arity)
    for (i in 0 until start.arity) {
        for (j in 0 until start.arity) {
            matrix.setEntry(i,j,getSecDerivative(start,func,i,j,delta))
        }
    }
    return matrix
}

fun getSecDerivative(start: Vector, func: VecFun, arg1: Int, arg2: Int, delta: Double): Double {
    val dim = start.arity
    val e1 = getSingular(arg1, dim)
    val e2 = getSingular(arg2, dim)
    val res =
        (func(start + (e1 + e2) * delta) - func(start + e1 * delta) - func(start + e2 * delta) + func(start)) / delta.pow(
            2
        )
    return res
}

fun getMin(start: Vector, func: VecFun, delta: Double):Vector {
    val grad = getGrad(start,func,delta)
    if (grad.len<delta) return start
    val hesse = getHesse(start,func,delta)
    val iter = start - (MatrixUtils.inverse(hesse)).operate(grad.toApache()).toVector()
    return getMin(iter,func,delta)
}

fun RealVector.toVector():Vector {
    return Vector(this.toArray().toList())
}