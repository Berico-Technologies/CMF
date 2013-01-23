This folder contains the .NET implementation of CMF, as well as examples.

cmf.sln  //the master MSBuild file
|
|-- examples  //contains examples that use the .NET client
|
|-- binaries  //output directory for compiled binaries
|
|  |-- libraries  //we don't use nuget since not everyone has the internet
|
|-- source  //the source for the .NET client implementation
|
|-- tests  //contains unit tests


cmf.examples.sln  //if you're only interested in the example code
|
|-- cmf.exmaples.duplex  //a windows forms app that sends and receives events
|
|-- cmf.examples.messages  //a class library that defines a few simple events