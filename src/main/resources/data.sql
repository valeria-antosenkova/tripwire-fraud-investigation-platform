-- ============================================================
--  Tripwire — Case 1 seed (high-risk fraud)

-- ============================================================


-- ----------------------------------------------------------
-- 1. Reason
-- ----------------------------------------------------------
INSERT INTO Reason (reason_id, reason_name, description)
VALUES (1, 'Not received', 'Buyer claims the item never arrived.');

-- ----------------------------------------------------------
-- 2. Agent 
-- ----------------------------------------------------------
INSERT INTO Agent (agent_id, name, email, password_hash)
VALUES (
           1,
           'Sarah Okafor',
           'sarah.okafor@tripwire.internal',
           '{noop}tripwire123'
       );

-- ----------------------------------------------------------
-- 3. Account  (the fraudster)
--    ip   : 45.95.147.19  → Alsycon B.V., Dutch VPS/hosting
--                           (not residential; flagged range)
--    email: disposable guerrillamail throwaway
--    phone: Brazilian (+55) — mismatches the NL hosting IP
--    iban : German — mismatches phone country (BR) and IP (NL)
-- ----------------------------------------------------------
INSERT INTO Account (account_id, name, email, ip_address, iban, phone_number, created_at)
VALUES (
           1,
           'Alex Turner',
           'urgentclaim992@guerrillamail.com',
           '45.95.147.19',
           'DE75512108001245126199',
           '+5511998877665',
           '2026-05-28 01:47:00'   -- account created ~3 hours before the refund
       );

-- ----------------------------------------------------------
-- 4. Transaction  (the refund case)
--    status_id 4 = UNASSIGNED (no agent yet, freshly ingested)
--    agent_id  NULL → not yet assigned
--    risk_score will be calculated by your scoring engine;
--               set to 0 here so your engine has something to
--               UPDATE once it runs.
-- ----------------------------------------------------------
INSERT INTO Transactions (
    transaction_id, agent_id, account_id, status_id,
    amount, currency, risk_score, reason_text,
    order_id, order_date, items, payment_method,
    shipping_address, billing_address, created_at
)
VALUES (
           1,
           NULL,
           1,
           4,
           3499.00,
           'USD',
           0.00,
           'I placed this order 3 days ago and it still has not arrived. The tracking page shows no updates since the label was created. I need this refunded immediately as I required the item urgently.',
           'ORD-10001',
           '2026-05-25',
           '["MacBook Pro 16-inch"]',
           'Credit Card (**** 4829)',
           '456 Oak Ave, Miami, FL 33101',
           '22 Pine Rd, Chicago, IL 60601',
           '2026-05-28 04:52:00'
       );

-- ----------------------------------------------------------
-- 5. Transaction_Reason  (links transaction → reason)
-- ----------------------------------------------------------
INSERT INTO Transaction_Reason (transaction_id, reason_id)
VALUES (1, 1);

-- ----------------------------------------------------------
--  What your scoring engine should see when it reads this row
--  (for reference — not stored anywhere yet):
--
--  account_age_hours  : ~3
--  ip_org             : Alsycon B.V. (VPS/hosting, NL)
--  ip_type            : datacenter
--  email_domain       : guerrillamail.com (disposable)
--  phone_country      : BR
--  iban_country       : DE
--  ip_country         : NL
--  payout_country     : DE  ← differs from phone (BR) and IP (NL)
--  order_to_refund_h  : ~3
--  amount             : $3,499 (high value, first and only order)
--
--  Expected signals (what Dymo + your engine should compute):
--    Email risk              → 10  (guerrillamail = disposable)
--    IP risk                 → 9   (Alsycon VPS, flagged range)
--    Account age             → 10  (3 hours old)
--    Order-to-refund timing  → 10  (filed 3h after order)
--    Phone-country mismatch  → 9   (BR phone, NL IP, DE IBAN)
--    Payment dest. change    → 9   (IBAN country ≠ phone country)
--    Amount anomaly          → 8   ($3,499 >> cohort p95)
--    Refund velocity         → 7   (1 refund on 3h-old account)
--
--  Expected overall score  : 92–97 / HIGH / DENY


-- ============================================================
--  Tripwire — Case 6 Disposable Identity
-- ============================================================



