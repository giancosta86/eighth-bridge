package info.gianlucacosta.eighthbridge.core.facets

import scalafx.geometry.Point2D

trait HasOptionalCustomLabelCenter {
  def customLabelCenter: Option[Point2D]
}
