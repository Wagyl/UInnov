/* Component */
abstract class Component(name: String) {
  val description: String
}

/* Relation */
abstract class Relation(src: Component, trgt: Component) {
  val name: String
  val description: String

  val source = src
  val target = trgt

  /* Add validity cases for the relation */
  type P = PartialFunction[(Component, Component), Boolean]
  var validityCases: P = { case _ => false }
  def addValidityCase(toAdd: P): Unit = validityCases =
    toAdd orElse validityCases

  /* Check validity of a relation */
  def checkValidity(source: Component, target: Component): Boolean =
    validityCases(source, target)

  /* Check validity of this relation */
  def checkValidity: Boolean =
    checkValidity(source, target)
}

/* GUI */
abstract class gui {
  val name: String
  val description: String

  val igGraph: Digraph[Component, Relation] = new Digraph
  var conflictingRelations: List[Relation] = List()

  /* Add component to this IG */
  def addComponent(toAdd: Component) = { igGraph.addNode(toAdd); toAdd }

  /* Add a relation between two components of this IG 
   * if the relation is valid */
  def addRelation(toAdd: Relation) =
    if (toAdd.checkValidity(toAdd.source, toAdd.target))
      igGraph.addArc(toAdd.source, toAdd.target, toAdd)
    else
      conflictingRelations = toAdd :: conflictingRelations

  /* Check validity of the GUI */
  def checkValidity: Boolean = {
    if (!conflictingRelations.isEmpty) {
      errorOutput(conflictingRelations.map(x => x.name), "Relations in conflict")
      false
    }
    true
  }

  /* Error output */
  def errorOutput(arguments: List[String], message: String) = {
    var argumentsString = ""
    for (argName <- arguments)
      argumentsString = argName ++ " - " ++ argumentsString

    println("Error(s) detected for : " ++ argumentsString ++ " : " + message)
  }
}