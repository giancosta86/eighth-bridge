package info.gianlucacosta.eighthbridge.graphs.facets

trait HasSelectedSettable extends TimestampedPrototype with HasSelected {
  private var _selected: Boolean =
    false

  override def selected: Boolean =
    _selected

  def setSelected(selected: Boolean): this.type =
    if (selected != _selected) {
      val result =
        clone()

      result._selected =
        selected

      result
    } else
      this
}
