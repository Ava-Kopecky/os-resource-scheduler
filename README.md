# OS Resource Scheduler Simulation

A multithreaded operating system simulation in Java

Java | Multithreading | Systems Programming

## Overview

This project simulates core operating system resource management concepts, including concurrent disk I/O, printer queue scheduling, and a virtual file system. Multiple user threads compete for shared hardware resources, managed through synchronized resource managers that prevent race conditions and deadlocks.

This project demonstrates practical application of OS scheduling principles and concurrent programming in Java.

## Features

- Concurrent UserThread and PrintJobThread execution using Java's Thread class
- Synchronized resource allocation via ResourceManager with wait()/notify() to prevent race conditions
- Simulated disk I/O pipeline — sector-based read/write with configurable delay to model real hardware latency
- Printer queue management across multiple printers with fair resource scheduling
- Virtual file system with a DirectoryManager backed by a Hashtable for O(1) file lookup
- Configurable at runtime: number of users, disks, and printers passed as command-line args

## Architecture

UserThread — reads user command files, handles .save / .end / .print commands
PrintJobThread — spawned per print job; reads from disk and writes to printer concurrently
ResourceManager — generic synchronized manager; extended by DiskManager and PrinterManager
Disk — sector-based storage with simulated latency
DirectoryManager — maps filenames to disk locations (disk number, start sector, length)

## Getting Started

javac MainClass.java
java MainClass -u3 -d2 -p2
*-u* = number of users
*-d* = number of disks
*-p* = number of printers
User command files should be placed in a users/ directory named USER0, USER1, etc.

## Concepts Demonstrated

Thread synchronization and mutual exclusion in Java
Producer-consumer patterns for I/O pipelines
Resource contention and deadlock prevention
Sector-based file storage and directory indexing
