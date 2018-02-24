class LongMethod:

    @staticmethod
    def preprocessing(data):
        return data[:-2]

    @staticmethod
    def check(data):
        data = LongMethod.preprocessing(data)
        number_of_long_methods_found = 0
        for i in range(len(data)):
            if data[i] > 50:
                number_of_long_methods_found += 1
        return number_of_long_methods_found
