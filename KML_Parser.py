"""
This is a program to read a KML file, convert the data into a 3D occupancy grid, and save the results into a CSV file

Do note the KML file must be in a specific format as a CSV File, with x,y,z coordinates be in the form of
xxx.xxxxxx
y.yyyyyy
zz.z

"""

import pykml
from pykml import parser
import occupancyGrid
import sys
import csv


#create KML dictionary to store kml data
kmlData = {}

#function to parse coordinates data. Return a dictionary pairing a name and a list of dictionaries
def ParseCoord(x):
    newStr = "".join(x.replace(" ", ""))
    newStr = "".join(newStr.replace("\n", ""))
    numOfCoords = len(newStr)/23
    newList = []
    for x in range(0,numOfCoords):
        newDict = {}
        newDict.update({'x': float(newStr[0+(23*x):10+(23*x)])})
        newDict.update({'y': float(newStr[11 + (23 * x):19 + (23 * x)])})
        newDict.update({'z': float(newStr[20 + (23 * x):23 + (23 * x)])})
        newList.append(newDict)
    return newList

#code to let User choose which KML file to convert
kmlFile = raw_input("Please type in the name of the KML file you wish to import (e.g. TelokBlangahLite)\n")
kmlFileFull = kmlFile + ".kml"


#open kml file
with open(kmlFileFull) as f:
    folder = parser.parse(f).getroot().Document

#traverse the Placemark folder, parsing each data set to remove whitespace and newline, and then
#adding them into the dictionary
for pm in folder.Placemark:
    #kmlData.update({pm.name.text : ParseCoord(pm.Polygon.outerBoundaryIs.LinearRing.coordinates.text)})
    kmlData.update({pm.name.text: ParseCoord(pm.Polygon.outerBoundaryIs.LinearRing.coordinates.text)})

#global variables to store max and min coordinates
maxX = 0
maxY = 0
maxZ = 0
minX = 9999
minY = 9999
minZ = 9999

"""
for name in kmlData:
    print name
    print kmlData[name]
"""

#code to travese kmlData to update max and min coordinates
for name in kmlData:
    for points in kmlData[name]:
        if points['x'] > maxX:
            maxX = points['x']
        if points['y'] > maxY:
            maxY = points['y']
        if points['z'] > maxZ:
            maxZ = points['z']
        if points['x'] < minX:
                minX = points['x']
        if points['y'] < minY:
                minY = points['y']
        if points['z'] < minZ:
                minZ = points['z']


#code to choose what resolution of grid
resolution = int(input("Please choose approximate resolution: \n1) 0.1m\n2) 1m\n3) 2m\n4) 3.33m\n5) 10m\n\n"))
gridRes = ""

if resolution == 1:
    gridRes = "(0.1m)"
    scale = 1000000
elif resolution == 2:
    gridRes = "(1m)"
    scale = 100000
elif resolution == 3:
    gridRes = "(2m)"
    scale = 50000
elif resolution == 4:
    gridRes = "(3m)"
    scale = 33333
elif resolution == 5:
    gridRes = "(10m)"
    scale = 10000

#code to choose what buffer around building
bufferInput = int(input("Please choose approximate buffer: \n1) 1 grid resolution\n2) 2 grid resolution\n3) 3 grid resolution\n4) 5 grid resolution\n5) 10 grid resolution\n"))
buffer = 0

if bufferInput == 1:
    buffer = 1
elif bufferInput == 2:
    buffer = 2
elif bufferInput == 3:
    buffer = 3
elif bufferInput == 4:
    buffer = 5
elif bufferInput == 5:
    buffer = 10

#normalize the dataset to be in integers
#add a buffer of 10 grids to pad the corner of the occupancy grid
for name in kmlData:
    for points in kmlData[name]:
        points['x'] = float(round((points['x'] - minX) * scale))
        points['y'] = float(round((points['y'] - minY) * scale))

minX = float(round(minX * scale))
minY = float(round(minY * scale))



"""
Code to extend the vertices of the building to create buffer zone
"""
for name in kmlData:
    centroid = {'x' : 0, 'y' : 0}
    count = 0

    for points in kmlData[name]:
        centroid['x'] += points['x']
        centroid['y'] += points['y']
        count += 1

    centroid['x'] /= count
    centroid['y'] /= count

    for points in kmlData[name]:
        if abs(points['x'] - centroid['x']) > abs(points['y'] - centroid['y']):

            if points['y'] > centroid['y']:
                points['y'] += buffer
            elif points['y'] < centroid['y']:
                if points['y'] - buffer > 0:
                    points['y'] -= buffer

            if points['x'] > centroid['x']:
                points['x'] += buffer * round(abs(points['x'] - centroid['x']) / abs(points['y'] - centroid['y']))
            elif points['x'] < centroid['x']:
                if (points['x'] - (buffer * round(abs(points['x'] - centroid['x']) / abs(points['y'] - centroid['y'])))) > 0:
                    points['x'] -= buffer * round(abs(points['x'] - centroid['x']) / abs(points['y'] - centroid['y']))

        else:
            if points['x'] > centroid['x']:
                points['x'] += buffer
            elif points['x'] < centroid['x']:
                if points['x'] - buffer > 0:
                    points['x'] -= buffer

            if points['y'] > centroid['y']:
                points['y'] += buffer * round(abs(points['y'] - centroid['y']) / abs(points['x'] - centroid['x']))
            elif points['y'] < centroid['y']:
                if (points['y'] - (buffer * round(abs(points['y'] - centroid['y']) / abs(points['x'] - centroid['x'])))) > 0:
                    points['y'] -= buffer * round(abs(points['y'] - centroid['y']) / abs(points['x'] - centroid['x']))
