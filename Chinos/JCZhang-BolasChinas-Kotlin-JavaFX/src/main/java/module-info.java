module com.jczhang.jczhangbolaschinaskotlinjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires io.github.microutils.kotlinlogging;
    requires org.slf4j;
    requires kotlin.result.jvm;
    requires java.desktop;
    requires koin.core.jvm;


    opens com.jczhang.jczhangbolaschinaskotlinjavafx to javafx.fxml;
    exports com.jczhang.jczhangbolaschinaskotlinjavafx;

    opens com.jczhang.jczhangbolaschinaskotlinjavafx.controllers to javafx.fxml;
    exports com.jczhang.jczhangbolaschinaskotlinjavafx.controllers to javafx.fxml;

    opens com.jczhang.jczhangbolaschinaskotlinjavafx.routes to javafx.fxml;
    exports com.jczhang.jczhangbolaschinaskotlinjavafx.routes;
}