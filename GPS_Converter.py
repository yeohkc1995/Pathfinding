print "This is a program to convert GPS coordinates to the relavent occupancy grid position: \n\n"

xcoord = float(input("Orignal x-coordinates: "))
ycoord = float(input("Orignal y-coordinates: "))

print "\n\n"

minX = float(input("Minimum X: "))
minY = float(input("Minimum Y: "))
scale = float(input("Scale: "))

print "\n\n"

finalX = (xcoord * scale) - minX
finalY = (ycoord * scale) - minY

print "Final X: " + str(finalX)
print "Final Y: " + str(finalY)