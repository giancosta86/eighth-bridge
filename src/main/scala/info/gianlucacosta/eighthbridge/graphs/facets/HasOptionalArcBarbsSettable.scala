package info.gianlucacosta.eighthbridge.graphs.facets

trait HasOptionalArcBarbsSettable extends HasTimestampSettable with HasOptionalArcBarbs {
  private var _arcBarbs: Option[ArcBarbs] =
    None

  override def arcBarbs: Option[ArcBarbs] =
    _arcBarbs

  def setArcBarbs(barbs: Option[ArcBarbs]): this.type =
    if (barbs != _arcBarbs) {
      val result =
        updateTimestamp()

      result._arcBarbs =
        barbs

      result
    } else
      this
}
