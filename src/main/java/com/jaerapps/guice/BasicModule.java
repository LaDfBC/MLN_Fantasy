package com.jaerapps.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.jaerapps.Configuration;

public class BasicModule extends AbstractModule {
    public static final String DATABASE_URL = "databaseUrl";

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named(DATABASE_URL)).to(Configuration.getUrl());
    }
}
