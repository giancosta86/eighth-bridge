package info.gianlucacosta.eighthbridge.graphs.facets

//TODO! Move this to Helios?
/**
 * This class is required in Scala 2.11 to support calling clone() from within a concrete trait method,
 * but also provide a convenient clone() method returning "this.type".
 */
class Prototype extends Cloneable {
  override def clone(): this.type =
    super
      .clone()
      .asInstanceOf[this.type]
}
