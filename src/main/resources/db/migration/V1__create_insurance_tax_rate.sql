CREATE TABLE insurance_tax_rate (
                                    id UUID PRIMARY KEY,

                                    insurance_category VARCHAR(30) NOT NULL,
                                    tax_type VARCHAR(20) NOT NULL,

                                    rate DECIMAL(10, 6) NOT NULL,

                                    valid_from TIMESTAMP NOT NULL,

                                    created_by VARCHAR(100) NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT uk_tax_rate_version
                                        UNIQUE (insurance_category, tax_type, valid_from),

                                    CONSTRAINT chk_tax_rate_non_negative
                                        CHECK (rate >= 0)
);

CREATE INDEX idx_tax_rate_lookup
    ON insurance_tax_rate (
                           insurance_category,
                           tax_type,
                           valid_from
        );