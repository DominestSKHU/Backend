ALTER TABLE complaint
ADD FULLTEXT INDEX ft_idx_complaint_text (complaint_cause, complaint_resolution);