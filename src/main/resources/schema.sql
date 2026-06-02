CREATE TABLE Status (
                        status_id   INT          PRIMARY KEY AUTO_INCREMENT,
                        status_name VARCHAR(50)  NOT NULL
);

INSERT INTO Status (status_name) VALUES
                                     ('UNDER_REVIEW'),
                                     ('APPROVED'),
                                     ('DENIED'),
                                     ('UNASSIGNED');


CREATE TABLE Account (
                         account_id   INT          PRIMARY KEY AUTO_INCREMENT,
                         name         VARCHAR(100),
                         email        VARCHAR(150),
                         ip_address   VARCHAR(45),
                         iban         VARCHAR(34),
                         phone_number VARCHAR(20),
                         created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Agent (
                       agent_id        INT          PRIMARY KEY AUTO_INCREMENT,
                       name            VARCHAR(100) NOT NULL,
                       email           VARCHAR(150) UNIQUE NOT NULL,
                       password_hash   VARCHAR(255) NOT NULL,
                       role            VARCHAR(20)  NOT NULL DEFAULT 'ANALYST',
                       profile_picture LONGTEXT
);


-- Compatibility table for DAO (lowercase 'agent' with different column names)
CREATE TABLE IF NOT EXISTS agent (
    agentId INT PRIMARY KEY AUTO_INCREMENT,
    agentName VARCHAR(100),
    agentEmail VARCHAR(150)
);


CREATE TABLE Transactions (
                              transaction_id    INT            PRIMARY KEY AUTO_INCREMENT,
                              agent_id          INT            DEFAULT NULL,
                              account_id        INT            NOT NULL,
                              status_id         INT            NOT NULL DEFAULT 4,
                              amount            DECIMAL(10, 2) NOT NULL,
                              currency          VARCHAR(10),
                              risk_score        DECIMAL(5, 2)  DEFAULT 0,
                              reason_text       TEXT           DEFAULT NULL,
                               agent_note        TEXT           DEFAULT NULL,
                              order_id          VARCHAR(50)    DEFAULT NULL,
                              order_date        DATE           DEFAULT NULL,
                              items             TEXT           DEFAULT NULL,
                              payment_method    VARCHAR(100)   DEFAULT NULL,
                              shipping_address  TEXT           DEFAULT NULL,
                              billing_address   TEXT           DEFAULT NULL,
                              created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_transaction_agent
                                  FOREIGN KEY (agent_id) REFERENCES Agent(agent_id)
                                      ON DELETE SET NULL,

                              CONSTRAINT fk_transaction_account
                                  FOREIGN KEY (account_id) REFERENCES Account(account_id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT fk_transaction_status
                                  FOREIGN KEY (status_id) REFERENCES Status(status_id)
                                      ON DELETE RESTRICT
);


CREATE TABLE Reason (
                        reason_id   INT          PRIMARY KEY AUTO_INCREMENT,
                        reason_name VARCHAR(100) NOT NULL,
                        description TEXT
);


CREATE TABLE Transaction_Reason (
                                    transaction_id INT NOT NULL,
                                    reason_id      INT NOT NULL,

                                    PRIMARY KEY (transaction_id, reason_id),

                                    CONSTRAINT fk_tr_transaction
                                        FOREIGN KEY (transaction_id) REFERENCES Transactions(transaction_id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT fk_tr_reason
                                        FOREIGN KEY (reason_id) REFERENCES Reason(reason_id)
                                            ON DELETE RESTRICT
);

-- Sequences are reset by data.sql after all explicit inserts.
