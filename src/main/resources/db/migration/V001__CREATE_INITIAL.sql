CREATE TABLE gig (
                     id BIGINT AUTO_INCREMENT NOT NULL,
                     gig_id VARCHAR(255) NOT NULL,
                     max_price numeric(8, 2) NULL,
                     price numeric(8, 2) NULL,
                     price_token DOUBLE NULL,
                     title VARCHAR(255) NOT NULL,
                     `description` LONGTEXT NOT NULL,
                     execution_type VARCHAR(100) NOT NULL,
                     status VARCHAR(100) NOT NULL,
                     executionpool VARCHAR(255) NULL,
                     is_active BIT(1) NULL,
                     data_owner_id BIGINT NULL,
                     date_created timestamp NOT NULL,
                     last_updated timestamp NOT NULL,
                     CONSTRAINT PK_GIG PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE offer (
                       id BIGINT AUTO_INCREMENT NOT NULL,
                       accepted BIT(1) NULL,
                       price numeric(8, 2) NOT NULL,
                       price_token DOUBLE NOT NULL,
                       `description` LONGTEXT NOT NULL,
                       is_active BIT(1) NULL,
                       gig_id BIGINT NOT NULL,
                       specialist_id BIGINT NOT NULL,
                       date_created timestamp NOT NULL,
                       last_updated timestamp NOT NULL,
                       CONSTRAINT PK_OFFER PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE contract (
                          id BIGINT AUTO_INCREMENT NOT NULL,
                          signature_rdo BIT(1) NOT NULL,
                          signature_ds BIT(1) NOT NULL,
                          aggregated_json LONGTEXT NOT NULL,
                          transaction_id VARCHAR(255) NULL,
                          blockchain_identifier VARCHAR(255) NULL,
                          is_active BIT(1) NULL,
                          offer_id BIGINT NOT NULL,
                          date_created timestamp NOT NULL,
                          last_updated timestamp NOT NULL,
                          CONSTRAINT PK_CONTRACT PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE sla_statement (
                               id BIGINT AUTO_INCREMENT NOT NULL,
                               subject VARCHAR(255) NOT NULL,
                               restriction LONGTEXT NOT NULL,
                               is_active BIT(1) NULL,
                               gig_id BIGINT NOT NULL,
                               date_created timestamp NOT NULL,
                               last_updated timestamp NOT NULL,
                               CONSTRAINT PK_SLA_STATEMENT PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE skillset (
                          id BIGINT AUTO_INCREMENT NOT NULL,
                          sklillset_id VARCHAR(255) NOT NULL,
                          gig_id BIGINT NOT NULL,
                          date_created timestamp NOT NULL,
                          last_updated timestamp NOT NULL,
                          CONSTRAINT PK_SKILLSET PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE data_owner (
                            id BIGINT AUTO_INCREMENT NOT NULL,
                            uid char(36) NOT NULL,
                            date_created timestamp NOT NULL,
                            last_updated timestamp NOT NULL,
                            CONSTRAINT PK_DATA_OWNER PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE specialist (
                            id BIGINT AUTO_INCREMENT NOT NULL,
                            uid char(36) NOT NULL,
                            date_created timestamp NOT NULL,
                            last_updated timestamp NOT NULL,
                            CONSTRAINT PK_SPECIALIST PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE candidate_specialist (
                                      id BIGINT AUTO_INCREMENT NOT NULL,
                                      uid char(36) NOT NULL,
                                      date_created timestamp NOT NULL,
                                      last_updated timestamp NOT NULL,
                                      CONSTRAINT PK_CANDIDATE_SPECIALIST PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE gig_chosen_specialist (
                                       gig_id BIGINT NOT NULL,
                                       candidate_specialist_id BIGINT NOT NULL
);

ALTER TABLE gig ADD CONSTRAINT fk_gig_data_owner_id FOREIGN KEY (data_owner_id) REFERENCES data_owner (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE gig ADD CONSTRAINT unique_gig_gig_id UNIQUE (gig_id);

ALTER TABLE offer ADD CONSTRAINT fk_offer_gig_id FOREIGN KEY (gig_id) REFERENCES gig (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE offer ADD CONSTRAINT fk_offer_specialist_id FOREIGN KEY (specialist_id) REFERENCES specialist (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE contract ADD CONSTRAINT fk_contract_offer_id FOREIGN KEY (offer_id) REFERENCES offer (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE sla_statement ADD CONSTRAINT fk_sla_statement_gig_id FOREIGN KEY (gig_id) REFERENCES gig (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE skillset ADD CONSTRAINT fk_skillset_gig_id FOREIGN KEY (gig_id) REFERENCES gig (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE skillset ADD CONSTRAINT unique_skillset_sklillset_id UNIQUE (sklillset_id);

ALTER TABLE data_owner ADD CONSTRAINT unique_data_owner_uid UNIQUE (uid);

ALTER TABLE specialist ADD CONSTRAINT unique_specialist_uid UNIQUE (uid);

ALTER TABLE candidate_specialist ADD CONSTRAINT unique_candidate_specialist_uid UNIQUE (uid);

ALTER TABLE gig_chosen_specialist ADD PRIMARY KEY (gig_id, candidate_specialist_id);

ALTER TABLE gig_chosen_specialist ADD CONSTRAINT fk_gig_chosen_specialist_gig_id FOREIGN KEY (gig_id) REFERENCES gig (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE gig_chosen_specialist ADD CONSTRAINT fk_gig_chosen_specialist_candidate_specialist_id FOREIGN KEY (candidate_specialist_id) REFERENCES candidate_specialist (id) ON UPDATE NO ACTION ON DELETE NO ACTION;


