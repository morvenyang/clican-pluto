package com.clican.pluto.cms.ui.ext.renderer;

import java.io.IOException;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.AjaxContext;

public class ModalPanelRenderer extends org.richfaces.renderkit.html.ModalPanelRenderer {

    @Override
    public void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }
        child.encodeBegin(facesContext);
        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(facesContext);
        Set<String> ids = ajaxContext.getAjaxAreasToRender();
        if (ids.size() != 0) {
            if (child.getRendersChildren()) {
                child.encodeChildren(facesContext);
            } else {
                renderChildren(facesContext, child);
            }
        } else {
            UIComponent header = child.getParent().getFacet("header");
            UIComponent controls = child.getParent().getFacet("controls");
            if (child == header || child == controls) {
                if (child.getRendersChildren()) {
                    child.encodeChildren(facesContext);
                } else {
                    renderChildren(facesContext, child);
                }
            }
        }
        child.encodeEnd(facesContext);
    }

}