-- 3. Account
--    ip      : 185.220.101.42 — confirmed Tor exit node (Fraunhofer FKIE list)
--    email   : guerrillamail throwaway
--    phone   : UK (+44) — mismatches Tor IP which has no consistent geo
--    iban    : GB94BARC... — payout destination, different from payment method
--    created : 2026-05-27 21:30 — account is ~6h old when refund is filed
INSERT INTO Account (account_id, name, email, ip_address, iban, phone_number, created_at)
VALUES (
           2,
           'Sam Riley',
           'xk82jd@guerrillamail.com',
           '185.220.101.42',
           'GB94BARC10201530093459',
           '+447911123456',
           '2026-05-27 21:30:00'
       );

-- 4. Transaction
--    order placed  : 22:10 (40 min after account created)
--    refund filed  : 04:18 next day (off-hours, 6h after order)
--    status_id 4   : UNASSIGNED
--    risk_score 0  : your engine computes and UPDATEs this
INSERT INTO Transactions (
    transaction_id, agent_id, account_id, status_id,
    amount, currency, risk_score, reason_text,
    order_id, order_date, items, payment_method,
    shipping_address, billing_address, created_at
)
VALUES (
           2,
           NULL,
           2,
           4,
           4750.00,
           'USD',
           0.00,
           'My package was marked as delivered but I never received it. I checked with my neighbours and no one has it. Please issue a full refund.',
           'ORD-20001',
           '2026-05-26',
           '["Sony WH-1000XM5", "USB-C Hub"]',
           'Credit Card (**** 3311)',
           '789 Elm Blvd, Denver, CO 80201',
           '789 Elm Blvd, Denver, CO 80201',
           '2026-05-28 04:18:00'
       );

-- 5. Transaction_Reason
INSERT INTO Transaction_Reason (transaction_id, reason_id)
VALUES (2, 1);

-- ============================================================
--  Scoring engine reference — what to expect when you run it:
--
--  Raw facts your engine reads:
--    account_age_hours       : 6.8
--    ip                      : 185.220.101.42 (Tor exit node)
--    email_domain            : guerrillamail.com (disposable)
--    phone_country           : GB
--    iban_country            : GB  (matches phone but irrelevant —
--                              original payment was card, not IBAN)
--    payout_differs          : YES (card → new IBAN)
--    order_placed_at         : 2026-05-27 22:10
--    refund_filed_at         : 2026-05-28 04:18
--    order_to_refund_hours   : 6.1
--    filed_hour_utc          : 4 (off-hours)
--    total_orders            : 1
--    total_refunds           : 1
--    amount                  : $4,750
--
--  Expected signals:
--    IP risk (Tor exit)            → 10
--    Email risk (disposable)       → 10
--    Account age (6.8h)            → 10
--    Order-to-refund timing (6h)   → 10
--    Payment destination change    → 9
--    Amount anomaly                → 9
--    Time-of-day anomaly (4am)     → 8
--    Refund velocity               → 7
--    Phone-country mismatch        → 4  (phone GB, Tor = no geo)
--
--  Expected overall score : 95–99 / HIGH / DENY
-- ============================================================


-- ============================================================
--  Tripwire — Case 10 seed (identity mismatch / geo fraud)

-- ============================================================

-- 1. Reasons  (two reasons for this case)
INSERT INTO Reason (reason_id, reason_name, description)
VALUES (2, 'Damaged on arrival', 'Item arrived in damaged condition.');

-- 2. Agent
INSERT INTO Agent (agent_id, name, email, password_hash)
VALUES (2, 'Vanessa Lee', 'vanessa_lee@internal.com', '{noop}tripwire123');

-- 3. Account
--    email   : outlook.com — clean domain, no disposable flag
--    ip      : 118.200.44.91 — Singtel Fibre Broadband, residential SG
--    phone   : +5511912345678 — Brazilian mobile (+55 11)
--    iban    : SG29DBS... — DBS Bank Singapore payout
--    created : 8 months ago (established account, not new)
INSERT INTO Account (account_id, name, email, ip_address, iban, phone_number, created_at)
VALUES (
           3,
           'Maria Santos',
           'maria.santos99@outlook.com',
           '118.200.44.91',
           'SG29DBS80027312345678',
           '+6511912345',
           '2025-09-18 10:22:00'
       );

