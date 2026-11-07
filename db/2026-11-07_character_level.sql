-- Level của nhân vật. Số điểm cộng được tính theo level:
--   tổng điểm = start-points + (level - 1) * points-per-level   (cấu hình trong application.yml)
-- Điểm còn lại = tổng điểm - tổng điểm đã cộng (character_stat_point), không lưu.
--
-- Chạy: psql -h localhost -U postgres -d treasure_hunter -f db/2026-11-07_character_level.sql
-- Sau đó gen lại jOOQ: mvnw generate-sources

BEGIN;

ALTER TABLE public."character"
    ADD COLUMN level integer NOT NULL DEFAULT 1,
    ADD CONSTRAINT chk_character_level CHECK (level >= 1);

COMMIT;
