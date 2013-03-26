/* Graph - abstract */
abstract class GraphBase[T, U] {

  /* Edges - class */
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple = (n1.value, n2.value, value)
  }

  /* Nodes - class */
  case class Node(value: T) {
    var adj: List[Edge] = List()
    var adjUndirected: List[Edge] = List()
    def neighbors: List[Node] = adj.map(edgeTarget(_, this).get)
    def undirectedNeighbors: List[Node] =
      adjUndirected.map(edgeUndirected(_, this).get)
  }

  /* Nodes and edges */
  var nodes: Map[T, Node] = Map()
  var edges: List[Edge] = List()

  /* If the edge E connects N to another node, returns the other node,
  otherwise returns None. */
  def edgeTarget(e: Edge, n: Node): Option[Node]

  /* Only different for directed graph */
  def edgeUndirected(e: Edge, n: Node): Option[Node]

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

  /* Isolated nodes */
  def lonelyNodes: List[Node] = {
    var res: List[Node] = List()

    for (node <- nodes.valuesIterator)
      if (node.neighbors.isEmpty)
        res = node :: res

    res
  }

  /* Isolated nodes - different for directed graph */
  def undirectedLonelyNodes: List[Node] = {
    var res: List[Node] = List()

    for (node <- nodes.valuesIterator)
      if (node.undirectedNeighbors.isEmpty)
        res = node :: res

    res
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

  /* Edge source OR target */
  def edgeUndirected(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else if (e.n2 == n) Some(e.n1)
    else None

  /* Add an arc to the graph */
  def addArc(source: T, dest: T, value: U) = {
    val e = new Edge(nodes(source), nodes(dest), value)
    edges = e :: edges
    nodes(source).adj = e :: nodes(source).adj
    nodes(source).adjUndirected = e :: nodes(source).adjUndirected
    nodes(dest).adjUndirected = e :: nodes(dest).adjUndirected
  }
}