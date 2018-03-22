class BoatAnchor:

    @staticmethod
    def preprocessing(data):
        return data[:-2]

    @staticmethod
    def check(data):
        data = BoatAnchor.preprocessing(data)
        return data[0]
