package com.clican.pluto.cms.ui.ext.transfer;

import org.apache.commons.collections.Transformer;

import com.clican.pluto.common.inter.SelectItem;


public class SelectItem2StringTransfer implements Transformer {

    public Object transform(Object input) {
        SelectItem si = (SelectItem) input;
        return si.getId() + "," + si.getName();
    }

}
