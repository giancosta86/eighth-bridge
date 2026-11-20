package info.gianlucacosta.eighthbridge.core.bottomright

private class Fringe(onEmpty: () => Unit) {
  private var _value: Double =
    0

  def value: Double =
    _value

  //TODO! Simplify the notation for empty collections?
  private var items: Set[Any] =
    Set[Any]()

  def notifyItemValue(item: Any, itemValue: Double): Unit = {
    Math.signum(itemValue - _value) match {
      case 1 =>
        items =
          Set(item)

        _value =
          itemValue

      case 0 =>
        items +=
          item

      case _ =>
        remove(item)
    }
  }

  def remove(item: Any): Unit = {
    items -=
      item

    if (items.isEmpty) {
      _value =
        0

      onEmpty()
    }
  }
}
