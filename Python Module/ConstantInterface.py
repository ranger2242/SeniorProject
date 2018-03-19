class ConstantInterface:

    @staticmethod
    def preprocessing(data):
        return data[:-2]

    @staticmethod
    def check(data):
        data = ConstantInterface.preprocessing(data)
        if data[0] == 0 and data[1] == 1:
            return 1
        else:
            return 0