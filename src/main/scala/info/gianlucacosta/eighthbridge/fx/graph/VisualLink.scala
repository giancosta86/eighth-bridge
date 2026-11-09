package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.facets._

trait VisualLink extends TimestampedPrototype
  with Link
  with HasSelectedSettable
  with HasStyleClasses
  with HasOptionalArcBarbs
  with HasHandlesSettable

object VisualLink {
  def createDirected(): VisualLink =
    new VisualLink {
      override def arcBarbs: Option[ArcBarbs] =
        Some(ArcBarbs.Default)

      override def styleClasses: Set[String] =
        Set[String]()
    }

  def createUndirected(): VisualLink =
    new VisualLink {
      override def arcBarbs: Option[ArcBarbs] =
        None

      override def styleClasses: Set[String] =
        Set[String]()
    }
}