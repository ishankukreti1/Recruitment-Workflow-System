-- Run this in MySQL to create the job_portal database and tables

CREATE DATABASE IF NOT EXISTS job_portal;
USE job_portal;

CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL  -- 'CANDIDATE' or 'RECRUITER'
);

CREATE TABLE IF NOT EXISTS jobs (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    recruiter_id INT NOT NULL,
    FOREIGN KEY (recruiter_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS applications (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    job_id  INT NOT NULL,
    status  VARCHAR(20) NOT NULL DEFAULT 'APPLIED',  -- APPLIED, SHORTLISTED, INTERVIEW, SELECTED, REJECTED
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (job_id)  REFERENCES jobs(id),
    UNIQUE KEY no_duplicate (user_id, job_id)         -- prevent duplicate applications
);
