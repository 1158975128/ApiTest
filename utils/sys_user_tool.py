import random
from utils.request.sys_user_request import *

class SysUserTool:
    @staticmethod
    def get_getTherapist_info(headers):
        data = {
            "organizationId": defaultInfo_config.organizationId,
            "roleId": 11
        }
        res = SysUserRequest.getTherapist_request(data, headers)
        therapist_list = jsonpath(res, '$.therapist')[0]
        length = len(therapist_list)
        ret_num = random.randint(0, length - 1)
        return {"therapistId": therapist_list[ret_num]["userId"], "therapistName": therapist_list[ret_num]["name"]}

    @staticmethod
    def get_any_therapist_by_pst(headers, pst_id):
        data = {
            "organizationId": defaultInfo_config.organizationId,
            "postSmallType": pst_id
        }
        res = SysUserRequest.getTherapist_request(data, headers)
        therapist_list = jsonpath(res, '$.therapist')[0]
        therapist = random.choice(therapist_list)
        return {"therapistId": therapist["userId"], "therapistName": therapist["name"]}

    @staticmethod
    def get_any_therapist(headers):
        data = {
            "organizationId": defaultInfo_config.organizationId
        }
        res = SysUserRequest.getTherapist_request(data, headers)
        therapist_list = jsonpath(res, '$.therapist')[0]
        therapist = random.choice(therapist_list)
        return {"therapistId": therapist["userId"], "therapistName": therapist["name"]}

    @staticmethod
    def get_any_doctor(headers):
        data = {
            "organizationId": defaultInfo_config.organizationId,
            "roleId": 10
        }
        res = SysUserRequest.getDoctor_request(data, headers)
        doctor_list = jsonpath(res, '$.doctor')[0]
        if len(doctor_list) == 0:
            return False
        doctor = random.choice(doctor_list)
        return {"doctorId": doctor["userId"], "doctorName": doctor["name"]}

    @staticmethod
    def get_any_assess_user(headers):
        data = {
            "organizationId": defaultInfo_config.organizationId
        }
        res = SysUserRequest.getDoctor_request(data, headers)
        user_list = jsonpath(res, '$.doctor')[0]
        if len(user_list) == 0:
            return False
        user = random.choice(user_list)
        return {"userId": user["userId"], "userName": user["name"]}

    @staticmethod
    def get_patient_list_with_pst(pst, headers=None):
        if headers is None:
            headers = get_login_headers()
        data = {
            "page": 1,
            "limit": 99999,
            "organizationId": defaultInfo_config.organizationId,
            "isTherapist": 1
        }
        res = SysUserRequest.list_request(data, headers)
        code = jsonpath(res, '$.code')[0]
        if code == 0:
            return False
        therapist_list = jsonpath(res, '$.page.list')[0]
        if len(therapist_list) == 0:
            return False
        ret_list = []
        for therapist in therapist_list:
            info_res = SysUserRequest.userId_request(therapist["id"], headers)
            info_pst_list = jsonpath(info_res, '$.user.postSmallType')[0]
            if pst in info_pst_list:
                therapist_id = jsonpath(info_res, '$.user.id')[0]
                therapist_name = jsonpath(info_res, '$.user.name')[0]
                ret_list.append({"id": therapist_id, "name": therapist_name})
        return ret_list
