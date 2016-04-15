## QueueSimulator v1.05 Copyright (C) 2016 Serhan Yılmaz

### About
-----

QueueSimulator is a free software which aims to simulate primarily
G/G/k queueing systems that are used in computer networks. But
it can be used to simulate practically any stable queue system.

### Contact
-------

Github : <https://github.com/yilmazserhan/queuesimulator> <br />
E-mail : yilmazserhan@yahoo.com <br />

You are more than welcome to share your comments, suggestions, 
and bugs that you may have encountered via the given e-mail address.

### Minimum Requirements
--------------------

Java SE Runtime Environment version 7 or later

### Installation Guide
------------------

##### On Linux :

write the following command on command line to run:
java -jar queuesimulator.jar

##### On Windows :

Double click on queuesimulator.jar to run.

### How to use
----------

1 - Enter the parameters to simulate the system :
Server amount - number of parallel servers
Transmission rate - contant transmission rate per server in bps
Queue buffer size - maximum queue size in bits

2 - Specify "Packet Arrival Time Distribution" and "Packet Size Distribution" fields.
You can find more information about distributions in the DISTRIBUTION_INFO.md file.

3 - Select the termination method of simulation;
In terms of input package number
In terms of simulation clock
In terms of computation time

4 - Select the data delay field between %0 and %50.
Data delay field specifies the percentage of simulation data that 
will be discarded until the specified amount is reached. 
Necessary for isolating the stable state data.

5 - Press simulate and observe results.

### License Information
-------------------

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
