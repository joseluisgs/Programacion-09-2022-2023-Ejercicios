module com.example.vehiculoscrudinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.sql;
    requires org.mybatis;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires kotlin.stdlib.jdk7;

    opens com.example.vehiculoscrudinterface.controllers to javafx.fxml;
    exports com.example.vehiculoscrudinterface;
}