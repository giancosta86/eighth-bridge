package info.gianlucacosta.eighthbridge.graphs.point2point.specific

/**
  * Object having a name
  */
trait Named[T <: Named[T]] {
  this: T =>
  def name: String

  /**
    * Copies the current object, giving it a new name.
    *
    * If you implement this trait as a "case class", you can implement this method just by using the Scala-provided copy() method.
    *
    * @param name The new name
    * @return The resulting new object
    */
  def nameCopy(name: String): T
}
