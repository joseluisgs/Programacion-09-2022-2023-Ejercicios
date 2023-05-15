module com.example.loschinos {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires koin.logger.slf4j;
    requires koin.core.jvm;


    opens com.example.loschinos to javafx.fxml;
    exports com.example.loschinos;

    opens com.example.loschinos.controllers to javafx.fxml;
    exports com.example.loschinos.controllers;
}