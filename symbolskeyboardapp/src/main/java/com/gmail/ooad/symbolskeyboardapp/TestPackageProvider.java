package com.gmail.ooad.symbolskeyboardapp;

import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 16.05.18.
 */
public class TestPackageProvider implements ISymbolsPackagesProvider{
    @Override
    public ArrayList<ISymbolsPackage> getPackages() {
        return new ArrayList<ISymbolsPackage>() {{
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
            add(new TestPackage());
        }};
    }
}
