/* Graph - abstract */
abstract class GraphBase[T, U] {

  /* Edges - class */
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple = (n1.value, n2.value, value)
  }

  /* Nodes - class */
  case class Node(value: T) {
    var adj: List[Edge] = Nil
    def neighbors: List[Node] = adj.map(edgeTarget(_, this).get)
  }

  /* Nodes and edges */
  var nodes: Map[T, Node] = Map()
  var edges: List[Edge] = Nil

  /* If the edge E connects N to another node, returns the other node,
  otherwise returns None. */
  def edgeTarget(e: Edge, n: Node): Option[Node]

  /* equals */
  override def equals(o: Any) = o match {
    case g: GraphBase[_, _] => (nodes.keys.toList -- g.nodes.keys.toList == Nil &&
      edges.map(_.toTuple) -- g.edges.map(_.toTuple) == Nil)
    case _ => false
  }

  /* Add a node to the graph - returns it */
  def addNode(value: T) = {
    val n = new Node(value)
    nodes = Map(value -> n) ++ nodes
    n
  }
}

/* Directed graph */
class Digraph[T, U] extends GraphBase[T, U] {
  /* equals */
  override def equals(o: Any) = o match {
    case g: Digraph[_, _] => super.equals(g)
    case _ => false
  }

  /* If the edge E connects N to another node, returns the other node,
  otherwise returns None. */
  def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else None

  /* Add an arc to the graph */
  def addArc(source: T, dest: T, value: U) = {
    val e = new Edge(nodes(source), nodes(dest), value)
    edges = e :: edges
    nodes(source).adj = e :: nodes(source).adj
  }
}