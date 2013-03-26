/* Concrete components */
class Panel extends Component with AcceptMenu with AcceptSwitch {
  val name = "Panel"
  val description = "Simple panel"
}

class Menu extends Component with AcceptMenu {
  val name = "Menu"
  val description = "Simple menu"
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

/* Concrete relations */
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