-- 4. Transactions  (3 total — history + current)
INSERT INTO Transactions (
    transaction_id, agent_id, account_id, status_id,
    amount, currency, risk_score, reason_text,
    order_id, order_date, items, payment_method,
    shipping_address, billing_address, created_at
)
VALUES
    (3, 1, 3, 2,  124.00, 'USD', 0.0,
     'The item I received is not the one I ordered. The size is completely wrong and the colour is different from the product page.',
     'ORD-30001', '2026-05-11', '["Nike Air Max 270", "Running Socks 3-pack"]', 'Credit Card (**** 5577)',
     '88 Orchard Rd, Singapore 238839', '88 Orchard Rd, Singapore 238839',
     '2026-05-18 11:05:00'),

    (4, 1, 3, 1,  310.00, 'USD', 0.0,
     '{"text":"The package arrived with visible damage to the box and the product inside was broken. I have taken photos as evidence of the damage. Please see the attached images and process a full refund or replacement as soon as possible.","images":["/attachments/case-4/damage-box.jpg","/attachments/case-4/damage-screen.png"]}',
     'ORD-30002', '2026-05-20', '["iPad Pro 12.9-inch", "Apple Pencil", "Smart Keyboard Folio"]', 'Credit Card (**** 5577)',
     '88 Orchard Rd, Singapore 238839', '88 Orchard Rd, Singapore 238839',
     '2026-05-24 09:40:00'),

    (5, NULL, 3, 4,  660.00, 'USD', 0.0,
     'Another item from you that arrived damaged. The screen is cracked and the casing is dented. I am very disappointed and want a refund right away.',
     'ORD-30003', '2026-05-24', '["LG OLED Monitor 27-inch", "Ergonomic Mouse"]', 'Credit Card (**** 5577)',
     '88 Orchard Rd, Singapore 238839', '88 Orchard Rd, Singapore 238839',
     '2026-05-28 05:07:00');

