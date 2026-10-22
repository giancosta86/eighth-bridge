package info.gianlucacosta.eighthbridge.fx

import javafx.beans.binding.DoubleBinding
import scalafx.beans.property.ReadOnlyDoubleProperty

trait Centered {
  def centerX: ReadOnlyDoubleProperty

  def centerY: ReadOnlyDoubleProperty
}
