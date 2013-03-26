abstract class Component {
  val name: String
  val description: String
}

abstract class Relation(source: Component, target: Component) {
  val name: String
  val description: String

  /* Add validity cases for the relation */
  type P = PartialFunction[(Component, Component), Boolean]
  var validityCases: P = { case _ => false }
  def addValidityCase(toAdd: P): Unit = validityCases =
    toAdd orElse validityCases

  /* Check validity of the relation */
  def checkValidity(source: Component, target: Component): Boolean =
    validityCases(source, target)
}

abstract class ig {
  val igGraph: Digraph[Component, Relation]

  /* Add component to this IG */
  def addComponent(toAdd: Component) = igGraph.addNode(toAdd)

  /* Add a relation between two components of this IG 
   * if the relation is valid */
  def addRelation(toAdd: Relation, source: Component, target: Component) =
    if (toAdd.checkValidity(source, target))
      igGraph.addArc(source, target, toAdd)
}