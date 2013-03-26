/* Concrete components */
class Panel(name: String) extends Component(name) with AcceptMenu
  with AcceptSwitch with Container {
  val description = "Simple panel"
}

class Menu(name: String) extends Component(name) with AcceptMenu {
  val description = "Simple menu"
}

class ListPres(name: String) extends Component(name) {
  val description = "Graphical view of a list"
}

class CounterPres(name: String) extends Component(name) {
  val description = "Graphical view of a counter"
}

/* Traits for component options */
trait ComponentOption {
  def feature: String
}

trait AcceptMenu extends ComponentOption {
  override def feature: String = "Accept menus"
}

trait AcceptSwitch extends ComponentOption {
  override def feature: String = "Accept to switch"
}

trait Container extends ComponentOption {
  override def feature: String = "Accept inclusions"
}

/* Concrete relations */
class SimpleInclusion(source: Component, target: Component)
  extends Relation(source, target) {
  val name = "Simple inclusion"
  val description = "Inclusion"

  addValidityCase({ case (_, _: Container) => true })
}

class MenuRelation(source: Component, target: Component)
  extends Relation(source, target) {
  val name = "Menu relation"
  val description = "Menu of !"

  addValidityCase({ case (_: Menu, _: AcceptMenu) => true })
}

class SwitchRelation(source: Component, target: Component)
  extends Relation(source, target) {
  val name = "Switch relation"
  val description = "Switching"

  addValidityCase({ case (_: AcceptSwitch, _: AcceptSwitch) => true })
}