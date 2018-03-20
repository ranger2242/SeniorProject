class CallSuper:

    @staticmethod
    def preprocessing(data):
        return data[:-2]

    @staticmethod
    def check(data):
        data = CallSuper.preprocessing(data)
        return data[0]
