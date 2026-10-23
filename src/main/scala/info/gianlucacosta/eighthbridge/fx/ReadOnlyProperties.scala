package info.gianlucacosta.eighthbridge.fx

import scalafx.beans.Observable
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyDoubleWrapper}

//TODO! Move this to Helios-FX!
object ReadOnlyProperties {
  def createDouble(expression: () => Double, dependencies: Observable*): ReadOnlyDoubleProperty = {
    val wrapper =
      new ReadOnlyDoubleWrapper()

    wrapper.bind(
      Bindings.createDoubleBinding(
        expression,

        dependencies:_*
      )
    )

    wrapper
  }
}
