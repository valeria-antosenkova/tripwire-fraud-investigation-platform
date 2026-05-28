-- ============================================================
--  Tripwire — Case 1 seed (high-risk fraud)
--  Run AFTER your CREATE DATABASE / CREATE TABLE script.
--  Insert order respects FK constraints.
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
           '$2b$12$KIXQz3Jv0Nf6kLmPqRtOuO3n8wYsZxBvDcEfGhIjKlMnOpQrStUv'
       );

-- ----------------------------------------------------------
-- 3. Account  (the fraudster)
--    ip   : 45.95.147.19  → Alsycon B.V., Dutch VPS/hosting
--                           (not residential; flagged range)
--    email: disposable guerrillamail throwaway
--    phone: Brazilian (+55) — mismatches the NL hosting IP
--    iban : German — mismatches phone country (BR) and IP (NL)
-- ----------------------------------------------------------
INSERT INTO Account (account_id, email, ip_address, iban, phone_number, created_at)
VALUES (
           1,
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
    amount, currency, risk_score, reason_text, created_at
)
VALUES (
           1,
           NULL,
           1,
           4,
            3499.00,
           'USD',
           0.00,                          -- your engine fills this in
           'Not received',
           '2026-05-28 04:52:00'          -- ~3h after account was created
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