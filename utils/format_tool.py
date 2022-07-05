from datetime import datetime


class FormatTool:
    @staticmethod
    def generate_patient_name():
        now = datetime.now()
        now_time = datetime.strftime(now, "%y%m%d_%H%M%S")
        suffix = now_time
        patient_name = "测_" + suffix
        return patient_name
