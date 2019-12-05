start = 357253
end = 892942

count = 0

for i in range(start, end):
    numList = [int(d) for d in str(i)]
    hasPair = False
    for num in numList:
        if numList.count(num) == 2:
            hasPair = True
            break

    if (numList == sorted(numList) and hasPair):
        count += 1
print(count)
