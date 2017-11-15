
#Read from file into a matrix
text = open("testing.txt", "r").read()
array = list(eval(text))


for i in range(len(array)):
    for j in range(len(array[i])):
        print(array[i][j])
    print()
