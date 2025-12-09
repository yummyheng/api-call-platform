-- Create api_request table
CREATE TABLE IF NOT EXISTS api_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_id VARCHAR(50) NOT NULL,
    project_id BIGINT NOT NULL,
    api_name VARCHAR(100) NOT NULL,
    api_url VARCHAR(255) NOT NULL,
    http_method VARCHAR(10) NOT NULL,
    request_headers TEXT,
    request_body TEXT,
    description TEXT,
    created_by VARCHAR(50) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_time DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create api_test_case table
CREATE TABLE IF NOT EXISTS api_test_case (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    api_id BIGINT NOT NULL,
    test_case_name VARCHAR(100) NOT NULL,
    test_data TEXT,
    expected_result TEXT,
    is_enabled BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(50) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (api_id) REFERENCES api_request(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;