"""
"""


"""
Code to create Occupancy Grid
"""

#list to store the the occupied cells
OG = []

#start of crazy algo
for landmark in kmlData:
    #print name of landmark
    print landmark
    #count number of points on the polygon
    numOfCoord = len(kmlData[landmark])
    #iterate through each coordinate point
    for i in range(numOfCoord - 1):
        #initialize the needed variables
        startingPoint = {'x': kmlData[landmark][i]['x'], 'y': kmlData[landmark][i]['y']}
        startingGrid = occupancyGrid.checkGrid(startingPoint['x'], startingPoint['y'])
        endingPoint = {'x': kmlData[landmark][i + 1]['x'], 'y': kmlData[landmark][i + 1]['y']}
        endingGrid = occupancyGrid.checkGrid(endingPoint['x'], endingPoint['y'])
        myGrad = occupancyGrid.gradient(startingPoint['x'], startingPoint['y'], endingPoint['x'], endingPoint['y'])
        currentPoint = startingPoint
        currentGrid = occupancyGrid.checkGrid(currentPoint['x'], currentPoint['y'])
        prevGrid = currentGrid
        errorCount = 0

        #start looping to find which cells lies on the line beween the 2 coordinate points
        while (currentGrid != endingGrid):
            prevGrid = currentGrid
            currentPoint['x'] += myGrad['x']
            currentPoint['y'] += myGrad['y']
            currentGrid = occupancyGrid.checkGrid(currentPoint['x'], currentPoint['y'])
            #print currentPoint  #uncomment this for therapeutic data processing

            #depending on the relative position of the new cell to the old cell, certain cells will be appended
            if occupancyGrid.checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 0:
                pass
            elif occupancyGrid.checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 2:
                for j in range(int(kmlData[landmark][i]['z'] + 1)):
                    OG.append({'x': currentGrid['x'], 'y': currentGrid['y'], 'z': j})
                    OG.append({'x': (currentGrid['x'] - 1), 'y': (currentGrid['y'] - 0), 'z': j})
                    OG.append({'x': (currentGrid['x'] - 0), 'y': (currentGrid['y'] - 1), 'z': j})
            elif occupancyGrid.checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 4:
                for j in range(int(kmlData[landmark][i]['z'] + 1)):
                    OG.append({'x': currentGrid['x'], 'y': currentGrid['y'], 'z': j})
                    OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] + 1), 'z': j})
                    OG.append({'x': (currentGrid['x'] - 1), 'y': currentGrid['y'], 'z': j})
            elif occupancyGrid.checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 6:
                for j in range(int(kmlData[landmark][i]['z'] + 1)):
                    OG.append({'x': currentGrid['x'], 'y': currentGrid['y'], 'z': j})
                    OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] + 1), 'z': j})
                    OG.append({'x': (currentGrid['x'] + 1), 'y': currentGrid['y'], 'z': j})
            elif occupancyGrid.checkUpdate(prevGrid['x'], prevGrid['y'], currentGrid['x'], currentGrid['y']) == 8:
                for j in range(int(kmlData[landmark][i]['z'] + 1)):
                    OG.append({'x': currentGrid['x'], 'y': currentGrid['y'], 'z': j})
                    OG.append({'x': currentGrid['x'], 'y': (currentGrid['y'] - 1), 'z': j})
                    OG.append({'x': (currentGrid['x'] + 1), 'y': currentGrid['y'], 'z': j})
            else:
                for j in range(int(kmlData[landmark][i]['z'] + 1)):
                    OG.append({'x': currentGrid['x'], 'y': currentGrid['y'], 'z': j})

            errorCount += 1
            if errorCount > 50000:
                print "Runaway loop!!!"
                print kmlData[landmark]
                sys.exit("Runaway!")

#for x in OG:
#    print x

print "\n\n\nNumber of Grids: "
print len(OG)
print "\n"

print("X Range = " + str(maxX - minX))
print("Y Range = " + str(maxY - minY))
print("Z Range = " + str(maxZ - minZ))






"""
Code to save Occupancy Grid into CSV file
"""

exportList = []
firstRow = [minX, minY, scale]

print "\nConverting Dictionary to List... \n"

for i in OG:

    if (((i['x']) == -1) or (i['y'] == -1)):
        continue

    subList = []
    subList.append(i['x'])
    subList.append(i['y'])
    subList.append(i['z'])
    exportList.append(subList)

print "Number of grids after filtering (-1): "
print(len(exportList))
print "\n\n"

    #print exportList

print "Writing to CSV file(occupancyGrids.csv)...\n"

csvFileFull = kmlFile + "Grids" + gridRes + ".csv"
with open(csvFileFull, 'wb') as f:
    writer = csv.writer(f)
    writer.writerow(firstRow)
    for row in exportList:
        writer.writerow(row)

print "Writing completed!\n"
