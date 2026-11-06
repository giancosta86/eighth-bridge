package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.fx.graph.NamedVisualVertex

trait HasNameSettable extends HasTimestampSettable with HasName {
  protected val initialName: String =
    ""

  private var _name: String =
    initialName

  override def name: String =
    _name

  def setName(name: String): this.type = {
      if (name != _name) {
        require(name.nonEmpty, "The name cannot be empty!")

        val result =
          this
            .updateTimestamp()

        result._name =
          name

        result
      } else
        this
  }
}
