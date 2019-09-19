import math

#accepts 2 points and returns the gradient between them
def gradient(x1, y1, x2, y2):

    if (x2 - x1) == 0:
        theta = 1.57079633
    else:
        theta = abs(math.atan((y2 - y1) / (x2 - x1)))

    x = 0.5 * math.cos(theta)
    y = 0.5 * math.sin(theta)

    if x2 < x1:
        x *= -1
    if y2 < y1:
        y *= -1
    return {'x': x, 'y': y}

#returns the cell which the point lies in. Returns a dictionary of x and y key
def checkGrid(x, y):
    return {'x': int(round(x)), 'y': int(round(y))}

#checks up there is a change in occupied square. Returns 0 if no change, 1(N), 2(NE), 3(E), 4(SE), 5(S), 6(SW), 7(W), 8(NW)
def checkUpdate(x1, y1, x2, y2):
    x11 = checkGrid(x1, y1)['x']
    y11 = checkGrid(x1, y1)['y']
    x22 = checkGrid(x2, y2)['x']
    y22 = checkGrid(x2, y2)['y']

    if (x11 == x22) and (y11 == y22):
        return 0
    elif (x22 == x11) and (y22 == (y11+1)):
        return 1
    elif (x11 == (x22+1)) and (y22 == (y11+1)):
        return 2
    elif (x22 == (x11+1)) and (y22 == y11):
        return 3
    elif (x22 == (x11+1)) and (y22 == (y11-1)):
        return 4
    elif (x22 == x11) and (y22 == (y11-1)):
        return 5
    elif (x22 == (x11-1)) and (y22 == (y11-1)):
        return 6
    elif (x22 == (x11-1)) and (y22 == y11):
        return 7
    elif (x22 == (x11-1)) and (y22 == (y11+1)):
        return 8

"""
OG = []

#initialize variables
startingPoint = {'x': 4437.0, 'y': 1855.0}
startingGrid = checkGrid(startingPoint['x'], startingPoint['y'])
endingPoint = {'x': 3000.0, 'y': 5000.0}
endingGrid = checkGrid(endingPoint['x'], endingPoint['y'])
myGrad = gradient(startingPoint['x'], startingPoint['y'], endingPoint['x'], endingPoint['y'])
currentPoint = startingPoint
currentGrid = checkGrid(currentPoint['x'], currentPoint['y'])
prevGrid = currentGrid
errorCount = 0
OG.append(currentGrid)


while (currentGrid != endingGrid):
    prevGrid = currentGrid
    currentPoint['x'] += myGrad['x']
    currentPoint['y'] += myGrad['y']
    currentGrid = checkGrid(currentPoint['x'], currentPoint['y'])

    if checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 0:
        pass
    elif checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 2:
        OG.append(currentGrid)
        OG.append({'x': (currentGrid['x'] - 1), 'y': (currentGrid['y'] - 0)})
        OG.append({'x': (currentGrid['x'] - 0), 'y': (currentGrid['y'] - 1)})
    elif checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 4:
        OG.append(currentGrid)
        OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] + 1)})
        OG.append({'x': (currentGrid['x'] - 1), 'y': currentGrid['y']})
    elif checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 6:
        OG.append(currentGrid)
        OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] + 1)})
        OG.append({'x': (currentGrid['x'] + 1), 'y': currentGrid['y']})
    elif checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 8:
        OG.append(currentGrid)
        OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] - 1)})
        OG.append({'x': (currentGrid['x'] + 1), 'y': currentGrid['y']})
    else:
        OG.append(currentGrid)

    #print currentPoint

    errorCount += 1
    if errorCount > 50000:
        print "Runaway loop!!!"
        break

print OG

"""