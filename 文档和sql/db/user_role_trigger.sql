-- 客户端设备只认医师和患者
DROP TRIGGER IF EXISTS `user_role_identity_insert`;
CREATE TRIGGER user_role_identity_insert
    AFTER INSERT ON sys_role_user
    FOR EACH ROW
BEGIN
    update user u set u.identity = NEW.roleId where u.id = NEW.userId and NEW.roleId in (0, 1) and u.identity < 1;
END;

DROP TRIGGER IF EXISTS `user_role_identity_update`;
CREATE TRIGGER user_role_identity_update
    AFTER UPDATE ON sys_role_user
    FOR EACH ROW
    update user u set u.identity = NEW.roleId where u.id = NEW.userId and NEW.roleId in (0, 1) and u.identity < 1;
