DROP TABLE IF EXISTS `account_balance`;
CREATE TABLE `account_balance`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'balance id',
    account VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'account number',
    balance BIGINT NOT NULL DEFAULT 0 COMMENT 'account balance',
    created_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'account created time',
    modified_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'account modified time',
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_account ON account_balance(account);

DROP TABLE IF EXISTS `transaction_record`;
CREATE TABLE `transaction_record`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'record id',
    transaction_id VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'transaction id',
    source_account VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'source account number',
    destination_account VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'destination account number',
    amount BIGINT NOT NULL DEFAULT 0 COMMENT 'amount',
    created_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'account created time',
    modified_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'account modified time',
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_trans ON transaction_record(transaction_id);



