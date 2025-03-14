-- Tắt tạm thời safe update mode
SET SQL_SAFE_UPDATES = 0;

-- Cập nhật các giá trị shipping_method cũ sang mới sử dụng ID
UPDATE orders o
SET o.shipping_method = 'STANDARD' 
WHERE o.shipping_method = 'van chuyen'
AND o.id > 0;

-- Đảm bảo tất cả các giá trị shipping_method đều hợp lệ
ALTER TABLE orders 
ADD CONSTRAINT check_shipping_method 
CHECK (shipping_method IN ('EXPRESS', 'STANDARD', 'SAVING'));

-- Bật lại safe update mode
SET SQL_SAFE_UPDATES = 1; 