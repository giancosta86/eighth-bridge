package info.gianlucacosta.eighthbridge.graphs

//TODO! Move to Helios!
object MapUtils {
  implicit class MapOfListExtensions[K, V](self: Map[K, List[V]]) {
    def safePrepend(entry: (K, V)): Map[K, List[V]] = {
      val (key, value) =
        entry

      val listToUpdate =
        self.getOrElse(key, Nil)

      val updatedList =
        value::listToUpdate

      self +
        (key -> updatedList)
    }

    def safeAppend(entry: (K, V)): Map[K, List[V]] = {
      val (key, value) =
        entry

      val listToUpdate =
        self.getOrElse(key, Nil)

      val updatedList =
        listToUpdate :+ value

      self +
        (key -> updatedList)
    }
  }

  implicit class MapOfSetExtensions[K, V](self: Map[K, Set[V]]) {
    def safeAdd(entry: (K, V)): Map[K, Set[V]] = {
      val (key, value) =
        entry

      val setToUpdate =
        self.getOrElse(key, Set[V]())

      val updatedSet =
        setToUpdate + value

      self +
        (key -> updatedSet)
    }
  }
}
