from datetime import datetime


class PatientTypeObject:
    def __init__(self, id, name, create_time=None, update_time=None):
        self.id = id
        self.name = name
        self.create_time = create_time
        self.update_time = update_time

    @property
    def create(self):
        try:
            self._create = datetime.strftime(self.create_time, "%Y-%m-%d %H:%M:%S")
        except TypeError:
            self._create = None
        finally:
            return self._create

    @property
    def update(self):
        try:
            self._update = datetime.strftime(self.update_time, "%Y-%m-%d %H:%M:%S")
        except TypeError:
            self._update = None
        finally:
            return self._update