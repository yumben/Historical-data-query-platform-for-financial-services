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

Date: 2018-02-05 17:39:50
*/


-- ----------------------------
-- Table structure for auth_key_pair_his
-- ----------------------------
DROP TABLE IF EXISTS "public"."auth_key_pair_his";
CREATE TABLE "public"."auth_key_pair_his" (
"system_code_his" varchar(255) COLLATE "default",
"public_key_his" text COLLATE "default",
"private_key_his" text COLLATE "default",
"create_time_his" varchar(255) COLLATE "default",
"user_id_his" varchar(255) COLLATE "default",
"data_his" text COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."auth_key_pair_his"."system_code_his" IS '系统代码';
COMMENT ON COLUMN "public"."auth_key_pair_his"."public_key_his" IS '公钥';
COMMENT ON COLUMN "public"."auth_key_pair_his"."private_key_his" IS '密钥';
COMMENT ON COLUMN "public"."auth_key_pair_his"."create_time_his" IS '创建时间';
COMMENT ON COLUMN "public"."auth_key_pair_his"."user_id_his" IS '用户名';
COMMENT ON COLUMN "public"."auth_key_pair_his"."data_his" IS '加密的数据';

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
