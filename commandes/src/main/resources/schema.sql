-- H2: legacy statut column was a native ENUM without EN_ATTENTE_VALIDATION; convert to VARCHAR for all enum values.
ALTER TABLE IF EXISTS commandes ALTER COLUMN statut SET DATA TYPE VARCHAR(50);
