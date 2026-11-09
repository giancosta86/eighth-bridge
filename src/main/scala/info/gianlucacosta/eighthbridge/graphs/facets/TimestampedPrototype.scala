package info.gianlucacosta.eighthbridge.graphs.facets

class TimestampedPrototype extends Prototype with HasTimestamp {
  private var _timestamp: Timestamp =
    Timestamp.now

  override def timestamp: Timestamp =
    _timestamp

  /**
   * Clones the object, updating the timestamp of the copy.
   */
  override def clone(): this.type = {
    val result =
      super.clone()

    result._timestamp =
      Timestamp.now

    result
  }
}

