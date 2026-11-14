package info.gianlucacosta.eighthbridge.graphs.facets

trait HasNameSettable extends TimestampedPrototype with HasName {
  private var _name: String =
    ""

  override def name: String =
    _name

  def setName(name: String): this.type = {
      if (name != _name) {
        require(name.nonEmpty, "The name cannot be empty!")

        val result =
          clone()

        result._name =
          name

        result
      } else
        this
  }
}
