package com.unisoft.algotrader.provider.ib.api.event;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 8/26/15.
 */
public class ManagedAccountListEvent extends IBEvent<ManagedAccountListEvent>  {

    public static final char SEPARATOR = ',';
    public final String commaSeparatedAccountList;
    public final Function<String, String> toTrimmedStringFunction = new Function<String, String>() {
        @Override
        public String apply(final String input) {
            return StringUtils.trim(input);
        }
    };

    public ManagedAccountListEvent(final String commaSeparatedAccountList) {
        super();
        this.commaSeparatedAccountList = commaSeparatedAccountList;
    }

    public List<String> getAccounts() {
        return Collections.unmodifiableList(Lists.transform(
                Lists.newArrayList(StringUtils.split(commaSeparatedAccountList, SEPARATOR)), toTrimmedStringFunction));
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onManagedAccountListEvent(this);
    }

    @Override
    public String toString() {
        return "ManagedAccountListEvent{" +
                "commaSeparatedAccountList='" + commaSeparatedAccountList + '\'' +
                ", toTrimmedStringFunction=" + toTrimmedStringFunction +
                "} " + super.toString();
    }
}