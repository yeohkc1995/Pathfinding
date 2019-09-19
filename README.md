# Pathfinding
This program aims to explore pathfinding algorithms for navigation purpose. There is 2 part to this program.

1) Python Code to generate a CSV occupancy grid file. The program takes in a KML file as input, and outputs a CSV file that stores the 3D coordinates of the occupied area.

2) A 3D A* algorithm that plans a path based on the occupancy grid, and generates a CSV file that stores the waypoint from start point to end point as given by the user in the form of GPS coordinates.

I relied heavily on marcelo-s's A* algorithm. I merely added a 3rd dimension to his Java implementation of the A* algorithm. Credits goes to him for his implmentation. Check out his code at https://github.com/marcelo-s/A-Star-Java-Implementation/tree/master/src/com/ai/astar
