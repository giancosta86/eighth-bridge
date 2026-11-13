package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.facets.TimestampedPrototype

trait HasBarbsSettable extends TimestampedPrototype with Arc {
  private var _barbs: ArcBarbs =
    ArcBarbs.Default

  override def barbs: ArcBarbs =
    _barbs

  def setArcBarbs(barbs: ArcBarbs): this.type =
    if (barbs != _barbs) {
      val result =
        clone()

      result._barbs =
        barbs

      result
    } else
      this
}
