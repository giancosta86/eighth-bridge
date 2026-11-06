package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property._
import scalafx.Includes._
import scalafx.beans.Observable
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyDoubleProperty, ReadOnlyFloatProperty, ReadOnlyIntegerProperty, ReadOnlyLongProperty, ReadOnlyObjectProperty, ReadOnlyStringProperty}

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

  def createBoolean(expression: () => Boolean, dependencies: Observable*): ReadOnlyBooleanProperty = {
    val backingProperty =
      new SimpleBooleanProperty()

    backingProperty <==
      Bindings.createBooleanBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }

  def createInt(expression: () => Int, dependencies: Observable*): ReadOnlyIntegerProperty = {
    val backingProperty =
      new SimpleIntegerProperty

    backingProperty <==
      Bindings.createIntegerBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }

  def createLong(expression: () => Long, dependencies: Observable*): ReadOnlyLongProperty = {
    val backingProperty =
      new SimpleLongProperty

    backingProperty <==
      Bindings.createLongBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }

  def createFloat(expression: () => Float, dependencies: Observable*): ReadOnlyFloatProperty = {
    val backingProperty =
      new SimpleFloatProperty

    backingProperty <==
      Bindings.createFloatBinding(
        expression,

        dependencies:_*
      )

    backingProperty
  }

  def createString(expression: () => String, dependencies: Observable*): ReadOnlyStringProperty = {
    val backingProperty =
      new SimpleStringProperty

    backingProperty <==
      Bindings.createStringBinding(
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
