package com.unisoft.algotrader.provider.csv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by alex on 7/20/15.
 */
public class CSVConfig {
    public final String path;

    @Inject
    public CSVConfig(@Named("csv.path")String path){
        this.path = path;
    }

}
