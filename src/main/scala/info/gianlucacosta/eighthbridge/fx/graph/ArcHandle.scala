package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.entities.Entity
import scalafx.geometry.{Dimension2D, Point2D}


case class ArcHandle(
  center: Point2D,
  dimension: Dimension2D
) extends Entity
