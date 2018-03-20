class ErrorHiding:

    @staticmethod
    def preprocessing(data):
        return data[:-2]

    @staticmethod
    def check(data):
        data = ErrorHiding.preprocessing(data)
        return data[0]