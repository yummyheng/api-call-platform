-- Insert test APIRequest entry for card trade history API
INSERT INTO api_request (app_id, project_id, api_name, api_url, http_method, request_headers, request_body, description, created_by)
VALUES ('APP_001', 1, '获取卡片交易历史', 'http://localhost:8080/api/card/trade-history/all', 'GET', '{"Content-Type": "application/json"}', '', '查询所有卡片交易历史', 'admin');

-- Insert test ApiTestCase entry for the API
INSERT INTO api_test_case (api_id, test_case_name, test_data, expected_result, is_enabled, created_by)
VALUES (1, '测试获取所有交易历史', '', '{"success": true}', true, 'admin');