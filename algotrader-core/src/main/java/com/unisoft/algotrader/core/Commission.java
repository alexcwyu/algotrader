package com.unisoft.algotrader.core;

import com.unisoft.algotrader.event.execution.Order;

/**
 * Created by alex on 5/25/15.
 */
public abstract class Commission {
    public final double commission;

    public Commission(double commission){
        this.commission = commission;
    }

    public abstract double apply(Transaction transaction);
    public abstract double apply(Order order);

    public static class AbsoluteCommission extends Commission {

        public AbsoluteCommission(double commission){
            super(commission);
        }

        public double apply(Transaction transaction){
            return commission;
        }
        public double apply(Order order){
            return commission;
        }
    }

    public static class PercentCommission extends Commission {

        public PercentCommission(double commission){
            super(commission);
        }

        public double apply(Transaction transaction){
            return commission * transaction.value();
        }
        public double apply(Order order){
            return commission * order.value();
        }
    }

    public static class PerShareCommission extends Commission {

        public PerShareCommission(double commission){
            super(commission);
        }

        public double apply(Transaction transaction){
            return commission * transaction.qty;
        }
        public double apply(Order order){
            return commission * order.filledQty;
        }
    }


}


