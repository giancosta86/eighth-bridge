package info.gianlucacosta.eighthbridge.graphs.point2point.specific

import info.gianlucacosta.helios.mathutils.Numbers

/**
  * Object having a weight
  */
trait Weighted[T <: Weighted[T]] {
  this: T =>
  def minWeight: Double

  def maxWeight: Double

  def weight: Double


  /**
    * Ensures the weight is in the range [minWeight; maxWeight], throwing an IllegalArgumentException in case of errors
    */
  protected def checkWeight(): Unit = {
    require(
      minWeight <= weight && weight <= maxWeight,
      s"Weight must be in [${Numbers.smartString(minWeight)}; ${Numbers.smartString(maxWeight)}"
    )
  }

  /**
    * Copies the current object, giving it a new weight.
    *
    * If you implement this trait as a "case class", you can implement this method just by using the Scala-provided copy() method.
    *
    * @param weight The new weight
    * @return The resulting new object
    */
  def weightCopy(weight: Double): T
}
