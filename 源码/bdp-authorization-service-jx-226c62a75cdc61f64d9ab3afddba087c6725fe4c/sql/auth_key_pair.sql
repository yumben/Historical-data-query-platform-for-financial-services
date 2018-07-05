/*
Navicat PGSQL Data Transfer

Source Server         : 10.0.118.84-测试
Source Server Version : 90604
Source Host           : 10.0.118.84:5432
Source Database       : bdp_basic
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90604
File Encoding         : 65001

Date: 2018-02-05 17:39:43
*/


-- ----------------------------
-- Table structure for auth_key_pair
-- ----------------------------
DROP TABLE IF EXISTS "public"."auth_key_pair";
CREATE TABLE "public"."auth_key_pair" (
"system_code" varchar(255) COLLATE "default",
"public_key" text COLLATE "default",
"private_key" text COLLATE "default",
"create_time" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."auth_key_pair"."system_code" IS '系统代码';
COMMENT ON COLUMN "public"."auth_key_pair"."public_key" IS '公钥';
COMMENT ON COLUMN "public"."auth_key_pair"."private_key" IS '密钥';
COMMENT ON COLUMN "public"."auth_key_pair"."create_time" IS '创建时间';

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
