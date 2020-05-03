import com.github.javafaker.Faker
import javafx.beans.binding.ListBinding
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import java.util.function.Predicate
import java.util.regex.PatternSyntaxException


class TableDemoApp : App(TableView::class)

fun main() {
    launch<TableDemoApp>()
}

data class Entry(val name: String, val surname: String)

//Not a strict teacher, but demo-data generator
val faker = Faker()

class TableView : View("My View") {
    val pageSize = 10

    // all items
    val items = FXCollections.observableArrayList<Entry>()
    // list view after filter application
    val filteredItems = items.filtered { true }
    // the number of current page in paginator
    val pageNumberProperty = SimpleIntegerProperty(0)
    var pageNumber by pageNumberProperty

    // content of the current page. Bound to both page number and
    val page = object : ListBinding<Entry>() {
        init {
            bind(pageNumberProperty, filteredItems)
        }

        override fun computeValue(): ObservableList<Entry> {
            val chunks = filteredItems.chunked(pageSize)
            return if (chunks.isEmpty()) {
                FXCollections.emptyObservableList()
            } else {
                val actualpage = pageNumberProperty.get().coerceAtMost(chunks.size - 1).coerceAtLeast(0)
                chunks[actualpage].asObservable()
            }
        }
    }


    override val root = borderpane {
        center {
            tableview<Entry> {
                //can't use binding as item storage so bind a copy
                items.bind(page) { it }
                readonlyColumn("Имя", Entry::name)
                readonlyColumn("Фамилия", Entry::surname) {
                    //clearing the text
                    text = null
                    //replacing it with vertical alignment
                    graphic = vbox {
                        alignment = Pos.CENTER
                        label("Фамилия")
                        textfield {
                            promptText = "Фильтр"
                            hgrow = Priority.ALWAYS
                            textProperty().onChange {
                                try {
                                    val regex = this@textfield.text.toRegex()
                                    pageNumber = 0
                                    filteredItems.predicate = Predicate { entry ->
                                        this@textfield.text.isBlank() || entry.surname.matches(regex)
                                    }
                                } catch (pse: PatternSyntaxException) {
                                    // display validation error?
                                }

                            }
                        }
                    }
                }
            }
        }
        bottom {
            pagination {
                pageCountProperty().bind(filteredItems.sizeProperty.integerBinding { filteredItems.size / pageSize })
                pageNumberProperty.bindBidirectional(currentPageIndexProperty())
            }
        }
    }

    init {
        repeat(100) {
            items.add(Entry(faker.name().firstName(), faker.name().lastName()))
        }
    }


}