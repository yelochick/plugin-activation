package com.yelochick;

import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginEntry;

import java.util.Collections;
import java.util.List;

public class ActivationPlugin implements PluginEntry {
    @Override
    public String getName() {
        return "ACTIVATION";
    }

    @Override
    public String getAuthor() {
        return "YeloChick";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return Collections.singletonList(new SmartInputTransformer());
    }
}
