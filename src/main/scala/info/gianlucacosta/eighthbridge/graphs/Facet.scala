package info.gianlucacosta.eighthbridge.graphs

//TODO! To Helios-core?
object Facet {
  def as[TFacet](instance: AnyRef): Option[TFacet] =
    instance match {
      case facet: TFacet =>
        Some(facet)

      case _ =>
        None
  }
}
