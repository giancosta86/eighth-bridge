package info.gianlucacosta.eighthbridge.graphs.facets

trait HasSelectedSettable extends HasTimestampSettable with HasSelected {
  protected val initialSelected: Boolean =
    false

  private var _selected: Boolean =
    initialSelected

  override def selected: Boolean =
    _selected

  def setSelected(selected: Boolean): this.type =
    if (selected != _selected) {
      val result =
        updateTimestamp()

      result._selected =
        selected

      result
    } else
      this
}
