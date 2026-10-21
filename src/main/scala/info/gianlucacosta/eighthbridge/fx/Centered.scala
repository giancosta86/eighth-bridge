package info.gianlucacosta.eighthbridge.fx

import scalafx.beans.property.ReadOnlyDoubleProperty

trait Centered {
  def centerX: ReadOnlyDoubleProperty

  def centerY: ReadOnlyDoubleProperty
}
