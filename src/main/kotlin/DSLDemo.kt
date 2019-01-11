import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class DSLDemoApp : App(DemoView::class, DemoStyle::class)

fun main() {
    launch<DSLDemoApp>()
}

class DemoView : View("Hello TornadoFX") {
    override val root = vbox {
        val label = label(title) {
            addClass(DemoStyle.heading)
        }
        button("A button")
        checkbox {
            text = "check me"
            val stringProperty = selectedProperty().stringBinding { selected ->
                if (selected != false) {
                    "checked"
                } else {
                    "unchecked"
                }
            }
            label.textProperty().bind(stringProperty)
        }
    }
}

class DemoStyle : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
    }
}