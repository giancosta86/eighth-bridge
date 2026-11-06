package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs

//TODO! Rename the property to "arcBarbs", here and in the read-only interface!
trait HasOptionalArcBarbsSettable extends HasTimestampSettable with HasOptionalArcBarbs {
  protected val initialBarbs: Option[ArcBarbs] =
    None

  private var _barbs: Option[ArcBarbs] =
    initialBarbs

  override def barbs: Option[ArcBarbs] =
    _barbs

  def setBarbs(barbs: Option[ArcBarbs]): this.type =
    if (barbs != _barbs) {
      val result =
        updateTimestamp()

      result._barbs =
        barbs

      result
    } else
      this
}
