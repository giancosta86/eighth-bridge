package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.beans.Observable
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyDoubleWrapper, ReadOnlyObjectProperty, ReadOnlyObjectWrapper}
import scalafx.Includes._

//TODO! Move this to Helios-FX!
object ReadOnlyProperties {
  def createDouble(expression: () => Double, dependencies: Observable*): ReadOnlyDoubleProperty = {
    val backingProperty =
      new SimpleDoubleProperty

    backingProperty <==
      Bindings.createDoubleBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }

  def createObject[T](expression: () => T, dependencies: Observable*): ReadOnlyObjectProperty[T] = {
    val backingProperty =
      new SimpleObjectProperty[T]

    backingProperty <==
      Bindings.createObjectBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }
}
