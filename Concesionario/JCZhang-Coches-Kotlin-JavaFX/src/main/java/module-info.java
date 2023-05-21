module com.jczhang.jczhangcocheskotlinjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires koin.logger.slf4j;
    requires koin.core.jvm;
    requires java.sql;


    opens com.jczhang.jczhangcocheskotlinjavafx to javafx.fxml;
    exports com.jczhang.jczhangcocheskotlinjavafx;

    opens com.jczhang.jczhangcocheskotlinjavafx.controller to javafx.fxml;
    exports com.jczhang.jczhangcocheskotlinjavafx.controller;

    opens com.jczhang.jczhangcocheskotlinjavafx.routes to javafx.fxml;
    exports com.jczhang.jczhangcocheskotlinjavafx.routes;

    opens com.jczhang.jczhangcocheskotlinjavafx.models to javafx.fxml;
    exports com.jczhang.jczhangcocheskotlinjavafx.models;

}