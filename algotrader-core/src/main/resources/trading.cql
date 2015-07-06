DROP KEYSPACE trading;

CREATE KEYSPACE IF NOT EXISTS trading WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE trading;


CREATE TYPE account_transaction (
   order_id bigint,
   datetime bigint,
   ccy_id text,
   value double,
   text text
);


CREATE TYPE account_position (
   ccy_id text,
   value double,
   transactions list<frozen<account_transaction>>
);


CREATE TABLE IF NOT EXISTS accounts (
  account_id text,
  name text,
  ccy_id text,
  positions map<text, frozen<account_position>>,
  PRIMARY KEY (account_id)
);



CREATE TYPE IF NOT EXISTS double_time_series (
    datetime_series list<bigint>,
    index_map map<bigint, int>,
    data_series list<double>,
    curr_idx int,
    curr_time bigint
);


CREATE TYPE IF NOT EXISTS performance (
    portfolio_id text,
    enabled boolean,
    equity double,
    core_equity double,
    low_equity double,
    high_equity double,
    pnl double,
    drawdown double,
    drawdown_percent double,
    current_drawdown double,
    current_run_up double,
    equity_series frozen<double_time_series>,
    core_equity_series frozen<double_time_series>,
    pnl_series frozen<double_time_series>,
    drawdown_series frozen<double_time_series>,
    drawdown_percent_series frozen<double_time_series>,
);

CREATE TABLE IF NOT EXISTS portfolios (
    portfolio_id text,
    account_name text,
    performance frozen<performance>,
    PRIMARY KEY (portfolio_id)
);


CREATE TABLE IF NOT EXISTS orders (
    order_id bigint,
    inst_id int,
    date_time bigint,
    ord_type text,
    ord_status text,
    limit_price double,
    stop_price double,
    ord_qty double,
    last_qty double,
    last_price double,
    filled_qty double,
    avg_price double,
    tif text,
    side text,
    text text,
    stop_limit_ready boolean,
    trailing_stop_exec_price double,
    exec_provider_id text,
    portfolio_id text,
    strategy_id text,
    commissions list<double>,
    pnl double,
    realized_pnl double,
    PRIMARY KEY (order_id)
);

CREATE INDEX order_inst_id ON trading.orders (inst_id);
CREATE INDEX order_portfolio_id ON trading.orders (portfolio_id);
CREATE INDEX order_strategy_id ON trading.orders (strategy_id);

CREATE TABLE IF NOT EXISTS execution_reports (
    exec_id bigint,
    order_id bigint,
    inst_id int,
    transaction_time bigint,
    ord_type text,
    ord_status text,
    limit_price double,
    stop_price double,
    ord_qty double,
    last_qty double,
    last_price double,
    filled_qty double,
    avg_price double,
    tif text,
    side text,
    text text,
    PRIMARY KEY (exec_id)
);

CREATE INDEX er_inst_id ON trading.execution_reports (inst_id);
CREATE INDEX er_order_id ON trading.execution_reports (order_id);


