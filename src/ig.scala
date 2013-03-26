/* Component */
abstract class Component(nameC: String) {
  val name: String = nameC
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

/*Relation debug item */
class RelationDebug(toDebug: Relation) {
  val value: Relation = toDebug
  var debugFunctions: List[DebugToolFunction] = List()

  def debugMessage: String = {
    var optionMessage: String = ""

    for (debugFun <- debugFunctions)
      if (debugFun.value())
        optionMessage = optionMessage ++ debugFun.description ++ " "

    value.name ++ "[" ++ optionMessage ++ "]"
  }

  /* Some debug tools */
  val symetryProblem = new Function[Unit, Boolean] {
    def apply(v: Unit): Boolean =
      value.checkValidity(value.target, value.source) == true
  }

  debugFunctions = List(new DebugToolFunction("Symetry problem", symetryProblem))
}

/* Debug tool - function */
class DebugToolFunction(message: String, fun: Function[Unit, Boolean]) {
  val value: Function[Unit, Boolean] = fun
  val description: String = message
}

/* GUI */
abstract class gui {
  val name: String
  val description: String

  val igGraph: Digraph[Component, Relation] = new Digraph
  var conflictingRelations: List[RelationDebug] = List()
  var checkFunctions: List[Function[Unit, Boolean]] = List()

  /* Add a check function to this IG */
  def addCheckFunction(toAdd: Function[Unit, Boolean]) =
    checkFunctions = toAdd :: checkFunctions

  /* Add component to this IG */
  def addComponent(toAdd: Component) = { igGraph.addNode(toAdd); toAdd }

  /* Add a relation between two components of this IG 
   * if the relation is valid */
  def addRelation(toAdd: Relation) =
    if (toAdd.checkValidity(toAdd.source, toAdd.target))
      igGraph.addArc(toAdd.source, toAdd.target, toAdd)
    else
      conflictingRelations = new RelationDebug(toAdd) :: conflictingRelations

  /* Check validity of the relations GUI */
  val checkRelationsValidity = new Function[Unit, Boolean] {
    def apply(v: Unit) = {
      if (!conflictingRelations.isEmpty) {
        errorOutput(conflictingRelations.map(x => x.debugMessage),
          "Relations in conflict")
        false
      }
      true
    }
  }
  addCheckFunction(checkRelationsValidity)

  /* Check for lonely components */
  val checkLonelyComponents = new Function[Unit, Boolean] {
    def apply(v: Unit) = {
      if (!igGraph.lonelyNodes.isEmpty) {
        errorOutput(igGraph.undirectedLonelyNodes.map(x => x.value.name),
          "Lonely components")
        true
      }
      false
    }
  }
  addCheckFunction(checkLonelyComponents)

  /* Check GUI */
  def checkGui: Unit =
    /* checkFunctions.foldLeft(true)((x, y) => x && y())
     l'optimisation du && ne permet pas d'itérer sur la liste entière
     si un false est trouvé au milieu */
    for (checkFun <- checkFunctions)
      checkFun()

  /* Error output */
  def errorOutput(arguments: List[String], message: String) = {
    var argumentsString = ""
    for (argName <- arguments)
      argumentsString = argName ++ " - " ++ argumentsString

    println("Error(s) detected for : " ++ argumentsString ++ " : " + message)
  }
}