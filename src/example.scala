/* IG Example */
object SimpleExample extends App {

  val myGui = new gui {
    val name = "GUI example 1.0"
    val description = "A simple GUI example"
  }

  /* Components */
  val menu = myGui.addComponent(new Menu("Main menu"))
  val panelOne = myGui.addComponent(new Panel("Panel one"))
  val panelTwo = myGui.addComponent(new Panel("Panel two"))
  val counterPres = myGui.addComponent(new CounterPres("Counter pres"))
  val listPres = myGui.addComponent(new ListPres("List pres"))

  /* Valid relations */
  myGui.addRelation(new MenuRelation(menu, panelOne))
  myGui.addRelation(new MenuRelation(menu, panelTwo))
  myGui.addRelation(new SwitchRelation(panelOne, panelTwo))
  myGui.addRelation(new SimpleInclusion(listPres, panelOne))
  myGui.addRelation(new SimpleInclusion(counterPres, panelTwo))

  /* Some conflicting relations */
  myGui.addRelation(new MenuRelation(panelOne, panelTwo))
  myGui.addRelation(new SimpleInclusion(panelOne, counterPres))

  /* Test */
  myGui.checkValidity
}