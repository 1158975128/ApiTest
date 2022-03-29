import unittest


class Verify(unittest.TestCase):
    def verifyEqual(self, first, second, msg=None):
        try:
            self.assertEqual(first, second, msg)
        except AssertionError as e:
            print(e)