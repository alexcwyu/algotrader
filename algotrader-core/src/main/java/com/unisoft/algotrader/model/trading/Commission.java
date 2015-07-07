package com.unisoft.algotrader.model.trading;

import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 5/25/15.
 */
public abstract class Commission {
    public double commission;

    public Commission(double commission){
        this.commission = commission;
    }

    public abstract double apply(Order order);

    public static class AbsoluteCommission extends Commission {

        public AbsoluteCommission(double commission){
            super(commission);
        }

        public double apply(Order order){
            return commission;
        }
    }

    public static class PercentCommission extends Commission {

        public PercentCommission(double commission){
            super(commission);
        }

        public double apply(Order order){
            return commission * order.value();
        }
    }

    public static class PerShareCommission extends Commission {

        public PerShareCommission(double commission){
            super(commission);
        }

        public double apply(Order order){
            return commission * order.filledQty;
        }
    }


}


