package info.gianlucacosta.eighthbridge.fx.graph.features

import scalafx.geometry.Point2D

trait Labeled {
  def label: String

  def setLabel(label: String): this.type

  def labelCenter: Option[Point2D]

  def setLabelCenter(labelCenter: Option[Point2D]): this.type
}
