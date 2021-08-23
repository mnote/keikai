package io.github.mnote.zkoss.hello;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class HelloComposer extends SelectorComposer {

    @Wire("label")
    Label label;

    @Listen("onClick = button")
    public void hello(){
        label.setValue("Hello Mimic");
    }

}
