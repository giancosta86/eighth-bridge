package info.gianlucacosta.eighthbridge.graphs.features

import scalafx.geometry.Point2D

trait HasOptionalCustomLabelCenter {
  def customLabelCenter: Option[Point2D]
}