-- 5. Transaction_Reason links
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (3, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (4, 2);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (5, 2);

-- ============================================================
--  Scoring engine reference for transaction_id = 3
--
--  Raw facts:
--    account_age_days        : ~252  (8 months)
--    total_orders            : 9
--    total_refunds           : 3  (including this one)
--    refunds_last_7d         : 2  (tx 2 + tx 3)
--    refund_ratio            : 0.33  (3/9)
--    cohort_avg_ratio        : 0.07
--    ip_country              : SG  (Singtel residential)
--    phone_country           : BR  (+55)
--    account_country         : US
--    iban_country            : SG  (DBS Bank)
--    payout_differs          : YES (card → new IBAN)
--    filed_hour_utc          : 5   (off-hours)
--    amount_escalation       : $124 → $310 → $660 (doubling)
--
--  Three-country mismatch:
--    phone = BR,  IP = SG,  account = US
--    All three point to different places
--
--  Expected signals:
--    Phone-country mismatch (BR/SG/US)   → 9
--    Payment destination change           → 8
--    IBAN country matches IP not account  → 7
--    Refund velocity (2 in 7d)            → 7
--    Refund-to-purchase ratio (0.33)      → 8
--    Amount escalation ($124→$310→$660)   → 7
--    Time-of-day anomaly (5am)            → 6
--    IP risk (residential, clean)         → 1
--    Email risk (outlook.com)             → 1
--
--  Expected overall score : 62–74 / MEDIUM-HIGH / REVIEW or DENY
-- ============================================================


-- ============================================================
--  Tripwire — Case 4 seed (new account burst / smash-and-grab)
-- ============================================================

-- 2. Agent
INSERT INTO Agent (agent_id, name, email, password_hash)
VALUES (3, 'John Smith', 'john.smith@tripwire.internal', '{noop}tripwire123');

-- 3. Account
--    ip     : 46.161.11.11 — Petersburg Internet Network Ltd., RU
--    email  : mail.ru domain — elevated fraud association
--    phone  : +79161234567 — Russian mobile (MTS, +7 916 prefix)
--    iban   : DE89370400440532013000 — German payout, mismatches RU identity
--    created: 6 days ago
INSERT INTO Account (account_id, name, email, ip_address, iban, phone_number, created_at)
VALUES (
           4,
           'Ivan Petrov',
           'temp.user8821@mail.ru',
           '46.161.11.11',
           'DE89370400440532013000',
           '+79161234567',
           '2026-05-22 19:00:00'
       );

-- 4. Transactions — 4 orders in 6 days, 3 refunded
INSERT INTO Transactions (
    transaction_id, agent_id, account_id, status_id,
    amount, currency, risk_score, reason_text,
    order_id, order_date, items, payment_method,
    shipping_address, billing_address, created_at
)
VALUES
    (6, 3, 4, 2,  195.00, 'EUR', 0.0,
     'I never received my order. It has been two weeks with no updates on the tracking. Please refund the full amount.',
     'ORD-40001', '2026-05-20', '["Samsung 55-inch QLED TV", "HDMI 2.1 Cable"]', 'Credit Card (**** 8821)',
     '123 Main St, Austin, TX 78701', '54 River Dr, Atlanta, GA 30301',
     '2026-05-22 20:14:00'),

    (7, 1, 4, 2,  340.00, 'EUR', 0.00,
     'Order still not delivered after 10 days. Shipping carrier says it is lost. This is the second time this happens. I want my money back now.',
     'ORD-40002', '2026-05-22', '["iPhone 15 Pro", "Apple Watch Series 9"]', 'Credit Card (**** 8821)',
     '123 Main St, Austin, TX 78701', '54 River Dr, Atlanta, GA 30301',
     '2026-05-24 11:30:00'),

    (8, 2, 4, 1,  480.00, 'EUR', 0.00,
     'Another lost parcel. The delivery company confirmed the package is missing. I have been waiting 3 weeks and I need an immediate refund.',
     'ORD-40003', '2026-05-24', '["MacBook Pro 16-inch", "AirPods Pro"]', 'Credit Card (**** 8821)',
     '123 Main St, Austin, TX 78701', '54 River Dr, Atlanta, GA 30301',
     '2026-05-26 16:55:00'),

    (9, NULL, 4, 4, 620.00, 'EUR', 0.00,
     'Order has not arrived after 14 days and the tracking has not updated in a week. Please process my refund as soon as possible.',
     'ORD-40004', '2026-05-26', '["iPad Pro 12.9-inch", "Apple Pencil"]', 'Credit Card (**** 8821)',
     '123 Main St, Austin, TX 78701', '54 River Dr, Atlanta, GA 30301',
     '2026-05-28 08:22:00');

-- 5. Transaction_Reason links (all four use reason 1 = Not received)
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (6, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (7, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (8, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (9, 1);

-- ============================================================
--  Scoring engine reference for transaction_id = 4
--
--  Raw facts:
--    account_age_days          : 6
--    total_orders              : 4
--    total_refunds             : 3  (tx1, tx2, tx3 + this one = 4th)
--    refunds_last_30d          : 4  (all within 6 days)
--    refund_ratio              : 0.75  (3 of 4 already refunded)
--    cohort_avg_ratio          : 0.07
--    times_used_this_reason    : 4  ("Not received" all 4 times)
--    ip_org                    : Petersburg Internet Network Ltd (hosting, RU)
--    ip_abuse_reports          : 989 on AbuseIPDB
--    email_domain              : mail.ru (elevated fraud association)
--    phone_country             : RU (+7 916)
--    iban_country              : DE  (mismatches RU identity)
--    payout_differs            : YES (card → German IBAN)
--    amount_escalation         : €195 → €340 → €480 → €620
--    filed_hour_utc            : 8   (normal hours — clean signal)
--
--  Expected signals:
--    Refund velocity (4 in 6d)            → 10
--    Refund-to-purchase ratio (0.75)      → 10
--    Account age (6 days)                 → 9
--    Refund reason repetition (4x same)   → 9
--    Amount escalation (€195→€620)        → 8
--    Payment destination change           → 8
--    IP risk (hosting range, 989 reports) → 8
--    Email risk (mail.ru)                 → 6
--    Phone-country vs IBAN mismatch       → 6
--    Time-of-day anomaly                  → 0  (8am = normal)
--
--  Expected overall score : 88–96 / HIGH / DENY
-- ============================================================


-- ============================================================
--  Tripwire — Case 7 seed (refund abuse / policy exploitation)
--
--  Identity is completely clean — risk comes entirely from
--  claim pattern: 8 escalating "not received" claims over 3 months.
-- ============================================================

-- 3. Account
--    email   : gmail.com — clean, no disposable flag
--    ip      : 86.9.44.123 — BT Broadband, residential UK
--    phone   : +447532109876 — UK mobile (EE network)
--    iban    : GB29NWBK60161331926819 — NatWest UK payout
--    phone_country GB = iban_country GB → identity MATCHES (clean signal)
--    created : 2025-09-15 — 8+ months old (established account)
INSERT INTO Account (account_id, name, email, ip_address, iban, phone_number, created_at)
VALUES (
           5,
           'Alex Morgan',
           'alex.morgan.returns@gmail.com',
           '86.9.44.123',
           'GB29NWBK60161331926819',
           '+447532109876',
           '2025-09-15 14:22:00'
       );

-- 4. Transactions — 8 claims in 3 months, all "not received", escalating amounts
INSERT INTO Transactions (
    transaction_id, agent_id, account_id, status_id,
    amount, currency, risk_score, reason_text,
    order_id, order_date, items, payment_method,
    shipping_address, billing_address, created_at
)
VALUES
    (10, 1, 5, 2,  45.00, 'GBP', 0.0,
     'I ordered this item over two weeks ago and it still has not arrived. Tracking shows no movement. Please issue a refund.',
     'ORD-50001', '2026-02-18', '["Phone Case", "Screen Protector"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-03-02 10:14:00'),

    (11, 1, 5, 2,  89.00, 'GBP', 0.0,
     'My parcel has not been delivered. I have waited patiently but the tracking has not updated in over a week. I need a refund.',
     'ORD-50002', '2026-03-05', '["Bluetooth Speaker"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-03-18 11:30:00'),

    (12, 2, 5, 2, 127.00, 'GBP', 0.0,
     'Another order that never arrived. The carrier website just says dispatched with no further updates. This is unacceptable, please refund.',
     'ORD-50003', '2026-03-19', '["Smart Watch Strap", "Wireless Charger"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-04-01 09:52:00'),

    (13, 2, 5, 2, 203.00, 'GBP', 0.0,
     'Item not received after 10 days. No delivery attempt was made at my address. Neighbours confirm nothing was left. Full refund required.',
     'ORD-50004', '2026-04-03', '["Mechanical Keyboard"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-04-14 14:07:00'),

    (14, 1, 5, 2, 298.00, 'GBP', 0.0,
     'Once again my order has not arrived. Tracking stopped updating after the label was created. I should not keep having this problem. Please refund immediately.',
     'ORD-50005', '2026-04-19', '["Sony WH-1000XM5"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-04-28 08:45:00'),

    (15, 3, 5, 2, 450.00, 'GBP', 0.0,
     'Package still not received after two weeks. I have contacted the courier and they have no record of a delivery attempt. I want my money back.',
     'ORD-50006', '2026-04-28', '["iPad Mini"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-05-09 16:20:00'),

    (16, 3, 5, 1, 612.00, 'GBP', 0.0,
     'My item has not been delivered. This is the same issue I keep experiencing with orders from your store. Tracking is stuck at label created. Please refund the full amount as soon as possible.',
     'ORD-50007', '2026-05-13', '["iPhone 15"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-05-21 13:55:00'),

    (17, NULL, 5, 4, 780.00, 'GBP', 0.0,
     'Order placed 12 days ago and still not received. The tracking has not moved since the label was printed. I have been a customer for a while now and this keeps happening. I expect a full refund.',
     'ORD-50008', '2026-05-19', '["MacBook Air 13-inch"]', 'Credit Card (**** 6624)',
     '10 Cedar Ct, London EC1A 1BB', '10 Cedar Ct, London EC1A 1BB',
     '2026-05-28 11:30:00');

-- 5. Transaction_Reason links (all eight use reason 1 = Not received)
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (10, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (11, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (12, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (13, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (14, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (15, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (16, 1);
INSERT INTO Transaction_Reason (transaction_id, reason_id) VALUES (17, 1);

-- Reset auto-increment past all manually inserted IDs
ALTER TABLE Transactions ALTER COLUMN transaction_id RESTART WITH 18;
ALTER TABLE Account      ALTER COLUMN account_id     RESTART WITH 6;
ALTER TABLE Agent        ALTER COLUMN agent_id        RESTART WITH 4;

-- ============================================================
--  Scoring engine reference for transaction_id = 17
--
--  Identity signals (all CLEAN):
--    email_domain   : gmail.com (clean)
--    ip_org         : BT Broadband (residential UK, clean)
--    phone_country  : GB (+44) — MATCHES iban_country GB
--    account_age    : 8.5 months
--
--  Behavioural signals (all HIGH):
--    total_claims          : 8  (all "not received")
--    claims_last_90d       : 6  (tx 12–17)
--    amount_escalation     : £45→£89→£127→£203→£298→£450→£612→£780
--    repeated_reason       : "not received" used 8/8 times
--    prior_approvals       : 6 claims approved
--
--  Expected overall score : 55–72 / MEDIUM-HIGH / REVIEW or DENY
-- ============================================================

