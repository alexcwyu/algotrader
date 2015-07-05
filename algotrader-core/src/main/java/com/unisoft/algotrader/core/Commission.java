package com.unisoft.algotrader.core;

import com.unisoft.algotrader.event.execution.Order;

/**
 * Created by alex on 5/25/15.
 */
public class Commission {
    public double commission;

    protected Commission(){}

    public Commission(double commission){
        this.commission = commission;
    }

    //public abstract double apply(Transaction transaction);
    public double apply(Order order){
        throw new UnsupportedOperationException();
    }
    public static class AbsoluteCommission extends Commission {

        protected AbsoluteCommission(){}

        public AbsoluteCommission(double commission){
            super(commission);
        }
//
//        public double apply(Transaction transaction){
//            return commission;
//        }
        public double apply(Order order){
            return commission;
        }
    }
    public static class PercentCommission extends Commission {

        protected PercentCommission(){}

        public PercentCommission(double commission){
            super(commission);
        }

//        public double apply(Transaction transaction){
//            return commission * transaction.getValue();
//        }
        public double apply(Order order){
            return commission * order.value();
        }
    }
    public static class PerShareCommission extends Commission {

        protected PerShareCommission(){}

        public PerShareCommission(double commission){
            super(commission);
        }

//        public double apply(Transaction transaction){
//            return commission * transaction.qty;
//        }
        public double apply(Order order){
            return commission * order.filledQty;
        }
    }


}


