
class PatternChecker:

    def check_all_patterns(self, data):
        long = self.check_long_methods(data[0])
        return [long]

    def check_long_methods(self, data):
        if data[0] > 50:
            return 1
        return 0

