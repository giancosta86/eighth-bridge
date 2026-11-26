package info.gianlucacosta.eighthbridge.core.facets

trait HasBarbsSettable extends TimestampedPrototype with HasBarbs {
  private var _barbs: Barbs =
    Barbs.Default

  override def barbs: Barbs =
    _barbs

  def setBarbs(barbs: Barbs): this.type =
    if (barbs != _barbs) {
      val result =
        clone()

      result._barbs =
        barbs

      result
    } else
      this
}
